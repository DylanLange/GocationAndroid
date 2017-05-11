package com.gocation.gocation_android

/**
 * Created by dylanlange on 11/05/17.
 */

fun String.toFirebaseKey(): String = this.replace('.', '_')
