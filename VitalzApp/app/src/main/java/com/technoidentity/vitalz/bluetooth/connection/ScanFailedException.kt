package com.technoidentity.vitalz.bluetooth.connection

import android.bluetooth.le.ScanCallback

class ScanFailedException(private val errorCode: Int) : Exception() {

    override val message: String?
        get() = when (errorCode) {
            ScanCallback.SCAN_FAILED_ALREADY_STARTED -> "SCAN_FAILED_ALREADY_STARTED"
            ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> "SCAN_FAILED_APPLICATION_REGISTRATION_FAILED"
            ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED -> "SCAN_FAILED_FEATURE_UNSUPPORTED"
            ScanCallback.SCAN_FAILED_INTERNAL_ERROR -> "SCAN_FAILED_INTERNAL_ERROR"
            else -> "SCAN_FAILED_UNKNOWN_ERROR"
        }
}