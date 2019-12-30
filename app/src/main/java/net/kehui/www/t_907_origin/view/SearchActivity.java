package net.kehui.www.t_907_origin.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.application.Constant;
import net.kehui.www.t_907_origin.base.BaseActivity;
import net.kehui.www.t_907_origin.entity.Data;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
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

/**
 * @author li.md
 * @date 2019/07/05
 */
public class SearchActivity extends BaseActivity {

    @BindView(R.id.sp_date)
    Spinner spDate;
    @BindView(R.id.sp_mode)
    Spinner spMode;
    @BindView(R.id.sp_range)
    Spinner spRange;
    @BindView(R.id.sp_line)
    Spinner spLine;
    @BindView(R.id.sp_phase)
    Spinner spPhase;
    @BindView(R.id.sp_tester)
    Spinner spTester;
    @BindView(R.id.sp_location)
    Spinner spLocation;
    @BindView(R.id.btn_Confirm_Search)
    Button  btnConfirmSearch;

    private List<String> modeList  = new ArrayList<>();
    private List<String> rangeList = new ArrayList<>();
    private List<String> phaseList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initFrame();

    }

    private void initFrame() {

        setSpDate();
        setSpMode();
        setSpRange();
        setSpLine();
        setSpPhase();
        setSpTester();
        setSpLocation();

    }

    private void setSpDate() {

    }

    private void setSpMode() {
        modeList.add(getResources().getString(R.string.btn_tdr));
        modeList.add(getResources().getString(R.string.btn_icm));
        modeList.add(getResources().getString(R.string.btn_sim));
        modeList.add(getResources().getString(R.string.btn_decay));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, modeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMode.setAdapter(adapter);
        spMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0:
//                        Constant.Mode = getResources().getString(R.string.btn_tdr);
//                        break;
//                    case 1:
//                        Constant.Mode = getResources().getString(R.string.btn_icm);
//                        break;
//                    case 2:
//                        Constant.Mode = getResources().getString(R.string.btn_sim);
//                        break;
//                    case 3:
//                        Constant.Mode = getResources().getString(R.string.btn_decay);
//                        break;
//                    default:
//                        break;
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setSpRange() {
        rangeList.add(getResources().getString(R.string.btn_500m));
        rangeList.add(getResources().getString(R.string.btn_1km));
        rangeList.add(getResources().getString(R.string.btn_2km));
        rangeList.add(getResources().getString(R.string.btn_4km));
        rangeList.add(getResources().getString(R.string.btn_8km));
        rangeList.add(getResources().getString(R.string.btn_16km));
        rangeList.add(getResources().getString(R.string.btn_32km));
        rangeList.add(getResources().getString(R.string.btn_64km));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, rangeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRange.setAdapter(adapter);
//        spRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0:
//                        Constant.Range = getResources().getString(R.string.btn_500m);
//                        break;
//                    case 1:
//                        Constant.Range = getResources().getString(R.string.btn_1km);
//                        break;
//                    case 2:
//                        Constant.Range = getResources().getString(R.string.btn_2km);
//                        break;
//                    case 3:
//                        Constant.Range = getResources().getString(R.string.btn_4km);
//                        break;
//                    case 4:
//                        Constant.Range = getResources().getString(R.string.btn_8km);
//                        break;
//                    case 5:
//                        Constant.Range = getResources().getString(R.string.btn_16km);
//                        break;
//                    case 6:
//                        Constant.Range = getResources().getString(R.string.btn_32km);
//                        break;
//                    case 7:
//                        Constant.Range = getResources().getString(R.string.btn_64km);
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }

    private void setSpLine() {

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
                    default:break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpTester() {

    }

    private void setSpLocation() {

    }

    @OnClick( R.id.btn_Confirm_Search)
    public void onViewClicked(View view) {
        Flowable.create((FlowableOnSubscribe<List>) e -> {
            Data[] data = null;
            data = dao.query();
            /*if(!TextUtils.isEmpty(idET.getText().toString().trim())){
                data = dao.queryUid(Integer.valueOf(idET.getText().toString().trim()));
            }else if(!TextUtils.isEmpty(nameET.getText().toString().trim())){
                data = dao.queryName(nameET.getText().toString().trim());
            }else if(!TextUtils.isEmpty(placeET.getText().toString().trim())){
                data = dao.queryPlace(placeET.getText().toString().trim());
            }else{
                data = dao.query();
            }*/

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

            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onComplete() {
            }
        });
        Intent it = new Intent(getApplicationContext(), ListActivity.class);
        startActivity(it);
        finish();

    }
}

