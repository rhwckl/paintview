package com.son.paintview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import rx.Subscriber

/**
 * Created by son on 2/14/17.
 */

class CustomPaint(color: Int = Color.BLACK, strokeWidth: Float = 10.0f) : Paint() {
    init {
        this.color = color
        this.strokeWidth = strokeWidth
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }
}

class PaintView : View {
    val path = Path()
    val paint = CustomPaint()
    var onTouchListener: Subscriber<PointF>? = null

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int):
            super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        onTouchListener?.onNext(PointF(x, y))

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                onTouchListener?.onCompleted()
                path.lineTo(x, y)
                invalidate()
            }
        }

        return true
    }
}
