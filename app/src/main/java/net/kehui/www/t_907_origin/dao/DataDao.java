package net.kehui.www.t_907_origin.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import net.kehui.www.t_907_origin.entity.Data;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @author li.md
 * @date 2019/7/4
 */

@Dao
public interface DataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(Data... data);

    @Delete
    int deleteData(Data... data);

    @Update
    int updateData(Data... data);

    @Query("SELECT * FROM data")
    Data[] query();

    /**
     * //数据库相关 //20200520
     * @param index
     * @return
     */
    @Query("SELECT  dataid,unit,para,cableId,date,time,mode,range,line,phase,positionVirtual,positionReal,tester,location,testsite,'' as waveData,'' as waveDataSim  FROM data limit (:index*10),10")
    Data[] queryByIndex(int index);

    @Query("SELECT * FROM data WHERE dataId = :dataId")
    Data[] queryDataId(int dataId);

    @Query("SELECT * FROM data WHERE date LIKE :date")

    /**
     * //数据库相关 //20200520
     */
    Data[] queryDate(int date);
    @Query("SELECT   dataid,unit,para,cableId,date,time,mode,range,line,phase,positionVirtual,positionReal,tester,location,testsite,'' as waveData,'' as waveDataSim  FROM data WHERE date LIKE :date and mode LIKE :mode limit (:index),10")
    Data[] queryDateByIndex(String date, String mode, int index);

    @Query("SELECT   *  FROM data WHERE dataId=:id")
    Data[] queryWaveById(int id);   //添加

    @Query("SELECT * FROM data WHERE mode LIKE :mode")
    Data[] queryMode(String mode);

    /**
     * //数据库相关 //20200520
     * @param mode
     * @param index
     * @return
     */
    @Query("SELECT  dataid,unit,para,cableId,date,time,mode,range,line,phase,positionVirtual,positionReal,tester,location,testsite,'' as waveData,'' as waveDataSim   FROM data WHERE mode LIKE :mode limit (:index),10")
    Data[] queryModeByIndex(String mode, int index);

    @Query("SELECT * FROM data WHERE range LIKE :range")
    Data[] queryRange(String range);

    @Query("SELECT * FROM data WHERE line LIKE :line")
    Data[] queryLine(String line);

    @Query("SELECT * FROM data WHERE phase LIKE :phase")
    Data[] queryPhase(String phase);

    @Query("SELECT * FROM data WHERE tester LIKE :tester")
    Data[] queryTester(String tester);

    @Query("SELECT * FROM data WHERE location LIKE :location")
    Data[] queryLocation(String location);
}
