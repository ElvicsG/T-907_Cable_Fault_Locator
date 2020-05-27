package net.kehui.www.t_907_origin.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import net.kehui.www.t_907_origin.util.DataConverters;

import java.util.Arrays;
import java.util.Objects;

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
     * 波形数据（waveData TDR/ICM/SIM第一条波形  waveDataSim SIM第二条波形数据）——去掉数据头
     */
    public int[] waveData;
    public int[] waveDataSim;

    /**
     * //数据库相关 //20200520
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return dataId == data.dataId &&
                unit == data.unit &&
                range == data.range &&
                positionVirtual == data.positionVirtual &&
                positionReal == data.positionReal &&
                Arrays.equals(para, data.para) &&
                Objects.equals(cableId, data.cableId) &&
                Objects.equals(date, data.date) &&
                Objects.equals(time, data.time) &&
                Objects.equals(mode, data.mode) &&
                Objects.equals(line, data.line) &&
                Objects.equals(phase, data.phase) &&
                Objects.equals(tester, data.tester) &&
                Objects.equals(location, data.location) &&
                Objects.equals(testsite, data.testsite) &&
                Arrays.equals(waveData, data.waveData) &&
                Arrays.equals(waveDataSim, data.waveDataSim);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(dataId, unit, cableId, date, time, mode, range, line, phase, positionVirtual, positionReal, tester, location, testsite);
        result = 31 * result + Arrays.hashCode(para);
        result = 31 * result + Arrays.hashCode(waveData);
        result = 31 * result + Arrays.hashCode(waveDataSim);
        return result;
    }
}
