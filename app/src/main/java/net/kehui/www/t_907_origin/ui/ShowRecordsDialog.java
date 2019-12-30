package net.kehui.www.t_907_origin.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.percentlayout.widget.PercentRelativeLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.adpter.RecordsAdapter;
import net.kehui.www.t_907_origin.application.Constant;
import net.kehui.www.t_907_origin.entity.Data;
import net.kehui.www.t_907_origin.util.ScreenUtils;
import net.kehui.www.t_907_origin.util.UnitUtils;
import net.kehui.www.t_907_origin.view.ModeActivity;

import org.checkerframework.checker.units.qual.MixedUnits;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static net.kehui.www.t_907_origin.base.BaseActivity.DECAY;
import static net.kehui.www.t_907_origin.base.BaseActivity.ICM;
import static net.kehui.www.t_907_origin.base.BaseActivity.ICM_DECAY;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_16_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_1_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_250;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_2_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_32_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_4_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_500;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_64_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_8_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.SIM;
import static net.kehui.www.t_907_origin.base.BaseActivity.TDR;
import static net.kehui.www.t_907_origin.view.ListActivity.DISPLAY_ACTION;


/**
 * Create by jwj on 2019/11/26
 */
public class ShowRecordsDialog extends BaseDialog implements View.OnClickListener {
    /**
     * 数据库存储波形部分
     */
    public RecordsAdapter adapter;
    public int selectedId;
    int pos;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    RecyclerView rvRecords;
    TextView tvCableIdText;
    TextView tvCableId;
    TextView tvDateText;
    TextView tvDate;
    TextView tvModeText;
    TextView tvMode;
    TextView tvRangeText;
    TextView tvRange;
    TextView tvCableLengthText;
    TextView tvCableLength;
    TextView tvFaultLocationText;
    TextView tvFaultLocation;
    TextView tvPhaseText;
    TextView tvPhase;
    TextView tvOperatorText;
    TextView tvOperator;
    TextView tvTestSiteText;
    TextView tvTestSite;
    TextView tvDisplay;
    TextView tvDelete;
    RelativeLayout rlInfo;


    private RecyclerView.LayoutManager layoutManager;
    private View view;
    private Unbinder unbinder;
    private TextView tvNoRecords;
    private PercentRelativeLayout rlHasRecords;
    private int mode;
    private boolean fromMain;
    private TextView tvCableLengthUnit;

    public void setFromMain(boolean fromMain) {
        this.fromMain = fromMain;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public ShowRecordsDialog(@NonNull Context context) {
        super(context);
    }

    public ShowRecordsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ShowRecordsDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(getContext(), R.layout.layout_show_records_dialog, null);

        setContentView(view);
        initView();

        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) ( ScreenUtils.getScreenWidth(getContext()) * 0.9);
        params.height = (int) (ScreenUtils.getScreenHeight(getContext()) * 0.7);
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initAdapter();

    }

    private void initView() {
        ivClose = view.findViewById(R.id.iv_close);
        rvRecords = view.findViewById(R.id.rv_records);
        tvCableId = view.findViewById(R.id.tv_cable_id);
        tvDate = view.findViewById(R.id.tv_date);
        tvMode = view.findViewById(R.id.tv_mode);
        tvRange = view.findViewById(R.id.tv_range);
        tvCableLength = view.findViewById(R.id.tv_cable_length);
        tvFaultLocation = view.findViewById(R.id.tv_fault_location);
        tvPhase = view.findViewById(R.id.tv_phase);
        tvOperator = view.findViewById(R.id.tv_operator);
        tvTestSite = view.findViewById(R.id.tv_test_site);
        tvDisplay = view.findViewById(R.id.tv_display);
        tvDelete = view.findViewById(R.id.tv_delete);
        tvNoRecords = view.findViewById(R.id.tv_no_records);
        rlHasRecords = view.findViewById(R.id.rl_has_records);
        tvCableLengthUnit = view.findViewById(R.id.tv_cable_length_unit);
        ivClose.setOnClickListener(this);
        tvDisplay.setOnClickListener(this);
        tvDelete.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_display:
                Intent intent = new Intent();
                if (fromMain) {
                    intent.setClass(getContext(), ModeActivity.class);
                    intent.putExtra(ModeActivity.MODE_KEY, Integer.valueOf(adapter.datas.get(pos).mode));
                    intent.putExtra("display_action", ModeActivity.DISPLAY_DATABASE);
                    intent.putExtra("isReceiveData", false);
                    getContext().startActivity(intent);
                } else {
                    intent = new Intent(DISPLAY_ACTION);
                    intent.putExtra(ModeActivity.MODE_KEY, Integer.valueOf(adapter.datas.get(pos).mode));
                    intent.putExtra("display_action", ModeActivity.DISPLAY_DATABASE);
                    getContext().sendBroadcast(intent);
                }

                dismiss();
                break;
            case R.id.tv_delete:
                deletePosition();
                break;

        }
    }


    private void deletePosition() {
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
        dismiss();
    }

    private void initAdapter() {
        adapter = new RecordsAdapter();
        layoutManager = new LinearLayoutManager(getContext());
        rvRecords.setLayoutManager(layoutManager);
        rvRecords.setAdapter(adapter);
        rvRecords.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemClickListener((view, dataId, selectedPara[], selectedWave[], selectedSim[], position) -> {
            adapter.changeSelected(position);
            selectedId = dataId;
            //GC20190713
            Constant.Para = selectedPara;
            Constant.WaveData = selectedWave;
            Constant.SimData = selectedSim;
            Constant.PositionR = adapter.datas.get(position).positionReal;
            Constant.PositonV = adapter.datas.get(position).positionVirtual;
            Constant.SaveLocation = adapter.datas.get(position).location;
            pos = position;
            setDataByPosition(adapter.datas.get(position));
        });
        adapter.setOnItemInitDataListener((view, dataId, para, waveData, simData, position) -> {
            Data data = adapter.datas.get(position);
            setDataByPosition(data);
        });

        Flowable.create((FlowableOnSubscribe<List<Data>>) e -> {
            Data[] data;
            if (mode == 0) {
                data = dao.query();
            } else {
                data = dao.queryMode(mode + "");
            }

            e.onNext(Arrays.asList(data));
            e.onComplete();
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Data>>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
                //subscription = s;
            }

            @Override
            public void onNext(List<Data> list) {
                if (list.size() > 0) {
                    rlHasRecords.setVisibility(View.VISIBLE);
                    tvNoRecords.setVisibility(View.GONE);
                    adapter.datas = list;
                    adapter.notifyDataSetChanged();
                    setDataByPosition(list.get(0));
                    selectedId = list.get(0).dataId;
                    //GC20190713
                    Constant.Para = list.get(0).para;
                    Constant.WaveData = list.get(0).waveData;
                    Constant.SimData = list.get(0).waveDataSim;
                    Constant.PositonV = list.get(0).positionVirtual;
                    Constant.PositionR = list.get(0).positionReal;
                    Constant.SaveLocation = list.get(0).location;
                } else {
                    rlHasRecords.setVisibility(View.GONE);
                    tvNoRecords.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void setDataByPosition(Data data) {
        tvCableId.setText(String.valueOf(data.cableId));
        if (Constant.CurrentUnit == Constant.FtUnit) {
            if (!TextUtils.isEmpty(data.line))
                tvCableLength.setText(UnitUtils.miToFt(Double.valueOf(data.line)));
            else {
                tvCableLength.setText("0");
            }
            tvCableLengthUnit.setText(R.string.ft);
        } else {
            tvCableLength.setText(data.line);
            tvCableLengthUnit.setText(R.string.mi);
        }

        tvDate.setText(data.date);
        tvMode.setText(initMode(Integer.valueOf(data.mode)));
        tvRange.setText(initRange(data.range));
        if (Constant.CurrentUnit == Constant.MiUnit) {
            if (Constant.CurrentSaveUnit == Constant.MiUnit)
                tvFaultLocation.setText(new DecimalFormat("0.00").format(data.location));
            else
                tvFaultLocation.setText(UnitUtils.ftToMi(data.location));
        } else {
            if (Constant.CurrentSaveUnit == Constant.FtUnit)
                tvFaultLocation.setText(new DecimalFormat("0.00").format(data.location));
            else
                tvFaultLocation.setText(UnitUtils.miToFt(data.location));
        }

        tvPhase.setText(initphase(Integer.valueOf(data.phase)));
        tvOperator.setText(data.tester);
        tvTestSite.setText(data.tester);
    }

    private String initRange(int range) {
        switch (range) {
            case RANGE_250:
                if (Constant.CurrentUnit == Constant.MiUnit) {
                    return getContext().getResources().getString(R.string.btn_250m);
                } else if (Constant.CurrentUnit == Constant.FtUnit) {
                    return getContext().getResources().getString(R.string.btn_250m_to_ft);
                }

            case RANGE_500:
                if (Constant.CurrentUnit == Constant.MiUnit) {
                    return getContext().getResources().getString(R.string.btn_500m);
                } else {
                    return getContext().getResources().getString(R.string.btn_500m_to_ft);
                }
            case RANGE_1_KM:

                if (Constant.CurrentUnit == Constant.MiUnit) {
                    return getContext().getResources().getString(R.string.btn_1km);
                } else {
                    return getContext().getResources().getString(R.string.btn_1km_to_yingli);
                }
            case RANGE_2_KM:
                if (Constant.CurrentUnit == Constant.MiUnit) {
                    return getContext().getResources().getString(R.string.btn_2km);
                } else {
                    return getContext().getResources().getString(R.string.btn_2km_to_yingli);
                }
            case RANGE_4_KM:
                if (Constant.CurrentUnit == Constant.MiUnit) {
                    return getContext().getResources().getString(R.string.btn_4km);
                } else {
                    return getContext().getResources().getString(R.string.btn_4km_to_yingli);
                }

            case RANGE_8_KM:
                if (Constant.CurrentUnit == Constant.MiUnit) {
                    return getContext().getResources().getString(R.string.btn_8km);
                } else {
                    return getContext().getResources().getString(R.string.btn_8km_to_yingli);
                }
            case RANGE_16_KM:
                if (Constant.CurrentUnit == Constant.MiUnit) {
                    return getContext().getResources().getString(R.string.btn_16km);
                } else {
                    return getContext().getResources().getString(R.string.btn_16km_to_yingli);
                }
            case RANGE_32_KM:
                if (Constant.CurrentUnit == Constant.MiUnit) {
                    return getContext().getResources().getString(R.string.btn_32km);
                } else {
                    return getContext().getResources().getString(R.string.btn_32km_to_yingli);
                }
            case RANGE_64_KM:
                if (Constant.CurrentUnit == Constant.MiUnit) {
                    return getContext().getResources().getString(R.string.btn_64km);
                } else {
                    return getContext().getResources().getString(R.string.btn_64km_to_yingli);
                }
            default:
                return "";
        }
    }

    private String initMode(int mode) {
        switch (mode) {
            case TDR:
                return getContext().getResources().getString(R.string.btn_tdr);
            case ICM:
                return getContext().getResources().getString(R.string.btn_icm);
            case ICM_DECAY:
                return getContext().getResources().getString(R.string.btn_icm_decay);
            case SIM:
                return getContext().getResources().getString(R.string.btn_sim);
            case DECAY:
                return getContext().getResources().getString(R.string.btn_decay);
            default:
                return "";
        }
    }

    private String initphase(int phase) {
        switch (phase) {
            case 0:
                return getContext().getResources().getString(R.string.phaseA);
            case 1:
                return getContext().getResources().getString(R.string.phaseB);
            case 2:
                return getContext().getResources().getString(R.string.phaseC);
            default:
                return "";
        }
    }

}
