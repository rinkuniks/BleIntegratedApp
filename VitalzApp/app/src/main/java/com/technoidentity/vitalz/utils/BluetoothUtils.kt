package com.technoidentity.vitalz.utils

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*

/**
 * Ble constants
 */

const val SERVICE_UUID = "ba20fd05-ea08-446d-b007-5dac7b2d1d3b"

val HEART_RATE_SER_UUID = UUID.fromString("03B80E5A-EDE8-4B33-A751-6CE34EC4C700")
val HEART_RATE_CHAR_UUID = UUID.fromString("03B80E5B-EDE8-4B33-A751-6CE34EC4C700")
val ECG_RATE_CHAR_UUID = UUID.fromString("03B80E5E-EDE8-4B33-A751-6CE34EC4C700")

val DEVICE_BATTERY_SER_UUID = UUID.fromString("04B8055C-EDE8-4B44-A785-6CE44EC4C755")
val DEVICE_BATTERY_CHAR_UUID = UUID.fromString("04B87594-EDE8-4B44-A785-6CE44EC4C755")

val BODY_POS_SER_UUID = UUID.fromString("04B8055C-EDE8-4B44-A751-6CE44EC4C745")
val BODY_POS_CHAR_UUID = UUID.fromString("04B875D4-EDE8-4B44-A751-6CE44EC4C745")

val TEMP_SER_UUID = UUID.fromString("03B80E5C-EDE8-4B33-A751-6CE34EC4C745")
val TEMP_CHAR_UUID = UUID.fromString("03B875D4-EDE8-4B33-A751-6CE34EC4C745")

const val CHAR_READ_INITIALIZED_SUCCESS = "Character read initialized successfully"
const val CHAR_READ_INITIALIZED_FAILURE = "Character read failed"
const val DEVICE_START_NAME = "HRM"

const val HEART_RATE_DATA = "HEART_RATE"
const val BATTERY_DATA = "BATTERY_DATA"
const val ECG_DATA = "ecg"

const val ACTIVE_STATUS = "ACTIVE"
const val INACTIVE_STATUS = "INACTIVE"

const val CONNECTION_GOOD = "GOOD"
const val CONNECTION_BAD = "BAD"


/**
 * Telemetry Key
 */

//enum class TelemetryKey(val value: String) {
//    HEART_RATE_DATA("heartrate"),
//    BATTERY_DATA("battery"),
//    ECG_DATA("ecg");
//
//    companion object {
//        fun create(value: String): TelemetryKey{
//            return when(value) {
//                "heartrate" -> HEART_RATE_DATA
//                "battery" -> BATTERY_DATA
//                "ecg" -> ECG_DATA
//                else -> throw IllegalStateException()
//            }
//        }
//    }
//}


/** UUID of the Client Characteristic Configuration Descriptor (0x2902). */
const val CCC_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805F9B34FB"

var HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb"
var CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb"


fun BluetoothDevice.patchId(patchId: String? = null): String = patchId ?: "Unnamed"

fun equalsDevice(macId: String, bluetoothDevice: BluetoothDevice): Boolean = (macId == bluetoothDevice.address)

fun BluetoothDevice.diplayName(device: BluetoothDevice) = if (device.name.startsWith(DEVICE_START_NAME)) device.name else "Unnamed"

fun BluetoothGatt.findCharacteristic(uuid: UUID): BluetoothGattCharacteristic? {
    services?.forEach { service ->
        service.characteristics?.firstOrNull { characteristic ->
            characteristic.uuid == uuid
        }?.let { matchingCharacteristic ->
            return matchingCharacteristic
        }
    }
    return null
}

fun BluetoothGatt.findDescriptor(uuid: UUID): BluetoothGattDescriptor? {
    services?.forEach { service ->
        service.characteristics.forEach { characteristic ->
            characteristic.descriptors?.firstOrNull { descriptor ->
                descriptor.uuid == uuid
            }?.let { matchingDescriptor ->
                return matchingDescriptor
            }
        }
    }
    return null
}

fun BluetoothGattDescriptor.isReadable(): Boolean =
    containsPermission(BluetoothGattDescriptor.PERMISSION_READ)

fun BluetoothGattDescriptor.isWritable(): Boolean =
    containsPermission(BluetoothGattDescriptor.PERMISSION_WRITE)

fun BluetoothGattDescriptor.containsPermission(permission: Int): Boolean =
    permissions and permission != 0

/**
 * Convenience extension function that returns true if this [BluetoothGattDescriptor]
 * is a Client Characteristic Configuration Descriptor.
 */
fun BluetoothGattDescriptor.isCccd() =
    uuid.toString().toUpperCase(Locale.US) == CCC_DESCRIPTOR_UUID.toUpperCase(Locale.US)

fun BluetoothGattCharacteristic.isReadable(): Boolean =
    containsProperty(BluetoothGattCharacteristic.PROPERTY_READ)

fun BluetoothGattCharacteristic.isIndicatable(): Boolean =
    containsProperty(BluetoothGattCharacteristic.PROPERTY_INDICATE)

fun BluetoothGattCharacteristic.isNotifiable(): Boolean =
    containsProperty(BluetoothGattCharacteristic.PROPERTY_NOTIFY)

fun BluetoothGattCharacteristic.containsProperty(property: Int): Boolean =
    properties and property != 0

// ByteArray
fun ByteArray.toHexString(): String =
    joinToString(separator = " ", prefix = "0x") { String.format("%02X", it) }
