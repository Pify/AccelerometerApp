package com.simpledeeds.accelerometerapp

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var mSensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    lateinit var ground: GroundView

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)

        //get reference of service
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //focus in Accelerometer
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        //setup the window
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            View.SYSTEM_UI_FLAG_FULLSCREEN
            View.SYSTEM_UI_FLAG_IMMERSIVE
        }

        //set view
        ground = GroundView(this)
        setContentView(ground)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            ground.updateMe(event.values[1] , event.values[0])
        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
    }
}