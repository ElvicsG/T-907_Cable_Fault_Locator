package net.kehui.www.t_907_origin.adpter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.base.BaseActivity;
import net.kehui.www.t_907_origin.entity.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author li.md
 * @date 2019/7/9
 */
public class RecordsAdapter extends RecyclerView.Adapter {

    private OnItemClickListener onItemClickListener;
    private OnItemInitDataListener onItemInitDataListener;

    public List<Data> datas = new ArrayList<>();
    private int selected = 0;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        RecordsHolder holder = new RecordsHolder(View.inflate(viewGroup.getContext(), R.layout.item_record
                , null));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        RecordsHolder holder = (RecordsHolder) viewHolder;
        Data data = datas.get(position);
        //记录显示中英文切换      //GC20200525
//        holder.tvRecord.setText("Record" + (position + 1));
        holder.tvRecord.setText(BaseActivity.mContext.getResources().getString(R.string.record) + (position + 1));
        int[] selectedPara = data.para;
        int[] selectedWave = data.waveData;
        int[] selectedSim = data.waveDataSim;
        int selectedId = data.dataId;
        if (selected == position) {
            ((RecordsHolder) viewHolder).ivRecord.setImageResource(R.drawable.bg_btn);
//            if (selectedId == 0)
//                if (onItemInitDataListener != null) {
//                    int pos = holder.getLayoutPosition();
//                    onItemInitDataListener.onItemInitData(holder.itemView, selectedId, selectedPara,
//                            selectedWave,
//                            selectedSim, pos);
//                }
        } else {
            ((RecordsHolder) viewHolder).ivRecord.setImageResource(R.drawable.bg_btn_normal);
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
        notifyItemRangeChanged(selected, getItemCount());
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
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemInitDataListener(OnItemInitDataListener listener) {
        this.onItemInitDataListener = listener;
    }

    public interface OnItemClickListener {
        /**
         * @param view
         * @param position
         */
        void onItemClick(View view, int dataId, int[] para, int[] waveData, int[] simData, int position);

    }


    public interface OnItemInitDataListener {
        /**
         * @param view
         * @param position
         */
        void onItemInitData(View view, int dataId, int[] para, int[] waveData, int[] simData, int position);

    }

}
