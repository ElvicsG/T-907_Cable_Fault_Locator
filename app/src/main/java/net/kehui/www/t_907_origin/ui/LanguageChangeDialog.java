package net.kehui.www.t_907_origin.ui;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.adpter.RecordsAdapter;
import net.kehui.www.t_907_origin.application.Constant;
import net.kehui.www.t_907_origin.entity.Data;
import net.kehui.www.t_907_origin.receiver.RestartAppReceiver;
import net.kehui.www.t_907_origin.util.MultiLanguageUtil;
import net.kehui.www.t_907_origin.view.MainActivity;
import net.kehui.www.t_907_origin.view.SplashActivity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static net.kehui.www.t_907_origin.view.ListActivity.DISPLAY_ACTION;

/**
 * Create by jwj on 2019/11/26
 */
public class LanguageChangeDialog extends Dialog implements View.OnClickListener {

    ImageView ivClose;
    TextView tvEn;
    TextView tvZh;

    private View view;
    private boolean isClose = true;

    public boolean getCloseStatus() {
        return isClose;
    }

    public LanguageChangeDialog(@NonNull Context context) {
        super(context);
    }

    public LanguageChangeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LanguageChangeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = View.inflate(getContext(), R.layout.layout_language_change_dialog, null);

        setContentView(view);
        initView();

        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (1920 * 0.5);
        params.height = (int) (1080 * 0.5);
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

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
        dismiss();
//        Intent intentSplash = new Intent(getContext(), SplashActivity.class);
//        intentSplash.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getContext().startActivity(intentSplash);
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);
//        Intent intent = new Intent(getContext(), RestartAppReceiver.class);
//        intent.setPackage("net.kehui.www.t_907_origin");
//        intent.setComponent(new ComponentName("net.kehui.www.t_907_origin", "net.kehui.www.t_907_origin.RestartAppReceiver"));
//        intent.setAction("restartapp");
//        getContext().sendBroadcast(intent);
    }

    private void initView() {
        ivClose = view.findViewById(R.id.iv_close);
        tvEn = view.findViewById(R.id.tv_en);
        tvZh = view.findViewById(R.id.tv_zh);
        if (Constant.currentLanguage.equals("ch"))
            tvZh.setEnabled(false);
        if (Constant.currentLanguage.equals("en"))
            tvEn.setEnabled(false);
        ivClose.setOnClickListener(this);
        tvEn.setOnClickListener(this);
        tvZh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                isClose = true;
                dismiss();
                break;
            case R.id.tv_en:
                isClose = false;
                setLanguage("en");
                break;
            case R.id.tv_zh:
                isClose = false;
                setLanguage("ch");
                break;

        }
    }


}
