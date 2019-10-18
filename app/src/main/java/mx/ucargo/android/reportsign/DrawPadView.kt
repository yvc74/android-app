package mx.ucargo.android.reportsign

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View






class DrawPadView : View {


    private var cacheBitmap: Bitmap? = null

    private var cacheCanvas: Canvas? = null

    private var paint: Paint? = null

    private var BitmapPaint: Paint? = null

    private var path: Path? = null

    var alto: Int = 0

    var ancho: Int = 0

    /** Last saved X-coordinate  */
    private var pX: Float = 0.toFloat()
    /** Last saved Y-coordinate */
    private var pY: Float = 0.toFloat()

    /** Initial color  */
    private var paintColor = Color.BLACK

    private var canvas: Canvas? = null


    /** get the alto and ancho  */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        alto = h
        ancho = w
        init()
    }

    private fun init() {
        Log.d("com.drawing.ucargo", "init")
        cacheBitmap = Bitmap.createBitmap(ancho, alto, Config.ARGB_8888)
        cacheCanvas = Canvas(cacheBitmap!!)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        path = Path()
        BitmapPaint = Paint()
        updatePaint()
    }

    private fun updatePaint() {
        paint!!.color = paintColor
        paint!!.style = paintStyle
        paint!!.strokeWidth = paintwidth.toFloat()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                path!!.moveTo(event.x, event.y)
                pX = event.x
                pY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                path!!.quadTo(pX, pY, event.x, event.y)
                pX = event.x
                pY = event.y
            }
            MotionEvent.ACTION_UP -> {
                cacheCanvas!!.drawPath(path!!, paint!!)
                path!!.reset()
            }
        }
        invalidate()

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d("com.drawing.ucargo", "on drawing")
        this.canvas = canvas
        BitmapPaint = Paint()
        canvas.drawBitmap(cacheBitmap!!, 0f, 0f, BitmapPaint)
        canvas.drawPath(path!!, paint!!)
    }


    fun setColor(color: Int) {
        paintColor = color
        updatePaint()
    }


    fun setPaintancho(ancho: Int) {
        paintwidth = ancho
        updatePaint()
    }


    fun setStyle(style: Int) {
        when (style) {
            PEN -> paintStyle = Paint.Style.STROKE
            PAIL -> paintStyle = Paint.Style.FILL
        }
        updatePaint()
    }

    fun clearScreen() {
        isDrawingCacheEnabled = false
        onSizeChanged(width, height, width, height)
        invalidate()
        isDrawingCacheEnabled = true
    }


    companion object {

        private var paintStyle: Paint.Style = Paint.Style.STROKE
        /** Paint Point size  */
        private var paintwidth = 10

        val PEN = 1
        val PAIL = 2
    }

}