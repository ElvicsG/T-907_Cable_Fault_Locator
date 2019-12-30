package net.kehui.www.t_907_origin.adpter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.kehui.www.t_907_origin.R;

/**
 * @author li.md
 * @date 2019/7/9
 */
public class DataHolder extends RecyclerView.ViewHolder {
    public TextView date;
    public TextView time;
    public TextView mode;
    public TextView range;

    public DataHolder(@NonNull View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.tv_date);
        time = itemView.findViewById(R.id.tv_time);
        mode = itemView.findViewById(R.id.tv_mode);
        range = itemView.findViewById(R.id.tv_range);
    }
}