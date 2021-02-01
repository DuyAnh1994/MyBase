package com.dev.anhnd.mybase.utils.customview.seekbar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import com.dev.anhnd.mybase.R
import com.dev.anhnd.mybase.utils.calculator.CalculatorUtils
import com.dev.anhnd.mybase.utils.log.LogDebug
import kotlin.math.abs
import kotlin.math.roundToInt

@SuppressLint("UseCompatLoadingForDrawables")
class RadiusSeekBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val TAG = RadiusSeekBar::class.java.simpleName

    private var thumbDrawable: Drawable? = null
    private var thumbWidth = 0
    private var thumbHeight = 0

    private var rectView = Rect()
    private var rectThumb = RectF()

    private var rectBackground = RectF()

    private var rectTrack = RectF()

    private var trackProgressPaint = Paint()
    private var trackBackgroundPaint = Paint()

    private var mThumbSize = 0
    private var mLineHeight = 0f

    private var mMeasuredWidth = 0
    private var mMeasuredHeight = 0

    private var mPaddingLeft = 0
    private var mPaddingRight = 0

    private var mSeekLength = 0f
    private var sizeTouchExtraArea = 0

    private var mMin = 0
    private var mMax = 100
    private var mProgress = 100f

    private var roundValue = 25f

    private var spaceThumb = 0

    private var spaceBackgroundAndThumb = 8
    private var spaceTrackWithBackground = 5

    init {

        attrs?.let {
            setLayerType(LAYER_TYPE_SOFTWARE, null)
            val ta = context.obtainStyledAttributes(it, R.styleable.RadiusSeekBar)
            mProgress = ta.getInt(R.styleable.RadiusSeekBar_radius_seek_bar_progress, 100).toFloat()
            if (mProgress < mMin) mProgress = mMin.toFloat() else if (mProgress > mMax) mProgress = mMax.toFloat()
            ta.recycle()
        }

        thumbDrawable = resources.getDrawable(R.drawable.ic_progress_volume, null)

        trackBackgroundPaint.isAntiAlias = true
        trackBackgroundPaint.color = Color.parseColor("#55575A")

        trackProgressPaint.isAntiAlias = true
        trackProgressPaint.color = Color.parseColor("#008FE0")

        mThumbSize = CalculatorUtils.dp2px(context, 16F)
        spaceThumb = CalculatorUtils.dp2px(context, 3F)

        mLineHeight = CalculatorUtils.dp2px(context, 3.5F).toFloat()
        thumbWidth = mThumbSize
        thumbHeight = getHeightByRatio(thumbDrawable!!, thumbWidth)
        sizeTouchExtraArea = context.resources.getDimensionPixelSize(R.dimen._10sdp)

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        LogDebug.e(TAG, "onMeasure")
        mMeasuredWidth = measuredWidth
        mMeasuredHeight = measuredHeight
        mPaddingLeft = paddingLeft
        mPaddingRight = paddingRight

        rectView.set(mPaddingLeft, 0, mMeasuredWidth - paddingRight, mMeasuredHeight)

        mSeekLength = rectTrack.width() - mThumbSize + spaceTrackWithBackground

        measureSeekBar()

        invalidateThumbWithProgress(mProgress)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawTrack(it)

            drawThumb(it)

        }
    }

    fun setProgress(progress: Int) {
        LogDebug.e(TAG, "XXXprogress : $progress")
        var newProgress = progress
        if (newProgress < mMin)
            newProgress = mMin
        if (newProgress > mMax)
            newProgress = mMax
        mProgress = newProgress.toFloat()
        invalidateThumbWithProgress(mProgress)
        invalidate()
    }

    private fun drawTrack(canvas: Canvas) {

        //draw background
        canvas.drawRoundRect(rectBackground.left, rectBackground.top, rectBackground.right, rectBackground.bottom, roundValue, roundValue, trackBackgroundPaint)

        //draw blue track
        canvas.drawRoundRect(rectTrack.left, rectTrack.top, rectThumb.right, rectTrack.bottom, roundValue, roundValue, trackProgressPaint)

        //draw left progress
        //canvas.drawRoundRect(rectTrack.left,rectTrack.top,rectThumb.left - spaceThumb,rectTrack.bottom,roundValue,roundValue,trackProgressPaint)

        // draw right progress
        //canvas.drawRoundRect(rectThumb.right + spaceThumb,rectTrack.top,rectTrack.right,rectTrack.bottom,roundValue,roundValue,trackBackgroundPaint)

    }

    private fun drawThumb(canvas: Canvas) {
        val rectDrawable = Rect()
        rectThumb.round(rectDrawable)
        thumbDrawable?.bounds = rectDrawable
        thumbDrawable?.draw(canvas)
    }

    private fun measureSeekBar() {
        rectBackground.left = rectView.left.toFloat()
        rectBackground.top = rectView.exactCenterY() - (thumbHeight / 2f) - (spaceBackgroundAndThumb / 2f)
        rectBackground.right = rectView.right.toFloat()
        rectBackground.bottom = rectView.exactCenterY() + (thumbHeight / 2f) + (spaceBackgroundAndThumb / 2f)

        rectTrack.left = rectView.left.toFloat() + spaceTrackWithBackground
        rectTrack.top = rectView.exactCenterY() - (thumbHeight / 2f)
        rectTrack.right = rectView.right.toFloat() - spaceTrackWithBackground
        rectTrack.bottom = rectView.exactCenterY() + (thumbHeight / 2f)

        rectThumb.set(
                mPaddingLeft.toFloat(),
                ((rectBackground.centerY() - thumbHeight / 2f)),
                mPaddingLeft + mThumbSize.toFloat(),
                ((rectBackground.centerY() + thumbHeight / 2f))
        )

    }

    private val pointDown = PointF(0f, 0f)
    private var isSeekByUser = false
    private var isSeeking = false
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isEnabled) {
            return false
        }

        return when (event?.actionMasked) {
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                isSeeking = false
                isSeekByUser = false
                parent.requestDisallowInterceptTouchEvent(false)
                false
            }
            MotionEvent.ACTION_DOWN -> {
                pointDown.x = event.x
                isSeekByUser = true
                if (pointDown.x !in rectThumb.left - sizeTouchExtraArea..rectThumb.right + sizeTouchExtraArea) {
                    onSeek((pointDown.x - rectThumb.left).toInt())
                }
                parent.requestDisallowInterceptTouchEvent(true)
                true
            }
            MotionEvent.ACTION_MOVE -> {
                val xDiff = (event.x - pointDown.x).toInt()
                pointDown.x = event.x
                if (isSeeking) {
                    if (isSeekByUser) {
                        onSeek(xDiff)
                    }
                    true
                } else {
                    if (abs(xDiff) >= touchSlop) {
                        isSeeking = true
                        true
                    } else
                        false
                }
            }
            else -> {
                false
            }
        }
    }

    private fun onSeek(distance: Int) {
        setThumbPosition(rectThumb.left + distance)
        mProgress = rectThumb.left.ToProgress().toFloat()
        invalidate()
        rsListener?.onProgressChange(mProgress.toInt())
        LogDebug.e(TAG, "mProgress = : $mProgress")
    }

    private fun setThumbPosition(posLeft: Float) {
        var left = posLeft
        if (posLeft < (mPaddingLeft + spaceTrackWithBackground))
            left = mPaddingLeft.toFloat() + spaceTrackWithBackground
        else if (posLeft > mSeekLength)
            left = (mSeekLength)
        rectThumb.left = left
        rectThumb.right = left + mThumbSize
    }

    private fun getHeightByRatio(drawable: Drawable, width: Int): Int {
        val intrinsicWidth = drawable.intrinsicWidth
        val intrinsicHeight = drawable.intrinsicHeight
        return (1.0f * width * intrinsicHeight / intrinsicWidth).roundToInt()
    }

    private fun invalidateThumbWithProgress(progress: Float) {
        rectThumb.left = progress.toInt().ToDimension()
        rectThumb.right = rectThumb.left + mThumbSize
        LogDebug.e(TAG, "invalidateThumbWithProgress :$progress ${rectThumb.left}")
    }

    private fun Int.ToDimension() =
            (this - mMin).toFloat() / (mMax - mMin) * mSeekLength + mPaddingLeft //+ spaceTrackWithBackground

    private fun Int.ToDimension2() =
            (this - mMin).toFloat() / (mMax - mMin) * mSeekLength + mPaddingLeft - thumbWidth / 2f

    private fun Float.ToProgress() =
            ((this - mPaddingLeft) / mSeekLength * (mMax - mMin) + mMin).toInt()

    private var rsListener: IOnRadiusSeekBarListener? = null

    fun setRadiusSeekBarListener(listener: IOnRadiusSeekBarListener) {
        this.rsListener = listener
    }

    interface IOnRadiusSeekBarListener {
        fun onProgressChange(progress: Int)
    }

}