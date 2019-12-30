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
 * @date 2019/07/03
 */
public class AdjustFragment extends Fragment {
    @BindView(R.id.btn_gain_plus)
    Button btnGainPlus;
    @BindView(R.id.btn_gain_minus)
    Button btnGainMinus;
    @BindView(R.id.btn_balance_plus)
    public Button btnBalancePlus;
    @BindView(R.id.btn_balance_minus)
    public Button btnBalanceMinus;
    @BindView(R.id.btn_delay_plus)
    public Button btnDelayPlus;
    @BindView(R.id.btn_delay_minus)
    public Button btnDelayMinus;
    @BindView(R.id.btn_vel_plus)
    Button btnVelPlus;
    @BindView(R.id.btn_vel_minus)
    Button btnVelMinus;
    @BindView(R.id.btn_inductor_plus)
    public Button btnInductorPlus;
    @BindView(R.id.btn_inductor_minus)
    public Button btnInductorMinus;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View adjustLayout = inflater.inflate(R.layout.adj_layout, container, false);
        unbinder = ButterKnife.bind(this, adjustLayout);
        return adjustLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //GC20190705 调节栏fragment初始化——无延时、电感按钮
        btnDelayPlus.setVisibility(View.GONE);
        btnDelayMinus.setVisibility(View.GONE);
        btnInductorPlus.setVisibility(View.GONE);
        btnInductorMinus.setVisibility(View.GONE);
        //初始化按键无效显示效果
        btnDelayMinus.setEnabled(false);
        btnInductorMinus.setEnabled(false);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_gain_plus, R.id.btn_gain_minus, R.id.btn_balance_plus,R.id.btn_delay_plus,R.id.btn_delay_minus,
            R.id.btn_balance_minus, R.id.btn_vel_plus, R.id.btn_vel_minus,R.id.btn_inductor_plus,R.id.btn_inductor_minus})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_gain_plus:
//                int gain = ((MainActivity) getActivity()).getGain();
//                if (gain < 31) {
//                    gain++;
//                    //GC20190704 增益发送命令修改   (命令范围0-31阶)
//                    ((MainActivity) getActivity()).setGain(gain);
//                    btnGainMinus.setEnabled(true);
//                }
//                //增益命令到最大，按钮点击无效
//                if (gain == 31) {
//                    btnGainPlus.setEnabled(false);
//                }
                break;
            case R.id.btn_gain_minus:
//                gain = ((MainActivity) getActivity()).getGain();
//                if (gain > 0) {
//                    gain--;
//                    ((MainActivity) getActivity()).setGain(gain);
//                    btnGainPlus.setEnabled(true);
//                }
//                if (gain == 0) {
//                    btnGainMinus.setEnabled(false);
//                }
                break;
            case R.id.btn_balance_plus:
//                int balance = ((MainActivity) getActivity()).getBalance();
//                if (balance < 15) {
//                    balance++;
//                    //GC20190704 平衡发送命令修改   (命令范围0-15阶)
//                    ((MainActivity) getActivity()).setBalance(balance);
//                    btnBalanceMinus.setEnabled(true);
//                }
//                if (balance == 15) {
//                    btnBalancePlus.setEnabled(false);
//                }
                break;
            case R.id.btn_balance_minus:
//                balance = ((MainActivity) getActivity()).getBalance();
//                if (balance > 0) {
//                    balance--;
//                    ((MainActivity) getActivity()).setBalance(balance);
//                    btnBalancePlus.setEnabled(true);
//                }
//                if (balance == 0) {
//                    btnBalanceMinus.setEnabled(false);
//                }
                break;
            case R.id.btn_delay_plus:
//                int delay = ((MainActivity) getActivity()).getDelay();
//                if (delay < 1250) {
//                    delay = delay + 5;
//                    //GC20190704 延时发送命令修改   (延时从0到1250，点击一次增加5，共250阶)
//                    ((MainActivity) getActivity()).setDelay(delay);
//                    btnDelayMinus.setEnabled(true);
//                }
//                if (delay == 1250) {
//                    btnDelayPlus.setEnabled(false);
//                }
                break;
            case R.id.btn_delay_minus:
//                delay = ((MainActivity) getActivity()).getDelay();
//                if (delay > 0) {
//                    delay = delay - 5;
//                    ((MainActivity) getActivity()).setDelay(delay);
//                    btnDelayPlus.setEnabled(true);
//                }
//                if (delay == 0) {
//                    btnDelayMinus.setEnabled(false);
//                }
                break;
            case R.id.btn_vel_plus:
//                int velocity = ((MainActivity) getActivity()).getVelocity();
//                if (velocity < 300) {
//                    velocity++;
//                    ((MainActivity) getActivity()).setVelocity(velocity);
//                }
                break;
            case R.id.btn_vel_minus:
//                velocity = ((MainActivity) getActivity()).getVelocity();
//                if (velocity > 90) {
//                    velocity--;
//                    ((MainActivity) getActivity()).setVelocity(velocity);
//                }
                break;
            case R.id.btn_inductor_plus:
//                int inductor = ((MainActivity) getActivity()).getInductor();
//                if (inductor < 10) {
//                    inductor++;
//                    //GC20190710   (电感从3到10)
//                    ((MainActivity) getActivity()).setInductor(inductor);
//                    btnInductorMinus.setEnabled(true);
//                }
//                if (inductor == 10) {
//                    btnInductorPlus.setEnabled(false);
//                }
                break;
            case R.id.btn_inductor_minus:
//                inductor = ((MainActivity) getActivity()).getInductor();
//                if (inductor > 3) {
//                    inductor--;
//                    ((MainActivity) getActivity()).setInductor(inductor);
//                    btnInductorPlus.setEnabled(true);
//                }
//                if (inductor == 3) {
//                    btnInductorMinus.setEnabled(false);
//                }
                break;
            default:
                break;
        }
    }

}
