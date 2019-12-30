package net.kehui.www.t_907_origin.fragment;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.kehui.www.t_907_origin.R;
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
public class ModeFragment extends Fragment {
    @BindView(R.id.btn_tdr)
    public Button btnTdr;
    @BindView(R.id.btn_icm)
    public Button btnIcm;
    @BindView(R.id.btn_sim)
    public Button btnSim;
    @BindView(R.id.btn_decay)
    public Button btnDecay;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View modeLayout = inflater.inflate(R.layout.mtd_layout, container, false);
        unbinder = ButterKnife.bind(this, modeLayout);
        return modeLayout;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnTdr.setEnabled(false);
        btnIcm.setEnabled(true);
        btnSim.setEnabled(true);
        btnDecay.setEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_tdr, R.id.btn_icm, R.id.btn_sim, R.id.btn_decay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_tdr:
//                ((MainActivity) Objects.requireNonNull(getActivity())).setMode(0x11);
                break;
            case R.id.btn_icm:
//                ((MainActivity) Objects.requireNonNull(getActivity())).setMode(0x22);
                btnTdr.setEnabled(true);
                btnIcm.setEnabled(false);
                btnSim.setEnabled(true);
                btnDecay.setEnabled(true);
                break;
            case R.id.btn_sim:
//                ((MainActivity) Objects.requireNonNull(getActivity())).setMode(0x33);
                btnTdr.setEnabled(true);
                btnIcm.setEnabled(true);
                btnSim.setEnabled(false);
                btnDecay.setEnabled(true);
                break;
            case R.id.btn_decay:
                //G?  方法报警告作用
//                ((MainActivity) Objects.requireNonNull(getActivity())).setMode(0x44);
                btnTdr.setEnabled(true);
                btnIcm.setEnabled(true);
                btnSim.setEnabled(true);
                btnDecay.setEnabled(false);
                break;
            default:
                break;
        }
    }

}
