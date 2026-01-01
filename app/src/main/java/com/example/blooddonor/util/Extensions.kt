package com.example.blooddonor.util



import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.graphics.Color

fun openDialer(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    context.startActivity(intent)
}

fun sendSMS(context: Context, phoneNumber: String, message: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("smsto:$phoneNumber")
        putExtra("sms_body", message)
    }
    context.startActivity(intent)
}

fun Color.toHex(): String {
    return String.format("#%08X", this.value)
}