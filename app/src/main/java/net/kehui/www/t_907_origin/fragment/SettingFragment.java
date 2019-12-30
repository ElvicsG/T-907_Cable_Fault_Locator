package net.kehui.www.t_907_origin.fragment;

import androidx.fragment.app.Fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.application.MyApplication;
import net.kehui.www.t_907_origin.receiver.RestartAppReceiver;
import net.kehui.www.t_907_origin.util.MultiLanguageUtil;
import net.kehui.www.t_907_origin.view.MainActivity;
import net.kehui.www.t_907_origin.view.SplashActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author IF
 * @date 2018/3/26
 */
public class SettingFragment extends Fragment {
    @BindView(R.id.btn_zero)
    Button btnZero;
    @BindView(R.id.btn_lang)
    Button btnLang;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View settingLayout = inflater.inflate(R.layout.setting_layout, container, false);
        unbinder = ButterKnife.bind(this, settingLayout);
        return settingLayout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_zero, R.id.btn_lang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_zero:
                //GC20190712    //G?
//                int simZero = ((MainActivity) Objects.requireNonNull(getActivity())).zero;
//                ((MainActivity) getActivity()).setSimZero(simZero);
                break;
            case R.id.btn_lang:
                setLanguage("en");
                break;
            default:
                break;
        }
    }

    private void setLanguage(String language) {
        if (language.equals("follow_sys")) {
            MultiLanguageUtil.getInstance().updateLanguage("follow_sys");
        } else if (language.equals("ch")) {
            MultiLanguageUtil.getInstance().updateLanguage("ch");

        } else if (language.equals("en")) {
            MultiLanguageUtil.getInstance().updateLanguage("en");

        } else if (language.equals("de")) {
            MultiLanguageUtil.getInstance().updateLanguage("de");
        } else if (language.equals("fr")) {
            MultiLanguageUtil.getInstance().updateLanguage("fr");
        } else if (language.equals("es")) {
            MultiLanguageUtil.getInstance().updateLanguage("es");
        }
        Intent intentSplash = new Intent(getActivity(), SplashActivity.class);
        intentSplash.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intentSplash);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        Intent intent = new Intent(getActivity(), RestartAppReceiver.class);
        intent.setPackage("net.kehui.www.t_907_origin");
        intent.setComponent(new ComponentName("net.kehui.www.t_907_origin", "net.kehui.www.t_907_origin.RestartAppReceiver"));
        intent.setAction("restartapp");
        getActivity().sendBroadcast(intent);


    }
}
