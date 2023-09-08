package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator.ofInt(0, 360).setDuration(2000)

    private var backgroundColor = 0
    private var textColor = 0
    private var loadingColor = 0
    private var iconCircleColor = 0

    private var statusText = ""
    private var progress = 0

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 80.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                statusText = resources.getString(R.string.button_loading)

                valueAnimator.start()

            }

            ButtonState.Completed -> {
                statusText = resources.getString(R.string.button_name)
                valueAnimator.cancel()

                progress = 0
            }

            ButtonState.Clicked -> {
                // nothing
            }
        }
        invalidate()
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            backgroundColor = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            textColor = getColor(R.styleable.LoadingButton_textStatusColor, 0)
            loadingColor = getColor(R.styleable.LoadingButton_loadingColor, 0)
            iconCircleColor = getColor(R.styleable.LoadingButton_iconCircleColor, 0)
        }
        buttonState = ButtonState.Completed
        valueAnimator.apply {
            addUpdateListener {
                progress = it.animatedValue as Int
                invalidate()
            }
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = backgroundColor
        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
        paint.color = loadingColor
        canvas?.drawRect(0f, 0f, widthSize * progress / 360f, heightSize.toFloat(), paint)
        paint.color = textColor
        canvas?.drawText(statusText, widthSize / 2.0f, heightSize / 2.0f + 30.0f, paint)
        paint.color = iconCircleColor
        canvas?.drawArc(
            widthSize - 200f, 50f, widthSize - 100f, 150f, 0f, progress.toFloat(), true, paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w), heightMeasureSpec, 0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}