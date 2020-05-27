package net.kehui.www.t_907_origin.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.util.UpdateAppManagerUtils;

public class AppUpdateDialog extends Dialog {

    private String url;
    public static boolean isShowUpdating=false;
    private View view;

    public AppUpdateDialog(@NonNull Context context) {
        this(context, R.style.BaseDialogStyle);
    }

    public AppUpdateDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AppUpdateDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(getContext(), R.layout.dialog_app_update, null);
        setContentView(view);
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (1920 * 0.7);
        params.height = (int) (1080 * 0.7);
        getWindow().setAttributes(params);
        view.findViewById(R.id.tv_no_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.tv_update_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(url != null) {
                    isShowUpdating=true;
                    UpdateAppManagerUtils updateAppManagerUtils=new UpdateAppManagerUtils(getContext(),url);
                    updateAppManagerUtils.showDownloadDialog();
                }else{
                    Toast.makeText(getContext(), R.string.no_load_url,Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });
    }

   //设置更新信息
    public void setVersionEntity(String  url){
        this.url = url;
    }

}
