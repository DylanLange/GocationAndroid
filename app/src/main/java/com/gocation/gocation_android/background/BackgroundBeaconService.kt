//package nz.co.paperkite.pk_office_android.background
//
//import android.app.Service
//import android.content.Intent
//import android.content.SharedPreferences
//import android.os.IBinder
//import android.os.IInterface
//import android.os.Parcel
//import android.os.RemoteException
//import android.preference.PreferenceManager
//import android.util.Log
//import com.gocation.gocation_android.EMAIL_PREFS_KEY
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import nz.co.paperkite.pk_office_android.EMAIL_PREFS_KEY
//import nz.co.paperkite.pk_office_android.PK_BEACONS
//import nz.co.paperkite.pk_office_android.data.models.SimpleBeacon
//import org.altbeacon.beacon.*
//import org.altbeacon.beacon.powersave.BackgroundPowerSaver
//import java.io.FileDescriptor
//
///**
// * Created by dylanlange on 5/05/17.
// */
//
//class BackgroundBeaconService: Service(), BeaconConsumer {
//
//    val TAG: String = BackgroundBeaconService::class.java.simpleName
//
//    val MAX_NO_BEACON_PINGS: Int = 5//how many times the phone will consecutively need to not find a beacon to say not in office
//
//    var mBeacons: List<SimpleBeacon> = emptyList()
//    var mNoBeaconCount: Int = MAX_NO_BEACON_PINGS
//    lateinit var mEmail: String
//    lateinit var mBeaconManager: BeaconManager
//    lateinit var mBackgroundPowerSaver: BackgroundPowerSaver//apparently holding reference to this in the activity saves about 60% battery?
//
//    override fun onCreate() {
//        super.onCreate()
//        init()
//    }
//
//    private fun init() {
//        mBeaconManager = BeaconManager.getInstanceForApplication(this)
//        mBackgroundPowerSaver = BackgroundPowerSaver(this)
//        mBeaconManager.bind(this)
//
//        var prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
//
//        mEmail = prefs.getString(EMAIL_PREFS_KEY, "NO_EMAIL")
//    }
//
//    override fun onBind(intent: Intent?): IBinder {
//        return LocalBinder()
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        if(intent == null) init()
//        return START_STICKY
//    }
//
//    override fun onBeaconServiceConnect() {
//        setupMonitor()
//        setupRangeNotifier()
//        try {
//            mBeaconManager.startMonitoringBeaconsInRegion(Region("myMonitoringUniqueId", null, null, null))
//            mBeaconManager.startRangingBeaconsInRegion(Region("myRangingUniqueId", null, null, null))
//        } catch (e: RemoteException) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun setupMonitor(){
//        mBeaconManager.addMonitorNotifier(object: MonitorNotifier {
//
//            override fun didDetermineStateForRegion(state: Int, region: Region?) {
//                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state)
//            }
//
//            override fun didEnterRegion(region: Region?) {
//                Log.i(TAG, "I just saw a beacon for the first time!")
//            }
//
//            override fun didExitRegion(region: Region?) {
//                Log.i(TAG, "I no longer see a beacon")
//            }
//
//        })
//    }
//
//    private fun setupRangeNotifier() {
//        mBeaconManager.addRangeNotifier(object: RangeNotifier {
//
//            override fun didRangeBeaconsInRegion(beacons: MutableCollection<Beacon>?, region: Region?) {
//                mBeacons = emptyList()
//                if(beacons == null) return
//                Log.d(TAG, mNoBeaconCount.toString())
//                Log.d(TAG, "BEACONS IN RANGE: ${beacons.size}")
//                for(b: Beacon in beacons){
//                    mBeacons = mBeacons.plus(
//                            SimpleBeacon(
//                                    b.id1.toString(),   //UUID
//                                    b.id2.toInt(),      //Major
//                                    b.id3.toInt()       //Minor
//                            )
//                    )
//                }
//                if(beacons.isNotEmpty() && beaconsAreFromPK(mBeacons)) {
//                    mNoBeaconCount = 0
//                } else {
//                    mNoBeaconCount++
//                }
//                setFirebaseInOffice(isInOffice())
//            }
//
//        })
//    }
//
//    private fun hasntSeenBeaconInTooLong(): Boolean = mNoBeaconCount > MAX_NO_BEACON_PINGS
//
//    /**
//     * Holy sh*t this confused me.
//     * Soooo the idea here is for the user's status for in_office to be set to true as soon as they detect a PK beacon
//     * and only be set to false if it hasn't seen a PK beacon in over MAX_NO_BEACON_PINGS scans. This way, if the phone
//     * fails to pick up a beacon a few times in between successful scans for whatever reason, the user will still appear
//     * as in the office.
//     *
//     * The reason I did this was because sometimes I was picking up 0 beacons while in the office. Also I think it's probably
//     * a better approach this way overall.
//     */
//    private fun isInOffice(): Boolean {
//        if(beaconsAreFromPK(mBeacons)){
//            return true
//        } else {
//            return !hasntSeenBeaconInTooLong()
//        }
//    }
//
//    private fun beaconsAreFromPK(beacons: List<SimpleBeacon>) = beacons.filter { PK_BEACONS.contains(it) }.isNotEmpty()
//
//    private fun setFirebaseInOffice(inOffice: Boolean) {
//        if(mEmail == "NO_EMAIL") return//this happens when the application starts this service and no preferences have been saved
//        var usersReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
//        usersReference.child(mEmail).updateChildren(
//                mapOf(
//                        "in_office" to inOffice
//                )
//        )
//        Log.d(TAG, "SETTING FIREBASE IN OFFICE: $inOffice")
//    }
//
//    private class LocalBinder: IBinder {
//        override fun getInterfaceDescriptor(): String = ""
//
//        override fun isBinderAlive(): Boolean = true
//
//        override fun linkToDeath(recipient: IBinder.DeathRecipient?, flags: Int) { }
//
//        override fun queryLocalInterface(descriptor: String?): IInterface = IInterface { this }
//
//        override fun transact(code: Int, data: Parcel?, reply: Parcel?, flags: Int): Boolean = false
//
//        override fun dumpAsync(fd: FileDescriptor?, args: Array<out String>?) { }
//
//        override fun dump(fd: FileDescriptor?, args: Array<out String>?) { }
//
//        override fun unlinkToDeath(recipient: IBinder.DeathRecipient?, flags: Int): Boolean = false
//
//        override fun pingBinder(): Boolean = false
//
//    }
//
//}