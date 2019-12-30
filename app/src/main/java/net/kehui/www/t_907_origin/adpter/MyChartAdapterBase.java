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
    /**
     * //GC20181223
     */
    private boolean isCursorState;
    /**
     * //GC20181227
     */
    private int isMax;

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
            splitNum, boolean isShowSplitLine, int isMax) {
        this.mTempArray = mTempArray;
        this.mCompareArray = mCompareArray;
        this.isShowCompareLine = isShowCompareLine;
        this.splitNum = splitNum;
        this.isShowSplitLine = isShowSplitLine;
        //GC20181227
        this.isMax = isMax;
    }

    /**
     * @param cursorState   //GC20181223
     */
    public void setCursorState(boolean cursorState) {
        isCursorState = cursorState;
    }

    @Override
    public int getCount() {
        return 510;
    }   //GC20181227

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

    @Override
    public boolean getCursorState(){
        return isCursorState;
    }

    @Override
    public int getMax() {
        return isMax;
    }

}
