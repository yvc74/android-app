package mx.ucargo.android.tracking

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class TrackingService : Service() {
    companion object {
        private val TAG = "TrackingService"

        fun newIntent(context: Context): Intent {
            return Intent(context, TrackingService::class.java)
        }
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null;
    }

    object binder : Binder() {

    }
}
