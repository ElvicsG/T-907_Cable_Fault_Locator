package net.kehui.www.t_907_origin.fragment;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.application.Constant;
import net.kehui.www.t_907_origin.view.MainActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Gong
 * @date 2019/07/04
 */
public class WaveFragment extends Fragment {
    @BindView(R.id.btn_zoom_in)
    public Button btnZoomIn;
    @BindView(R.id.btn_zoom_out)
    public Button btnZoomOut;
    @BindView(R.id.btn_res)
    public Button btnRes;
    @BindView(R.id.btn_memory)
    public Button btnMemory;
    @BindView(R.id.btn_compare)
    public Button btnCompare;
    @BindView(R.id.wavePrevious)
    public Button btnWavePrevious;
    @BindView(R.id.waveNext)
    public Button btnWaveNext;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View optionLayout = inflater.inflate(R.layout.wave_layout, container, false);
        unbinder = ButterKnife.bind(this, optionLayout);
        return optionLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //GC20190705 操作栏fragment初始化——无波形选择按钮
        btnWavePrevious.setVisibility(View.INVISIBLE);
        btnWaveNext.setVisibility(View.INVISIBLE);
        //初始化按键无效显示效果
        btnZoomIn.setEnabled(false);
        btnZoomOut.setEnabled(false);
        btnRes.setEnabled(false);
        btnWavePrevious.setEnabled(false);
        btnWaveNext.setEnabled(false);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_zoom_in, R.id.btn_zoom_out, R.id.btn_res, R.id.btn_memory, R.id.btn_compare, R.id.wavePrevious, R.id.waveNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_zoom_in:
                //GC20190711
//                int density = ((MainActivity) Objects.requireNonNull(getActivity())).getDensity();
//                if (density > 1) {
//                    density = density / 2;
//                    ((MainActivity) getActivity()).setDensity(density);
//                    btnZoomOut.setEnabled(true);
//                    btnRes.setEnabled(true);
//                }
                //无法放大
//                if (density == 1) {
//                    btnZoomIn.setEnabled(false);
//                }
                break;
            case R.id.btn_zoom_out:
//                density = ((MainActivity) Objects.requireNonNull(getActivity())).getDensity();
//                if (density < Constant.DensityMax) {
//                    density = density * 2;
//                    ((MainActivity) getActivity()).setDensity(density);
//                    btnZoomIn.setEnabled(true);
//                    btnRes.setEnabled(true);
//                }
//                //缩小到最初显示，只显示放大按钮
//                if (density == Constant.DensityMax) {
//                    btnZoomOut.setEnabled(false);
//                    btnRes.setEnabled(false);
//                }
                break;
            case R.id.btn_res:
//                ((MainActivity) Objects.requireNonNull(getActivity())).setDensity(Constant.DensityMax);
                btnZoomIn.setEnabled(true);
                btnZoomOut.setEnabled(false);
                btnRes.setEnabled(false);
                break;
            case R.id.btn_memory:
//                ((MainActivity) Objects.requireNonNull(getActivity())).clickMemory();
                break;
            case R.id.btn_compare:
//                ((MainActivity) Objects.requireNonNull(getActivity())).clickCompare();
                break;
            case R.id.wavePrevious:
                //GC20190702 SIM共8组，从1-8
//                int selectSim = ((MainActivity) Objects.requireNonNull(getActivity())).getSelectSim();
//                if (selectSim > 1) {
//                    selectSim--;
//                    ((MainActivity) getActivity()).setSelectSim(selectSim);
//                    btnWaveNext.setEnabled(true);
//                }
                //到第1组波形，下翻按钮点击无效
//                if (selectSim == 1) {
//                    btnWavePrevious.setEnabled(false);
//                }
                break;
            case R.id.waveNext:
//                selectSim = ((MainActivity) Objects.requireNonNull(getActivity())).getSelectSim();
//                if (selectSim < 8) {
//                    selectSim++;
//                    ((MainActivity) getActivity()).setSelectSim(selectSim);
//                    btnWavePrevious.setEnabled(true);
//                }
                //到第8组波形，上翻按钮点击无效
//                if (selectSim == 8) {
//                    btnWaveNext.setEnabled(false);
//                }
                break;
            default:
                break;

        }
    }
}
