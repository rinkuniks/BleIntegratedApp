package com.technoidentity.vitalz.notifications.datamodel

data class NotificationData(
    val title: String,
    val telemetryNotification: VitalzTelemetryNotification,
)

data class VitalzTelemetryNotification(
    val patchId: String?,
    val telemetryKey: String,
    val telemetryValue: String
)
