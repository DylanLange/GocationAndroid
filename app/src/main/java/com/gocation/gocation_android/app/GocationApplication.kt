package com.gocation.gocation_android.app

import android.app.Application
import com.facebook.FacebookSdk
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Region
import org.altbeacon.beacon.startup.BootstrapNotifier
import org.altbeacon.beacon.startup.RegionBootstrap

/**
 * Created by dylanlange on 11/05/17.
 */

class GocationApplication:
        Application(),
        BootstrapNotifier {

    lateinit private var mBeaconManager: BeaconManager
    lateinit private var mRegionBootstrap: RegionBootstrap

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(this)

        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        // beaconManager.getBeaconParsers().add(new BeaconParser().
        //        setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        mBeaconManager = BeaconManager.getInstanceForApplication(this)

        mBeaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))

        // wake up the app when any beacon is seen (you can specify specific id filers in the parameters below)
        val region = Region("myMonitoringUniqueId", null, null, null)
        mRegionBootstrap = RegionBootstrap(this, region)
    }

    override fun didDetermineStateForRegion(p0: Int, p1: Region?) {

    }

    override fun didEnterRegion(p0: Region?) {
        // This call to disable will make it so the activity below only gets launched the first time a beacon is seen (until the next time the app is launched)
        // if you want the Activity to launch every single time beacons come into view, remove this call.
        mRegionBootstrap.disable()

//        startService(Intent(this, BackgroundBeaconService::class.java))
    }

    override fun didExitRegion(p0: Region?) {

    }
}