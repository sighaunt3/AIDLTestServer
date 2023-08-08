package com.example.aidltest1

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.text.TextUtils
    import android.os.Process

class ICPService: Service() {
    companion object {
        var connectionCount: Int = 0
        val NOT_SENT = "Not sent!"
    }

    private val binder = object : IMyAidlInterface.Stub() {

        override fun getPid(): Int = Process.myPid()

        override fun getConnectionCount(): Int = ICPService.connectionCount

        override fun setDisplayedValue(packageName: String?, pid: Int, data: String?) {
            val clientData =
                if (data == null || TextUtils.isEmpty(data)) NOT_SENT
                else data

            RecentClient.client = Client(
                packageName ?: NOT_SENT,
                pid.toString(),
                clientData,
                "AIDL"
            )
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        connectionCount++
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        RecentClient.client = null
        return super.onUnbind(intent)
    }
}