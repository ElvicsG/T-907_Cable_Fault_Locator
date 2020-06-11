package net.kehui.www.t_907_origin.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.application.Constant;
import net.kehui.www.t_907_origin.util.MultiLanguageUtil;
import net.kehui.www.t_907_origin.util.ScreenUtils;
import net.kehui.www.t_907_origin.view.MainActivity;

/**
 * @author 34238
 */
public class LanguageChangeDialog extends Dialog implements View.OnClickListener {

    private MainActivity context;
    ImageView ivClose;
    TextView tvEn;
    TextView tvZh;

    private View view;
    private boolean isClose = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = View.inflate(getContext(), R.layout.layout_language_change_dialog, null);
        setContentView(view);
        initView();

        final WindowManager.LayoutParams params = getWindow().getAttributes();
        //20200521  语言对话框
        params.width = (int) ( ScreenUtils.getScreenWidth(getContext()) * 0.5);
        params.height = (int) (ScreenUtils.getScreenHeight(getContext()) * 0.5);
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    private void setLanguage(String language) {
        switch (language) {
            case "follow_sys":
                MultiLanguageUtil.getInstance().updateLanguage("follow_sys");
                break;
            case "ch":
                MultiLanguageUtil.getInstance().updateLanguage("ch");
                break;
            case "en":
                MultiLanguageUtil.getInstance().updateLanguage("en");
                break;
            case "de":
                MultiLanguageUtil.getInstance().updateLanguage("de");
                break;
            case "fr":
                MultiLanguageUtil.getInstance().updateLanguage("fr");
                break;
            case "es":
                MultiLanguageUtil.getInstance().updateLanguage("es");
                break;
            default:
                break;
        }
        dismiss();
    }

    private void initView() {
        ivClose = view.findViewById(R.id.iv_close);
        tvEn = view.findViewById(R.id.tv_en);
        tvZh = view.findViewById(R.id.tv_zh);
        if (Constant.currentLanguage.equals("ch")) {
            tvZh.setEnabled(false);
            //语言对话框显示优化    //GC20200525
            tvZh.setTextColor(getContext().getResources().getColor(R.color.blue_4EDAFC));
        }
        if (Constant.currentLanguage.equals("en")) {
            tvEn.setEnabled(false);
            tvEn.setTextColor(getContext().getResources().getColor(R.color.blue_4EDAFC));
        }
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
            default:
                break;
        }
    }

    public LanguageChangeDialog(@NonNull Context context) {
        super(context);
    }

    public boolean getCloseStatus() {
        return isClose;
    }

}
