package com.technoidentity.vitalz.data.network

object Urls {

    const val BASE_URL = "http://20.204.14.2/"

    const val SEND_OTP = "login/caretaker/send-otp"

    const val CARETAKER_LOGIN = "login/caretaker/login"

    const val DOC_NURSE_LOGIN = "login/staff/login"

    const val HOSPITAL_LIST = "login/hospital-name"

    const val PATIENT_LIST = "login/patient-caretakers"

    const val SINGLE_PATIENT_DASHBOARD = "graph/patient/{id}"

    const val MULTIPLE_PATIENT_DASHBOARD = "graph/patients"

    const val REGISTER_DEVICE = "device/register-device"

    const val DASHBOARD_DETAIL = "http://20.204.14.2/graph/item"

    const val SEND_DEVICE = "/device/save-device"

    const val GET_DEVICE_LIST = "device/device-list"

    const val UPDATE_DEVICE = "device/update-device"

    const val SEND_TELEMETRY = "graph/update-telemetry-data"

    const val SEARCH_MULTI_PATIENT = "graph/search/{parameter}"

    const val SEARCH_PATIENT = "graph/search/{parameter}"

    const val SEARCH_HOSPITAL= "login/hospital-name"

    const val DOCTOR_NOTIFICATION = "http://20.204.14.2/notification/doctor"

    const val NURSE_NOTIFICATION = "http://20.204.14.2/notification/admin-nurse"

    const val CARE_TAKER_NOTIFICATION = "http://20.204.14.2/notification/patient"

    const val PROFILE_UPDATE = "http://20.204.14.2/notification/patient"

    const val SEND_ECGDATA = ""

    const val SEND_TELEMETRY_NOTIFICATION = "notification/add-notification"
}
