package com.simpledeeds.accelerometerapp

import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Point
import android.os.Vibrator
import android.view.Display
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager

class GroundView(context: Context?) : SurfaceView(context), SurfaceHolder.Callback {

    var cx: Float = 10.toFloat()
    var cy: Float = 10.toFloat()

    var lastGx: Float = 0.toFloat()
    var lastGy: Float = 0.toFloat()

    var picHeight: Int = 0
    var picWidth: Int = 0

    var icon: Bitmap ?= null

    var windowWidth: Int = 0
    var windowHeight: Int = 0

    var noBorderX = false
    var noBorderY = false

    lateinit var vibratorService: Vibrator
    lateinit var thread : DrawThread

    init {
        holder.addCallback(this)
        thread = DrawThread(holder, this)
        val display: Display = (getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val size: Point = Point()
        display.getSize(size)
        windowWidth = size.x
        windowHeight = size.y
        icon = BitmapFactory.decodeResource(resources, R.drawable.ball)
        picHeight = icon!!.height
        picWidth = icon!!.width
        vibratorService = (getContext().getSystemService(Service.VIBRATOR_SERVICE)) as Vibrator
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        thread.setRunning(true)
        thread.start()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (canvas != null) {
            canvas.drawColor(0xFFAAAAA)
            icon?.let { canvas.drawBitmap(it,cx,cy,null) }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            canvas.drawColor(0xFFAAAAA)
            icon?.let { canvas.drawBitmap(it,cx,cy,null) }
        }
    }

    fun updateMe(inx: Float, iny: Float) {
        lastGx += inx
        lastGy += iny

        cx += lastGx
        cy += lastGy

        if(cx > (windowWidth - picWidth)){
            cx = (windowWidth - picWidth).toFloat()
            lastGx = 0F
            if (noBorderX){
                vibratorService.vibrate(100)
                noBorderX = false
            }
        }
        else if(cx < (0)){
            cx = 0F
            lastGx = 0F
            if(noBorderX){
                vibratorService.vibrate(100)
                noBorderX = false
            }
        }
        else{ noBorderX = true }

        if (cy > (windowHeight - picHeight)){
            cy = (windowHeight - picHeight).toFloat()
            lastGy = 0F
            if (noBorderY){
                vibratorService!!.vibrate(100)
                noBorderY = false
            }
        }

        else if(cy < (0)){
            cy = 0F
            lastGy = 0F
            if (noBorderY){
                vibratorService!!.vibrate(100)
                noBorderY= false
            }
        }
        else{ noBorderY = true }

        invalidate()

    }
}
