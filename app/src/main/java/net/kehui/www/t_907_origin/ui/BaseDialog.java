package net.kehui.www.t_907_origin.ui;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import net.kehui.www.t_907_origin.dao.DataDao;
import net.kehui.www.t_907_origin.global.BaseAppData;

/**
 * Create by jwj on 2019/12/3
 */
public class BaseDialog extends Dialog {

    public DataDao dao;

    public BaseDialog(@NonNull Context context) {
        super(context);
        //数据库相关 //20200520
        BaseAppData db = Room.databaseBuilder(getContext(), BaseAppData.class, "database-wave").allowMainThreadQueries().build();
        dao = db.dataDao();
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        BaseAppData db = Room.databaseBuilder(getContext(), BaseAppData.class, "database-wave").allowMainThreadQueries().build();
        dao = db.dataDao();
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        BaseAppData db = Room.databaseBuilder(getContext(), BaseAppData.class, "database-wave").allowMainThreadQueries().build();
        dao = db.dataDao();
    }
}
