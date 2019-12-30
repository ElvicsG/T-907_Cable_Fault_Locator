package net.kehui.www.t_907_origin.fragment;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.view.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Gong
 * @date 2019/07/04
 */
public class RangeFragment extends Fragment {
    @BindView(R.id.btn_500m)
    Button       btn500m;
    @BindView(R.id.btn_1km)
    Button       btn1km;
    @BindView(R.id.btn_2km)
    Button       btn2km;
    @BindView(R.id.btn_4km)
    Button       btn4km;
    @BindView(R.id.btn_8km)
    Button       btn8km;
    @BindView(R.id.btn_16km)
    Button       btn16km;
    @BindView(R.id.btn_32km)
    Button       btn32km;
    @BindView(R.id.btn_64km)
    Button       btn64km;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rangeLayout = inflater.inflate(R.layout.range_layout, container, false);
        unbinder = ButterKnife.bind(this, rangeLayout);
        return rangeLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn500m.setEnabled(false);
        btn1km.setEnabled(true);
        btn2km.setEnabled(true);
        btn4km.setEnabled(true);
        btn8km.setEnabled(true);
        btn16km.setEnabled(true);
        btn32km.setEnabled(true);
        btn64km.setEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_500m, R.id.btn_1km, R.id.btn_2km, R.id.btn_4km, R.id.btn_8km, R.id
            .btn_16km, R.id.btn_32km, R.id.btn_64km})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_500m:
//                ((MainActivity)getActivity()).setRange(0x11);
                btn500m.setEnabled(false);
                btn1km.setEnabled(true);
                btn2km.setEnabled(true);
                btn4km.setEnabled(true);
                btn8km.setEnabled(true);
                btn16km.setEnabled(true);
                btn32km.setEnabled(true);
                btn64km.setEnabled(true);
                break;
            case R.id.btn_1km:
//                ((MainActivity)getActivity()).setRange(0x22);
                btn500m.setEnabled(true);
                btn1km.setEnabled(false);
                btn2km.setEnabled(true);
                btn4km.setEnabled(true);
                btn8km.setEnabled(true);
                btn16km.setEnabled(true);
                btn32km.setEnabled(true);
                btn64km.setEnabled(true);
                break;
            case R.id.btn_2km:
//                ((MainActivity)getActivity()).setRange(0x33);
                btn500m.setEnabled(true);
                btn1km.setEnabled(true);
                btn2km.setEnabled(false);
                btn4km.setEnabled(true);
                btn8km.setEnabled(true);
                btn16km.setEnabled(true);
                btn32km.setEnabled(true);
                btn64km.setEnabled(true);
                break;
            case R.id.btn_4km:
//                ((MainActivity)getActivity()).setRange(0x44);
                btn500m.setEnabled(true);
                btn1km.setEnabled(true);
                btn2km.setEnabled(true);
                btn4km.setEnabled(false);
                btn8km.setEnabled(true);
                btn16km.setEnabled(true);
                btn32km.setEnabled(true);
                btn64km.setEnabled(true);
                break;
            case R.id.btn_8km:
//                ((MainActivity)getActivity()).setRange(0x55);
                btn500m.setEnabled(true);
                btn1km.setEnabled(true);
                btn2km.setEnabled(true);
                btn4km.setEnabled(true);
                btn8km.setEnabled(false);
                btn16km.setEnabled(true);
                btn32km.setEnabled(true);
                btn64km.setEnabled(true);
                break;
            case R.id.btn_16km:
//                ((MainActivity)getActivity()).setRange(0x66);
                btn500m.setEnabled(true);
                btn1km.setEnabled(true);
                btn2km.setEnabled(true);
                btn4km.setEnabled(true);
                btn8km.setEnabled(true);
                btn16km.setEnabled(false);
                btn32km.setEnabled(true);
                btn64km.setEnabled(true);
                break;
            case R.id.btn_32km:
//                ((MainActivity)getActivity()).setRange(0x77);
                btn500m.setEnabled(true);
                btn1km.setEnabled(true);
                btn2km.setEnabled(true);
                btn4km.setEnabled(true);
                btn8km.setEnabled(true);
                btn16km.setEnabled(true);
                btn32km.setEnabled(false);
                btn64km.setEnabled(true);
                break;
            case R.id.btn_64km:
//                ((MainActivity)getActivity()).setRange(0x88);
                btn500m.setEnabled(true);
                btn1km.setEnabled(true);
                btn2km.setEnabled(true);
                btn4km.setEnabled(true);
                btn8km.setEnabled(true);
                btn16km.setEnabled(true);
                btn32km.setEnabled(true);
                btn64km.setEnabled(false);
                break;
            default:
                break;
        }
    }

}
