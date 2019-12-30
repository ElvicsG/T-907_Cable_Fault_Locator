package net.kehui.www.t_907_origin.adpter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.kehui.www.t_907_origin.R;

/**
 * @author li.md
 * @date 2019/7/9
 */
public class RecordsHolder extends RecyclerView.ViewHolder {
    public TextView tvRecord;
    public ImageView ivRecord;

    public RecordsHolder(@NonNull View itemView) {
        super(itemView);
        tvRecord = itemView.findViewById(R.id.tv_records);
        ivRecord = itemView.findViewById(R.id.iv_records);
    }
}