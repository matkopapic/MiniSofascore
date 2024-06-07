package com.example.minisofascore.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.minisofascore.R
import kotlin.math.min

class CircularGraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {



    private var progress = 0

    private var highlightColor = Color.BLACK
    private var backgroundColor = Color.WHITE

    init {
        setCustomAttributes(attrs)
    }

    private val backgroundPaint = Paint().apply {
        color = backgroundColor
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
    }

    private val progressPaint = Paint().apply {
        color = highlightColor
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
    }

    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }

    private fun setCustomAttributes(attrs: AttributeSet?) {
        val attrArray = context.obtainStyledAttributes(attrs, R.styleable.CircularGraph)
        highlightColor = attrArray.getColor(R.styleable.CircularGraph_highlightColor, Color.BLACK)
        backgroundColor = attrArray.getColor(R.styleable.CircularGraph_nonHighlightColor, Color.WHITE)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(centerX, centerY) - backgroundPaint.strokeWidth / 2

        canvas.drawCircle(centerX, centerY, radius, backgroundPaint)

        val sweepAngle = progress * 360 / 100f
        canvas.drawArc(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius,
            -90f,
            sweepAngle,
            false,
            progressPaint
        )
    }
}