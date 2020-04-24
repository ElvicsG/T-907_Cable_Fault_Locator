package net.kehui.www.t_907_origin.adpter;

import net.kehui.www.t_907_origin.ui.SparkView.BaseSparkAdapter;

/**
 * @author Gong
 * @date 2018/12/23
 */
public class MyChartAdapterBase extends BaseSparkAdapter {

    private int[] mTempArray;
    private int[] mCompareArray;
    private boolean isShowCompareLine;
    private int splitNum;
    private boolean isShowSplitLine;

    public void setmCompareArray(int[] mCompareArray) {
        this.mCompareArray = mCompareArray;
    }

    public void setmTempArray(int[] mTempArray) {
        this.mTempArray = mTempArray;
    }

    public void setShowCompareLine(boolean showCompareLine) {
        isShowCompareLine = showCompareLine;
    }

    public MyChartAdapterBase(int[] mTempArray, int[] mCompareArray, boolean isShowCompareLine, int
            splitNum, boolean isShowSplitLine) {
        this.mTempArray = mTempArray;
        this.mCompareArray = mCompareArray;
        this.isShowCompareLine = isShowCompareLine;
        this.splitNum = splitNum;
        this.isShowSplitLine = isShowSplitLine;
    }

    @Override
    public int getCount() {
        return 510;
    }

    @Override
    public Object getItem(int index) {
        return index;
    }

    @Override
    public float getX(int index) {
        return super.getX(index);
    }

    @Override
    public float getY(int index) {
        return mTempArray[index];
    }

    @Override
    public float getY1(int index) {
        return mCompareArray[index];
    }

    @Override
    public boolean getCompare() {
        return isShowCompareLine;
    }

}
