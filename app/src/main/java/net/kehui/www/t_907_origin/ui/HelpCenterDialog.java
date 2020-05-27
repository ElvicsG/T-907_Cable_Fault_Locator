package net.kehui.www.t_907_origin.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.util.ScreenUtils;

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
        //20200521  帮助对话框
        params.width = (int) ( ScreenUtils.getScreenWidth(getContext()) * 0.7);
        params.height = (int) (ScreenUtils.getScreenHeight(getContext()) * 0.7);
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }


    private void initView() {
        ivClose = view.findViewById(R.id.iv_close);
        ivIntroductionButton = view.findViewById(R.id.iv_button_instructions);
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
            case R.id.iv_button_instructions:
                break;
            default:
                break;

        }
    }


}
