package com.gocation.gocation_android

import com.gocation.gocation_android.data.SimpleBeacon

/**
 * Created by dylanlange on 11/05/17.
 */

val ID_PREFS_KEY: String = "ID_PREFS_KEY"
val NAME_PREFS_KEY: String = "NAME_PREFS_KEY"
val EMAIL_PREFS_KEY: String = "EMAIL_PREFS_KEY"
val AGE_RANGE_PREFS_KEY: String = "AGE_RANGE_PREFS_KEY"
val IMAGE_URL_PREFS_KEY: String = "IMAGE_URL_PREFS_KEY"
val GENDER_PREFS_KEY: String = "GENDER_PREFS_KEY"

val BEACONS: List<SimpleBeacon> = listOf(
        SimpleBeacon("20cae8a0-a9cf-11e3-a5e2-0800200c9a66", 87, 61777),
        SimpleBeacon("20cae8a0-a9cf-11e3-a5e2-0800200c9a66", 27, 5951),
        SimpleBeacon("92ab49be-4127-42f4-b532-90faf1e26491", 1200, 5939),
        SimpleBeacon("50765cb7-d9ea-4e21-99a4-fa879613a492", 40589, 36634)
)