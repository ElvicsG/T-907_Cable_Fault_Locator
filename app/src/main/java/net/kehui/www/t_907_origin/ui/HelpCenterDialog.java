package net.kehui.www.t_907_origin.ui;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.receiver.RestartAppReceiver;
import net.kehui.www.t_907_origin.util.MultiLanguageUtil;
import net.kehui.www.t_907_origin.view.SplashActivity;

/**
 * Create by jwj on 2019/11/26
 */
public class HelpCenterDialog extends Dialog implements View.OnClickListener {

    ImageView ivClose;

    ImageView ivSafeGuide;
    ImageView ivOperationGuide;
    ImageView ivIntroductionButton;

    private View view;

    public HelpCenterDialog(@NonNull Context context) {
        super(context);
    }

    public HelpCenterDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected HelpCenterDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = View.inflate(getContext(), R.layout.layout_help_center_dialog, null);
        setContentView(view);
        initView();
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (1920 * 0.5);
        params.height = (int) (1080 * 0.5);
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }


    private void initView() {
        ivClose = view.findViewById(R.id.iv_close);
        ivIntroductionButton = view.findViewById(R.id.iv_introdution_button);
        ivOperationGuide = view.findViewById(R.id.iv_operation_guide);
        ivSafeGuide = view.findViewById(R.id.iv_safe_guide);

        ivClose.setOnClickListener(this);
        ivIntroductionButton.setOnClickListener(this);
        ivOperationGuide.setOnClickListener(this);
        ivSafeGuide.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.iv_operation_guide:

                break;
            case R.id.iv_safe_guide:

                break;
            case R.id.iv_introdution_button:

                break;

        }
    }


}
