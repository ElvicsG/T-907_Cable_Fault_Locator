package net.kehui.www.t_907_origin.ui;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * Create by jwj on 2019/12/23 09:44
 */
public class MoveView extends View {

    private int lastX;
    private int lastY;
    //代表垂直滑动水平滑动
    private int orientationMoveWaveView;
    //所在父View的
    private int parentWidth;
    private int parentHeight;
    private ViewGroup parentView;
    private ViewMoveWaveListener viewMoveWaveListener; //滑块滑动监听
    private ViewMoveSizeChangedListener viewMoveSizeChangedListener; //滑块滑动监听

    public void setViewMoveWaveListener(ViewMoveWaveListener viewMoveWaveListener) {
        this.viewMoveWaveListener = viewMoveWaveListener;
    }

    public void setViewMoveSizeChangedListener(ViewMoveSizeChangedListener viewMoveSizeChangedListener) {
        this.viewMoveSizeChangedListener = viewMoveSizeChangedListener;
    }

    public void setParentView(ViewGroup parentView) {
        this.parentView = parentView;
    }

    //初始化滑块位置
    public void setMoveViewHorizatolMove(float x) {
        lastY = (int) x;
        invalidate();
    }

    public void setParentWidth(int parentWidth) {
        this.parentWidth = parentWidth;
    }

    public void setParentHeight(int parentHeight) {
        this.parentHeight = parentHeight;
    }

    public void setOrientationMoveWaveView(int orientationMoveWaveView) {
        this.orientationMoveWaveView = orientationMoveWaveView;
    }

    public MoveView(Context context) {
        super(context);
    }

    public MoveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private final RectF contentRect = new RectF();//滑动矩形区域

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        parentWidth = parentView.getWidth();
        parentHeight = parentView.getHeight();
        contentRect.set(
                getPaddingStart(),
                getPaddingTop(),
                getWidth() - getPaddingEnd(),
                getHeight() - getPaddingBottom()
        );
        if (viewMoveSizeChangedListener != null)
            viewMoveSizeChangedListener.onSizeChanged();

        super.onSizeChanged(w, h, oldw, oldh);
    }

    public RectF getContentRect() {
        return contentRect;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastX;//计算滑动的距离
                int offsetY = y - lastY;
                int offesetPositionX = getLeft() + offsetX;
                Log.e("ACTION_MOVE", "getLeft():" + getLeft() + "/offsetX:" + offsetX + "offesetPositionX" + offesetPositionX);

                int offesetPositionY = getTop() + offsetY;
                Log.e("ACTION_MOVE", "getTop():" + getTop() + "/offsetY:" + offsetY + "offesetPositionY" + offesetPositionY);

                /*//重新放置新的位置
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
                if (orientationMoveWaveView == 0) {
                    if (offesetPositionX > 0 && offesetPositionX <= parentWidth - getWidth()) {
                        layoutParams.leftMargin = getLeft() + offsetX;
                        layoutParams.topMargin = getTop();
                        viewMoveWaveListener.onMoved(offesetPositionX);
                        //Log.e("ACTION_MOVE", "offesetPositionX===" + offesetPositionX);

                    }
                } else {
                    if (offesetPositionY > 0 && offesetPositionX <= parentHeight - getHeight()) {
                        layoutParams.leftMargin = getLeft();
                        layoutParams.topMargin = offesetPositionY;
                        viewMoveWaveListener.onMoved(offesetPositionY);
                        //Log.e("ACTION_MOVE", "offesetPositionY===" + offesetPositionY);

                    }
                }*/
                int maxLeft = parentWidth - getWidth();
//重新放置新的位置
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
                if (orientationMoveWaveView == 0) {
                    if (offesetPositionX > 0 && offesetPositionX <= maxLeft) {
                        layoutParams.leftMargin = offesetPositionX;
                        //改变位置
                        layoutParams.leftMargin = offesetPositionX;
                        layoutParams.topMargin = getTop();
                        setLayoutParams(layoutParams);
                        viewMoveWaveListener.onMoved(offesetPositionX);
                    } else if (offesetPositionX > maxLeft && getLeft() != maxLeft) {
                        offesetPositionX = maxLeft;
                        //改变位置
                        layoutParams.leftMargin = offesetPositionX;
                        layoutParams.topMargin = getTop();
                        setLayoutParams(layoutParams);
                        viewMoveWaveListener.onMoved(offesetPositionX);
                    } else if (offesetPositionX < 0 && getLeft() != 0) {
                        offesetPositionX = 0;
                        //改变位置
                        layoutParams.leftMargin = offesetPositionX;
                        layoutParams.topMargin = getTop();
                        setLayoutParams(layoutParams);
                        viewMoveWaveListener.onMoved(offesetPositionX);
                    }

                }
                setLayoutParams(layoutParams);
                //Log.e("ACTION_MOVE", "offset===" + (getLeft() + offsetX));
        }
        return true;
    }

    public int getParentWidth() {
        return parentWidth;
    }

    public interface ViewMoveWaveListener {
        void onMoved(float x);

    }

    public interface ViewMoveSizeChangedListener {
        void onSizeChanged();

    }
}
