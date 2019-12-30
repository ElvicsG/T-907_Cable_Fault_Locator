package net.kehui.www.t_907_origin.cursor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

/**
 *
 * @author IF
 * @date 2018/5/30
 */

public class Cursor extends View {

    private Paint mTextPaint;
    private Paint mCursorPaint;
    private float progress = 10;
    private int viewHeight;
    private int viewWidth;
    private boolean isCanMove;

    public Cursor(Context context) {
        super(context);
        init();
    }

    public Cursor(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Cursor(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        //背景颜色
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.CYAN);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(2);
        mTextPaint.setTextSize(48);
        //光标颜色
        mCursorPaint = new Paint();
        mCursorPaint.setAntiAlias(true);
        mCursorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCursorPaint.setColor(Color.RED);
        mCursorPaint.setStrokeWidth(4);
    }

    /**
     *
     * 测量view 高度宽度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(viewHeight(widthMeasureSpec),
                viewWidth(heightMeasureSpec));
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
    }

    /**
     *
     *EXACTCLY：宽和高为具体值，或为match_parent（父布局的大小）属性时系统会用此模式
     *AT_MOST：  布局文件中的宽和高为wrap_content 属性时，控件的大小一般会随着子View大小大或内容的多少的变化而变化
     *UNSPECIFIED：view大小没有限制，想多大就多大
     */
    private int viewHeight(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result=75;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;

    }

    private int viewWidth(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 75;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;

    }

    /**
     * 画线画圆
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.restore();
        canvas.drawLine(progress, 0, progress, viewHeight, mCursorPaint);
        canvas.drawCircle(progress, viewHeight, 10, mCursorPaint);
        BigDecimal bd = new BigDecimal((progress - 18) / 180);
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        mTextPaint.setTextSize(48);
        canvas.drawText(bd.floatValue() + "m", viewWidth -100, 100, mTextPaint);
    }

    /**
     *
     * 触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isCanMove = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isCanMove) {
                    return false;
                }
                float x = event.getX() - 10;
                progress = x;
                invalidate();
                break;
                default:
                    break;
        }
        return true;
    }
}