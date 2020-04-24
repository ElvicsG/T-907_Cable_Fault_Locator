package net.kehui.www.t_907_origin.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.adpter.DataAdapter;
import net.kehui.www.t_907_origin.application.Constant;
import net.kehui.www.t_907_origin.base.BaseActivity;
import net.kehui.www.t_907_origin.entity.Data;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ListActivity extends BaseActivity {

    @BindView(R.id.btn_Dele)
    Button       btnDele;
    @BindView(R.id.btn_Disp)
    Button       btnDisp;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    int pos;

    private RecyclerView.LayoutManager layoutManager;
    /**
     * //GC20190713
     */
    public static final String DISPLAY_ACTION = "display_action";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        //添加点击侧边消失
        setFinishOnTouchOutside(true);
        initAdapter();
    }

    private void initAdapter() {
        adapter = new DataAdapter();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemClickListener((view, dataId,selectedPara[], selectedWave[],selectedSim[],position) -> {
            adapter.changeSelected(position);
            selectedId = dataId;
            //GC20190713
            Constant.Para = selectedPara;
            Constant.WaveData = selectedWave;
            Constant.SimData = selectedSim;
            pos = position;
        });

        Flowable.create((FlowableOnSubscribe<List>) e -> {
            Data[] data;
            data = dao.query();
            e.onNext(Arrays.asList(data));
            e.onComplete();
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
                //subscription = s;
            }

            @Override
            public void onNext(List list) {
                adapter.datas = list;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onComplete() {
            }
        });
    }


    @OnClick({R.id.btn_Dele, R.id.btn_Disp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_Dele:
                Flowable.create((FlowableOnSubscribe<List>) e -> {
                    Data[] datas = null;
                    datas = dao.queryDataId(selectedId);
                    dao.deleteData(datas);
                    e.onNext(Arrays.asList(dao.query()));
                    e.onComplete();
                }, BackpressureStrategy.BUFFER)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                        //subscription = s;
                    }

                    @Override
                    public void onNext(List list) {
                        adapter.datas = list;
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
                adapter.deleteItem(pos);
                layoutManager.scrollToPosition(0);
                break;
            case R.id.btn_Disp:
                //GC20190713
                Intent intent = new Intent(DISPLAY_ACTION);
                intent.putExtra("display_action",8);
                sendBroadcast(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
