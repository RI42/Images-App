package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View


class WaveView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {
    
    private val wavePath = Path()
    private val linePaint = Paint()
    private val itemWidth: Int
    private val originalData: IntArray
    private var measuredData: IntArray? = null

    init {
        val displayMetrics = context.resources.displayMetrics
        var itemWidthFromAttr = (TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            DEFAULT_ITEM_WIDTH_DP.toFloat(),
            displayMetrics
        ) + 0.5f).toInt()
        var itemColorFromAttr = DEFAULT_ITEM_COLOR
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveView)
            itemWidthFromAttr =
                typedArray.getDimensionPixelSize(R.styleable.WaveView_itemWidth, itemWidthFromAttr)
            itemColorFromAttr =
                typedArray.getColor(R.styleable.WaveView_itemColor, itemColorFromAttr)
            typedArray.recycle()
        }
        itemWidth = itemWidthFromAttr
        originalData = WaveRepository.getWaveData()
        linePaint.style = Paint.Style.STROKE
        linePaint.color = itemColorFromAttr
        linePaint.strokeWidth = itemWidthFromAttr.toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measuredWidth = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val leftPadding = paddingLeft
        val rightPadding = paddingRight
        var width = originalData.size * itemWidth * 2 - itemWidth + leftPadding + rightPadding
        width = resolveSize(width, widthMeasureSpec)
        val itemCount = (measuredWidth + itemWidth - leftPadding - rightPadding) / (itemWidth * 2)
        measuredData = LinearInterpolation.interpolateArray(originalData, itemCount)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        if (measuredData == null) {
            return
        }
        wavePath.reset()
        val paddingTop = paddingTop
        val measuredHeight = measuredHeight - paddingTop - paddingBottom
        var currentX = itemWidth + paddingLeft
        for (data in measuredData!!) {
            val height: Float = data.toFloat() / WaveRepository.MAX_VOLUME * measuredHeight
            val startY = measuredHeight.toFloat() / 2f - height / 2f + paddingTop
            val endY = startY + height
            wavePath.moveTo(currentX.toFloat(), startY)
            wavePath.lineTo(currentX.toFloat(), endY)
            currentX += itemWidth * 2
        }
        canvas.drawPath(wavePath, linePaint)
    }

    companion object {
        private const val DEFAULT_ITEM_WIDTH_DP = 2
        private const val DEFAULT_ITEM_COLOR = Color.BLACK
    }


}