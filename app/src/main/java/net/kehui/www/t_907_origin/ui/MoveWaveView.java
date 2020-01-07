package net.kehui.www.t_907_origin.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.ui.SparkView.ScrubGestureDetector;
import net.kehui.www.t_907_origin.ui.SparkView.SparkView;


/**
 * Create by jwj on 2019/12/12 16:12
 */
public class MoveWaveView extends View implements ScrubGestureDetector.ScrubListener {
    private ScrubGestureDetector scrubGestureDetector;//滑动区域滑动监听
    private ViewMoveWaveListener viewMoveWaveListener; //滑块滑动监听
    private final RectF contentRect = new RectF();//滑动矩形区域
    private RectF sparkViewRect = new RectF();//波形区域
    @ColorInt
    private int viewColor; //滑块的颜色
    private float viewWidth;//滑块的宽度
    private float viewHeight;//滑块的高度
    private Paint scrubLinePaint;//滑块的画笔
    //滑块移动的y轴偏移量
    int topY;
    int leftY;
    private int orientationMoveWaveView;
    private int density = 1;

    private float currentMoverY = 0;
    private float currentY = 0;

    private float current510Y = 0;

    public void setViewMoveWaveListener(ViewMoveWaveListener viewMoveWaveListener) {
        this.viewMoveWaveListener = viewMoveWaveListener;
    }

    public MoveWaveView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public void setSparkViewRect(RectF sparkViewRect) {
        this.sparkViewRect = sparkViewRect;
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.move_MoveWaveView, defStyleAttr, defStyleRes);
        viewColor = a.getColor(R.styleable.move_MoveWaveView_move_viewColor, 0);
        viewWidth = a.getDimension(R.styleable.move_MoveWaveView_move_viewWidth, 0);
        viewHeight = a.getDimension(R.styleable.move_MoveWaveView_move_viewHeight, 0);
        orientationMoveWaveView = a.getInt(R.styleable.move_MoveWaveView_move_viewOrientation, 0);
        a.recycle();

        scrubLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scrubLinePaint.setStyle(Paint.Style.FILL);
        scrubLinePaint.setStrokeWidth(viewWidth);
        scrubLinePaint.setColor(viewColor);
        scrubLinePaint.setStrokeCap(Paint.Cap.ROUND);

        final Handler handler = new Handler();
        final float touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        scrubGestureDetector = new ScrubGestureDetector(this, handler, touchSlop);
        scrubGestureDetector.setEnabled(true);
        setOnTouchListener(scrubGestureDetector);
    }

    public MoveWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);

    }

    public MoveWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public MoveWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (viewWidth < getWidth()) {
            canvas.drawRect(0 + leftY, 0 + topY, viewWidth + leftY, viewHeight + topY, scrubLinePaint);// 长方形
        } else {
            canvas.drawRect(0, 0 + topY, viewWidth, viewHeight + topY, scrubLinePaint);// 长方形
        }
    }

    @Override
    public void onActionDown(float x, float y) {

        currentY = y;
    }

    @Override
    public void onScrubbed(float x, float y) {
        if (orientationMoveWaveView == 0) {
            if (x >= viewWidth / 2 && x <= getWidth() - viewWidth / 2) {
                float moveX = x - (viewWidth / 2);
                viewMoveWaveListener.onMoved(moveX, y);
            }
        } else {
            float moveValue = 0;
            moveValue = y - currentY;

            currentMoverY = currentMoverY + moveValue;
            if (currentMoverY >= 0 && currentMoverY <= getHeight() - viewHeight) {
                setMoveViewVerticalMove(currentMoverY);
            }
            current510Y = current510Y + (255 * moveValue) / (getHeight()) * 2;
            if (currentMoverY >= 0 && currentMoverY <= getHeight() - viewHeight) {
                viewMoveWaveListener.onMoved(x, current510Y);
            }
            currentY = y;
            /*float h1 = getHeight() - sparkViewRect.bottom;
            float scale = h1 / getHeight();
            if (y >= 0 && y <= getHeight() + viewHeight / 2) {
                float moveY = y * scale - (h1 / 2);
                viewMoveWaveListener.onMoved(x, moveY);
                setMoveViewVerticalMove((y * scale) - viewHeight + h1 / 2);
                //setMoveViewVerticalMove(y - viewHeight + h1 / 2);
                Log.e("【垂直滑条】", "X:" + x + "/Y:" + y + "/getHeight():" + getHeight() + "/moveY:" + moveY + "/滑块y:" + ((y * scale) - viewHeight + h1 / 2));
            }*/

        }

    }

    public void setMoveViewVerticalMove(float y) {
        topY = (int) y;
        invalidate();
    }

    //初始化滑块位置
    public void setMoveViewHorizatolMove(float x) {
        leftY = (int) x;
        invalidate();
    }

    @Override
    public void onScrubEnded() {
        viewMoveWaveListener.onMoveEnded();
    }

    public interface ViewMoveWaveListener {
        void onMoved(float x, float y);

        void onMoveEnded();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        updateContentRect();
        if (orientationMoveWaveView == 0) {
            setViewWidth(getWidth());
        }
    }


    private void updateContentRect() {
        if (contentRect == null) {
            return;
        }

        contentRect.set(
                getPaddingStart(),
                getPaddingTop(),
                getWidth() - getPaddingEnd(),
                getHeight() - getPaddingBottom()
        );
        if (orientationMoveWaveView == 0) {
            float scale = 255.0f / 510.0f;
            setMoveViewHorizatolMove(scale * getWidth() - viewWidth / 2);
        } else {
            //滑块滑动的偏移量
            float h3 = contentRect.bottom / 2 - viewHeight / 2;
            currentMoverY = h3;
            setMoveViewVerticalMove(h3);
        }

    }

    public RectF getContentRect() {
        return contentRect;
    }

    public void setViewWidth(float parentWidth) {
        this.viewWidth = parentWidth / density;

        invalidate();
    }

    public float getViewWidth() {
        return viewWidth;
    }

    public void setDensity(float parentWidth, int density) {
        this.density = density;
        setViewWidth(parentWidth);
    }
}
