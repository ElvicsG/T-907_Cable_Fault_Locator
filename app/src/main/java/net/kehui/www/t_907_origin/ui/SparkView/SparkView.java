/**
 * Copyright (C) 2016 Robinhood Markets, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.kehui.www.t_907_origin.ui.SparkView;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.ColorInt;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;

import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.ui.MoveWaveView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Gong
 * @date 2018/12/23
 */
public class SparkView extends View implements ScrubGestureDetector.ScrubListener, MoveWaveView.ViewMoveWaveListener {
    /**
     * styleable values
     */
    @ColorInt
    private int lineColor;
    private float lineWidth;
    private float cornerRadius;
    private boolean fill;
    @ColorInt
    private int baseLineColor;
    private float baseLineWidth;
    @ColorInt
    private int scrubLineColor;
    private float scrubLineWidth;
    private boolean scrubEnabled;
    private boolean animateChanges;
    private float moveY;


    /**
     * the onDraw data
     */
    private final Path renderPath = new Path();
    private final Path renderPath2 = new Path();
    private final Path sparkPath = new Path();
    private final Path sparkPath2 = new Path();
    private final Path baseLinePath = new Path();
    private final Path scrubLinePath = new Path();
    private final Path scrubLinePath2 = new Path();
    private final Path scrubLinePath3 = new Path();

    /**
     * adapter
     */
    private BaseSparkAdapter adapter;

    /**
     * misc fields
     */
    private ScaleHelper scaleHelper;
    private Paint sparkLinePaint;
    private Paint baseLinePaint;
    private Paint scrubLinePaint;
    private Paint scrubLinePaint2;
    private Paint scrubLinePaint3;

    private OnScrubListener scrubListener;

    private ScrubGestureDetector scrubGestureDetector;
    private List<Float> xPoints;
    private final RectF contentRect = new RectF();

    private float scX;
    /**
     * //GC20190629
     */
    private boolean startMove;

    public SparkView(Context context) {
        super(context);
        init(context, null, R.attr.spark_SparkViewStyle, R.style.spark_SparkView);
    }

    public SparkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.attr.spark_SparkViewStyle, R.style.spark_SparkView);
    }

    public SparkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, R.style.spark_SparkView);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        //GC20190629
        startMove = false;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.spark_SparkView, defStyleAttr, defStyleRes);
        lineColor = a.getColor(R.styleable.spark_SparkView_spark_lineColor, 0);
        lineWidth = a.getDimension(R.styleable.spark_SparkView_spark_lineWidth, 0);
        cornerRadius = a.getDimension(R.styleable.spark_SparkView_spark_cornerRadius, 0);
        fill = a.getBoolean(R.styleable.spark_SparkView_spark_fill, false);
        baseLineColor = a.getColor(R.styleable.spark_SparkView_spark_baseLineColor, 0);
        baseLineWidth = a.getDimension(R.styleable.spark_SparkView_spark_baseLineWidth, 0);
        scrubEnabled = a.getBoolean(R.styleable.spark_SparkView_spark_scrubEnabled, true);
        scrubLineColor = a.getColor(R.styleable.spark_SparkView_spark_scrubLineColor, baseLineColor);
        scrubLineWidth = a.getDimension(R.styleable.spark_SparkView_spark_scrubLineWidth, lineWidth);
        animateChanges = a.getBoolean(R.styleable.spark_SparkView_spark_animateChanges, false);
        a.recycle();

        sparkLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sparkLinePaint.setColor(lineColor);
        sparkLinePaint.setStrokeWidth(lineWidth);
        sparkLinePaint.setStyle(fill ? Paint.Style.FILL : Paint.Style.STROKE);
        sparkLinePaint.setStrokeCap(Paint.Cap.ROUND);
        if (cornerRadius != 0) {
            sparkLinePaint.setPathEffect(new CornerPathEffect(cornerRadius));
        }

        //初始化波形的颜色、样式
        baseLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        baseLinePaint.setStyle(Paint.Style.STROKE);
//        baseLinePaint.setColor(Color.WHITE);  //20200521    波形颜色
        baseLinePaint.setColor(Color.parseColor("#b0a04b"));
        baseLinePaint.setStrokeWidth(lineWidth);
        //实光标的颜色、样式
        scrubLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scrubLinePaint.setStyle(Paint.Style.STROKE);
        scrubLinePaint.setStrokeWidth(scrubLineWidth);
        scrubLinePaint.setColor(Color.RED);
        scrubLinePaint.setStrokeCap(Paint.Cap.ROUND);
        //虚光标的颜色、样式
        scrubLinePaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        scrubLinePaint2.setStyle(Paint.Style.STROKE);
        scrubLinePaint2.setStrokeWidth(scrubLineWidth);
        scrubLinePaint2.setColor(Color.parseColor("#ff00ff"));
        scrubLinePaint2.setAntiAlias(true);
        scrubLinePaint2.setStrokeCap(Paint.Cap.SQUARE);
        scrubLinePaint2.setPathEffect(new DashPathEffect(new float[]{6, 10}, 0));
        //初始化SIM标记光标的样式颜色  //GC20200330
        scrubLinePaint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        scrubLinePaint3.setStyle(Paint.Style.STROKE);
        scrubLinePaint3.setStrokeWidth(scrubLineWidth);
        scrubLinePaint3.setColor(Color.parseColor("#339933"));
        scrubLinePaint3.setStrokeCap(Paint.Cap.ROUND);

        final Handler handler = new Handler();
        final float touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        scrubGestureDetector = new ScrubGestureDetector(this, handler, touchSlop);
        scrubGestureDetector.setEnabled(scrubEnabled);
        setOnTouchListener(scrubGestureDetector);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        updateContentRect();
        populatePath();

    }

    /**
     * Populates the {@linkplain #sparkPath} with points
     */
    private void populatePath() {
        if (adapter == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        final int adapterCount = adapter.getCount();
        // to draw anything, we need 2 or more points
        if (adapterCount < 2) {
            clearData();
            return;
        }

        scaleHelper = new ScaleHelper(adapter, contentRect, lineWidth, fill);
        // xPoints is only used in scrubbing, skip if disabled
        if (xPoints == null) {
            xPoints = new ArrayList<>(adapterCount);
        } else {
            xPoints.clear();
        }
        // make our main graph path
        sparkPath.reset();
        for (int i = 0; i < adapterCount; i++) {
            final float x = scaleHelper.getX(adapter.getX(i));
            final float y = scaleHelper.getY(adapter.getY(i));
            if (i == 0) {
                sparkPath.moveTo(x, y);
            } else {
                sparkPath.lineTo(x, y);
            }
            xPoints.add(x);
        }

        // if we're filling the graph in, close the path's circuit
        if (fill) {
            float lastX = scaleHelper.getX(adapter.getCount() - 1);
            float bottom = getHeight() - getPaddingBottom();
            // line straight down to the bottom of the view
            sparkPath.lineTo(lastX, bottom);
            // line straight left to far edge of the view
            sparkPath.lineTo(getPaddingStart(), bottom);
            // line straight up to meet the first point
            sparkPath.close();
        }

        //画第二条线
        Log.e("画第二条线", "adapter.getCompare() = " + adapter.getCompare());
        if (adapter.getCompare()) {
            sparkPath2.reset();
            for (int i = 0; i < adapterCount; i++) {
                final float x = scaleHelper.getX(adapter.getX(i));
                final float y = scaleHelper.getY(adapter.getY1(i)) + moveY;
                if (i == 0) {
                    sparkPath2.moveTo(x, y);
                } else {
                    sparkPath2.lineTo(x, y);
                }
                xPoints.add(x);
            }
            // if we're filling the graph in, close the path's circuit
            if (fill) {
                float lastX = scaleHelper.getX(adapter.getCount() - 1);
                float bottom = getHeight() - getPaddingBottom();
                // line straight down to the bottom of the view
                sparkPath2.lineTo(lastX, bottom);
                // line straight left to far edge of the view
                sparkPath2.lineTo(getPaddingStart(), bottom);
                // line straight up to meet the first point
                sparkPath2.close();
            }
            renderPath2.reset();
            renderPath2.addPath(sparkPath2);
        } else {
            renderPath2.reset();
        }

        // make our base line path
        baseLinePath.reset();
        if (adapter.hasBaseLine()) {
            float scaledBaseLine = scaleHelper.getY(adapter.getBaseLine());
            baseLinePath.moveTo(0, scaledBaseLine);
            baseLinePath.lineTo(getWidth(), scaledBaseLine);
        }

        renderPath.reset();
        renderPath.addPath(sparkPath);

        invalidate();

    }

    /**
     * @param x 实光标位置（监听触摸位置）
     */
    public void setScrubLineRealMove(float x) {
        scrubLinePath.reset();
        scrubLinePath.moveTo(x, getPaddingTop());
        scrubLinePath.lineTo(x, getHeight() - getPaddingBottom());
        invalidate();
    }

    /**
     * @param x 虚光标位置（监听触摸位置）
     */
    public void setScrubLineVirtualMove(float x) {
        scrubLinePath2.reset();
        scrubLinePath2.moveTo(x, getPaddingTop());
        scrubLinePath2.lineTo(x, getHeight() - getPaddingBottom());
        invalidate();
    }

    int currentRealPosition;
    /**
     * @param position 实光标位置（设置固定值）
     */
    public void setScrubLineReal(int position) {
        try {
            currentRealPosition = position;
            if (xPoints != null) {
                //GC2019629
                startMove = true;
                scrubLinePath.reset();
                scrubLinePath.moveTo(xPoints.get(position), getPaddingTop());
                scrubLinePath.lineTo(xPoints.get(position), getHeight() - getPaddingBottom());
                invalidate();
            }
        } catch (Exception l_ex) {
            //20200521  //GC?不知道为啥加
            String str = l_ex.getMessage().toString();
            scrubLinePath.reset();
            setScrubLineRealDisappear();
        }

    }

    /**
     * 去掉实光标
     */
    public void setScrubLineRealDisappear() {
        scrubLinePath.reset();
        invalidate();
    }

    /**
     * @param position 虚光标位置（设置固定值）
     */
    public void setScrubLineVirtual(int position) {
        try {
            if (xPoints != null) {
                //GC20190629
                startMove = true;
                scrubLinePath2.reset();
                scrubLinePath2.moveTo(xPoints.get(position), getPaddingTop());
                scrubLinePath2.lineTo(xPoints.get(position), getHeight() - getPaddingBottom());
                invalidate();
            }
        } catch (Exception l_Ex) {
        }
    }

    /**
     * 去掉虚光标
     */
    public void setScrubLineVirtualDisappear() {
        scrubLinePath2.reset();
        invalidate();
    }

    /**
     * 绘制SIM标记光标  //GC20200330
     */
    public void setScrubLineSim(int position) {
        try {
            if (xPoints != null) {
                startMove = true;
                scrubLinePath3.reset();
                scrubLinePath3.moveTo(xPoints.get(position), getPaddingTop());
                scrubLinePath3.lineTo(xPoints.get(position), getHeight() - getPaddingBottom());
                invalidate();
            }
        } catch (Exception l_Ex) {
        }
    }

    /**
     * 去掉SIM标记光标
     */
    public void setScrubLineSimDisappear() {
        scrubLinePath3.reset();
        invalidate();
    }

    public void setSparkViewMove(float y) {
        moveY = y;
        populatePath();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        updateContentRect();
        populatePath();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(baseLinePath, baseLinePaint);
        canvas.drawPath(renderPath, sparkLinePaint);
        canvas.drawPath(renderPath2, baseLinePaint);
        canvas.drawPath(scrubLinePath, scrubLinePaint);
        canvas.drawPath(scrubLinePath2, scrubLinePaint2);
        //初始化   //GC20200330
        canvas.drawPath(scrubLinePath3, scrubLinePaint3);

        //TODO 20191224  屏蔽掉不知道干啥的
        if (!startMove) {
            //setScrubLineReal(0);
            setScrubLineReal(currentRealPosition);
            setScrubLineVirtual(255);
        }

    }

    /**
     * Set a {@link com.robinhood.spark.SparkView.OnScrubListener} to be notified of the user's
     * scrubbing gestures.
     */
    public void setScrubListener(OnScrubListener scrubListener) {
        this.scrubListener = scrubListener;
    }

    /**
     * Sets the backing {@link BaseSparkAdapter} to generate the points to be graphed
     */
    public void setAdapter(BaseSparkAdapter adapter) {
        if (this.adapter != null) {
            this.adapter.unregisterDataSetObserver(dataSetObserver);
        }
        this.adapter = adapter;
        if (this.adapter != null) {
            this.adapter.registerDataSetObserver(dataSetObserver);
        }
        populatePath();
    }

    private void clearData() {
        scaleHelper = null;
        renderPath.reset();
        sparkPath.reset();
        baseLinePath.reset();
        invalidate();
    }


    /**
     * Helper class for handling scaling logic.
     */
    public static class ScaleHelper {
        // the width and height of the view
        final float width, height;
        final int size;
        // the scale factor for the Y values
        final float xScale, yScale;
        // translates the Y values back into the bounding rect after being scaled
        final float xTranslation, yTranslation;

        public float getYScale() {
            return yScale;
        }

        ScaleHelper(BaseSparkAdapter adapter, RectF contentRect, float lineWidth, boolean fill) {
            final float leftPadding = contentRect.left;
            final float topPadding = contentRect.top;

            // subtract lineWidth to offset for 1/2 of the line bleeding out of the content box on
            // either side of the view
            final float lineWidthOffset = fill ? 0 : lineWidth;
            this.width = contentRect.width() - lineWidthOffset;
            this.height = contentRect.height() - lineWidthOffset;
            this.size = adapter.getCount();

            // get data bounds from adapter
            RectF bounds = adapter.getDataBounds();

            // if data is a line (which technically has no size), expand bounds to center the data
            bounds.inset(bounds.width() == 0 ? -1 : 0, bounds.height() == 0 ? -1 : 0);

            final float minX = bounds.left;
            final float maxX = bounds.right;
            final float minY = bounds.top;
            final float maxY = bounds.bottom;

            // xScale will compress or expand the min and dataMax x values to be just inside the view
            this.xScale = width / (maxX - minX);
            // xTranslation will move the x points back between 0 - width
            this.xTranslation = leftPadding - (minX * xScale) + (lineWidthOffset / 2);
            // yScale will compress or expand the min and dataMax y values to be just inside the view
            this.yScale = height / (maxY - minY);
            // yTranslation will move the y points back between 0 - height
            this.yTranslation = minY * yScale + topPadding + (lineWidthOffset / 2);
        }

        /**
         * Given the 'raw' X value, scale it to fit within our view.
         */
        float getX(float rawX) {
            return rawX * xScale + xTranslation;
        }

        /**
         * Given the 'raw' Y value, scale it to fit within our view. This mode also 'flips' the
         * value to be ready for drawing.
         */
        float getY(float rawY) {
            return height - (rawY * yScale) + yTranslation;
        }
    }

    @Override
    public int getPaddingStart() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1
                ? super.getPaddingStart()
                : getPaddingLeft();
    }

    @Override
    public int getPaddingEnd() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1
                ? super.getPaddingEnd()
                : getPaddingRight();
    }

    /**
     * Gets the rect representing the 'content area' of the view. This is essentially the bounding
     * rect minus any padding.
     */
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
    }

    /**
     * returns the nearest index (into {@link #adapter}'s data) for the given x coordinate.
     */
    static int getNearestIndex(List<Float> points, float x) {
        int index = Collections.binarySearch(points, x);

        // if binary search returns positive, we had an exact match, return that index
        if (index >= 0) {
            return index;
        }

        // otherwise, calculate the binary search's specified insertion index
        index = -1 - index;

        // if we're inserting at 0, then our guaranteed nearest index is 0
        if (index == 0) {
            return index;
        }

        // if we're inserting at the very end, then our guaranteed nearest index is the final one
        if (index == points.size()) {
            return --index;
        }

        // otherwise we need to check which of our two neighbors we're closer to
        final float deltaUp = points.get(index) - x;
        final float deltaDown = x - points.get(index - 1);
        if (deltaUp > deltaDown) {
            // if the below neighbor is closer, decrement our index
            index--;
        }

        return index;
    }

    @Override
    public void onActionDown(float x, float y) {
        if (adapter == null || adapter.getCount() == 0) {
            return;
        }
        if (scrubListener != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
            int index = getNearestIndex(xPoints, x);
            if (scrubListener != null) {
                scrubListener.onActionDown(adapter.getItem(index), y);
            }
        }
    }

    @Override
    public void onScrubbed(float x, float y) {
        //GC2019629
        startMove = true;
        scX = x;
        if (adapter == null || adapter.getCount() == 0) {
            return;
        }
        int index = 0;
        if (scrubListener != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
            index = getNearestIndex(xPoints, x);
            if (scrubListener != null) {
                scrubListener.onScrubbed(adapter.getItem(index), y);
            }
        }
        //GC20190628 光标位置限制，触摸有效范围
        if (x >= 8.9929 & x <= 1111.1304 & index < 509) {
            if (y < 400) {
                //新加纵坐标范围响应     //20200521
//                setScrubLineVirtualMove(x);
            }
        }
        Log.i("position", String.valueOf(x));
        Log.e("【SparkView内部】", "X:" + x + "/Y:" + y);
    }


    @Override
    public void onScrubEnded() {
        scrubLinePath.reset();
        if (scrubListener != null) {
            scrubListener.onScrubbed(null, 0);
        }
        invalidate();
    }

    /**
     * Listener for a user scrubbing (dragging their finger along) the graph.
     */
    public interface OnScrubListener {
        /**
         * Indicates the user is currently scrubbing over the given value. A null value indicates
         * that the user has stopped scrubbing.
         *
         * @param value //触摸位置
         */
        void onScrubbed(Object value, float y);

        void onActionDown(Object x, float y);

    }


    private final DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            populatePath();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            clearData();
        }
    };

    @Override
    public void onMoved(float x, float y) {

    }

    @Override
    public void onMoveEnded() {

    }

}
