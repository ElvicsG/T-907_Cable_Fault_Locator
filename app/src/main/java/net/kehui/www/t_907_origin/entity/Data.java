package net.kehui.www.t_907_origin.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import net.kehui.www.t_907_origin.util.DataConverters;


/**
 * @author li.md
 * @date 2019/7/4
 */
@TypeConverters(DataConverters.class)
@Entity(tableName = "data", indices = {@Index({"date", "mode", "range", "line", "phase", "tester", "location"})})
public class Data {
    @PrimaryKey(autoGenerate = true)
    public int dataId;

    public int unit;

    /**
     * 波形参数
     */
    public int[] para = new int[4];
    @ColumnInfo(name = "cableId")
    public String cableId;
    /**
     * 波形信息
     */
    @ColumnInfo(name = "date")
    public String date;
    @ColumnInfo(name = "time")
    public String time;
    public String mode;
    public int range;

    @ColumnInfo(name = "line")
    public String line;

    public String phase;
    public int positionVirtual;
    public int positionReal;

    @ColumnInfo(name = "tester")
    public String tester;

    //故障位置
    @ColumnInfo(name = "location")
    public Double location;

    @ColumnInfo(name = "testsite")
    public String testsite;

    /**
     * 波形数据
     */
    public int[] waveData;
    public int[] waveDataSim;
}
