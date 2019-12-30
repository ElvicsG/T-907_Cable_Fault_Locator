package net.kehui.www.t_907_origin.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.application.Constant;
import net.kehui.www.t_907_origin.base.BaseActivity;
import net.kehui.www.t_907_origin.entity.Data;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author li.md
 * @date 2019/07/05
 */
public class SaveActivity extends BaseActivity {


    @BindView(R.id.et_date)
    EditText etDate;
    @BindView(R.id.et_time)
    EditText etTime;
    @BindView(R.id.et_mode)
    EditText etMode;
    @BindView(R.id.et_range)
    EditText etRange;
    @BindView(R.id.et_line)
    AutoCompleteTextView etLine;
    @BindView(R.id.et_phase)
    EditText etPhase;
    @BindView(R.id.sp_phase)
    Spinner spPhase;
    @BindView(R.id.et_tester)
    AutoCompleteTextView etTester;
    @BindView(R.id.et_location)
    AutoCompleteTextView etLocation;
    @BindView(R.id.btn_Confirm_Save)
    Button btnConfirmSave;

    String date;
    String time;

    Date date1 = new Date(System.currentTimeMillis());


    private List<String> phaseList = new ArrayList<>();
    private String[] line = new String[100];
    private String[] tester = new String[100];
    private String[] location = new String[100];


    public List<String> lineList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        ButterKnife.bind(this);
        initFrame();
    }

    private void initFrame() {
        //Data data = datas.get(2);

        //lineList.add(data.line);

        setEtDate();
        setEtTime();
        setEtMode();
        setEtRange();
        setEtLine();
        setSpPhase();
        setEtTester();
        setEtLocation();

    }

    private void setEtDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd", Locale.US);
        date = sdf.format(date1);
        etDate.setText(this.date);
        Constant.Date = this.date;
        etDate.setEnabled(false);
    }

    private void setEtTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
        time = sdf.format(date1);
        etTime.setText(time);
        Constant.Time = time;
        etTime.setEnabled(false);
    }

    private void setEtMode() {
//        int mode = Constant.ModeValue;
//        switch (mode) {
//            case TDR:
//                etMode.setText(getResources().getString(R.string.btn_tdr));
//                Constant.Mode = getResources().getString(R.string.btn_tdr);
//                break;
//            case ICM:
//                etMode.setText(getResources().getString(R.string.btn_icm));
//                Constant.Mode = getResources().getString(R.string.btn_icm);
//                break;
//            case SIM:
//                etMode.setText(getResources().getString(R.string.btn_sim));
//                Constant.Mode = getResources().getString(R.string.btn_sim);
//                break;
//            case DECAY:
//                etMode.setText(getResources().getString(R.string.btn_decay));
//                Constant.Mode = getResources().getString(R.string.btn_decay);
//                break;
//            default:
//                break;
//        }
        etMode.setEnabled(false);
    }

    private void setEtRange() {
//        int range = Constant.RangeValue;
//        switch (range) {
//            case RANGE_500:
//                etRange.setText(getResources().getString(R.string.btn_500m));
//                Constant.Range = getResources().getString(R.string.btn_500m);
//                break;
//            case RANGE_1_KM:
//                etRange.setText(getResources().getString(R.string.btn_1km));
//                Constant.Range = getResources().getString(R.string.btn_1km);
//                break;
//            case RANGE_2_KM:
//                etRange.setText(getResources().getString(R.string.btn_2km));
//                Constant.Range = getResources().getString(R.string.btn_2km);
//                break;
//            case RANGE_4_KM:
//                etRange.setText(getResources().getString(R.string.btn_4km));
//                Constant.Range = getResources().getString(R.string.btn_4km);
//                break;
//            case RANGE_8_KM:
//                etRange.setText(getResources().getString(R.string.btn_8km));
//                Constant.Range = getResources().getString(R.string.btn_8km);
//                break;
//            case RANGE_16_KM:
//                etRange.setText(getResources().getString(R.string.btn_16km));
//                Constant.Range = getResources().getString(R.string.btn_16km);
//                break;
//            case RANGE_32_KM:
//                etRange.setText(getResources().getString(R.string.btn_32km));
//                Constant.Range = getResources().getString(R.string.btn_32km);
//                break;
//            case RANGE_64_KM:
//                etRange.setText(getResources().getString(R.string.btn_64km));
//                Constant.Range = getResources().getString(R.string.btn_64km);
//                break;
//            default:
//                break;
//        }
        etRange.setEnabled(false);

    }

    private void setEtLine() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, line);
        etLine.setAdapter(adapter);
        Constant.Line = Constant.Line + etLine.getText().toString();
    }

    private void setSpPhase() {
        phaseList.add(getResources().getString(R.string.phaseA));
        phaseList.add(getResources().getString(R.string.phaseB));
        phaseList.add(getResources().getString(R.string.phaseC));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, phaseList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPhase.setAdapter(adapter);
        spPhase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
//                    case 0:
//                        Constant.Phase = getResources().getString(R.string.phaseA);
//                        break;
//                    case 1:
//                        Constant.Phase = getResources().getString(R.string.phaseB);
//                        break;
//                    case 2:
//                        Constant.Phase = getResources().getString(R.string.phaseC);
//                        break;
//                    default:
//                        Constant.Phase = getResources().getString(R.string.phaseA);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setEtTester() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, tester);
        etTester.setAdapter(adapter);
        Constant.Tester = Constant.Tester + etTester;
    }

    private void setEtLocation() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, location);
        etLocation.setAdapter(adapter);
        //Constant.Location = Constant.Location + etTester;
    }


    @OnClick(R.id.btn_Confirm_Save)
    public void onViewClicked() {
        final Data data = formatData(new Data());
        Flowable.create((FlowableOnSubscribe<List>) e -> {
            dao.insertData(data);
            List list = Arrays.asList(dao.query());
            e.onNext(list);
            e.onComplete();
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(List list) {
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onComplete() {
            }
        });
        finish();
    }

    private Data formatData(Data data) {

        data.date = Constant.Date.trim();
        data.time = Constant.Time.trim();
        data.mode = Constant.Mode + "";
        data.range = Constant.Range;
        data.line = etLine.getText().toString().trim();
        data.phase = Constant.Phase + "";
        data.tester = etTester.getText().toString().trim();
        //data.location = etLocation.getText().toString().trim();
        data.waveData = Constant.WaveData;
        data.waveDataSim = Constant.SimData;
        //参数数据 方式  范围 增益 波速度
        data.para = new int[]{Constant.ModeValue, Constant.RangeValue, Constant.Gain,
                (int) Constant.Velocity};
        return data;
    }
}
/**
 * 后续工作：
 * 填入资料后自动更新 AutoCompleteText List(从数据库回调)
 * 上同查询界面spinnerList自动回调
 */