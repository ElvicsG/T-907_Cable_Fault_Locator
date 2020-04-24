package net.kehui.www.t_907_origin.adpter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.entity.Data;

import java.util.List;

/**
 * @author li.md
 * @date 2019/7/9
 */
public class DataAdapter extends RecyclerView.Adapter {

    private OnItemClickListener onItemClickListener;

    public  List<Data> datas;
    private int        selected = 0;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        DataHolder holder = new DataHolder(View.inflate(viewGroup.getContext(), R.layout.item_view
                , null));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        DataHolder holder = (DataHolder) viewHolder;
        Data data = datas.get(position);
        holder.date.setText(data.date);
        holder.time.setText(data.time);
        holder.mode.setText(data.mode);
        holder.range.setText(data.range);
        int[] selectedPara = data.para;
        int[] selectedWave = data.waveData;
        int[] selectedSim = data.waveDataSim;
        int selectedId = data.dataId;
        if (selected == position) {
            viewHolder.itemView.setBackgroundResource(R.color.T_99);
        } else {
            viewHolder.itemView.setBackgroundResource(R.drawable.selector);
        }
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                int pos = holder.getLayoutPosition();
                onItemClickListener.onItemClick(holder.itemView, selectedId, selectedPara,
                        selectedWave,
                        selectedSim, pos);
            }
        });
    }

    /**
     * 删除Item
     */
    public void deleteItem(int pos) {
        if (datas == null || datas.isEmpty()) {
            return;
        }
        notifyItemRemoved(selected);
    }

    /**
     * 刷新点击位置
     *
     * @param position 手指位置
     */
    public void changeSelected(int position) {
        if (position != selected) {
            selected = position;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        /**
         * @param view
         * @param position
         */
        void onItemClick(View view, int dataId, int[] para, int[] waveData, int[] simData,
                         int position);
    }
}
