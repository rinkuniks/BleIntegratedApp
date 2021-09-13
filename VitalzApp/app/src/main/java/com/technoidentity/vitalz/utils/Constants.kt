package com.technoidentity.vitalz.utils

object Constants {

    const val GATT_MIN_MTU_SIZE = 23
    const val DOCTOR = "doctor"
    const val NURSE = "nurse"
    const val CARETAKER = "careTaker"

    /** Maximum BLE MTU size as defined in gatt_api.h. */
    const val GATT_MAX_MTU_SIZE = 517
    const val ENABLE_BLUETOOTH_REQUEST_CODE = 1
    const val LOCATION_PERMISSION_REQUEST_CODE = 2
    const val BLE_START_SCAN = "BleutoothStartScan"
    const val REQUEST_ENABLE_BT = 32001
    const val REQUEST_LOCATION_SERVICE = 32002
    const val PERMISSION_REQUEST_FINE_LOCATION = 32003


}

/**
 *  Vitalz constants
 */
enum class VitalzConstant(val item : String){
    HEART_RATE("heartrate"),
    RESPIRATORY("respiratory"),
    BLOOD_PRESSURE("bloodpressure"),
    TEMPERATURE("temprature"),
    OXYGEN("oxygen")
}