package mx.ucargo.android.tracking

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class TrackingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startService(TrackingService.newIntent(this))
    }
}
