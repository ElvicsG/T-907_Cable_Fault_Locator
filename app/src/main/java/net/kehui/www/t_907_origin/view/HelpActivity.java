package net.kehui.www.t_907_origin.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Gong
 * @date 2020/03/12
 */
public class HelpActivity extends BaseActivity {

    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.btn_safe_guide)
    Button btnSafeGuide;
    @BindView(R.id.btn_operation_guide)
    Button btnOperationGuide;
    @BindView(R.id.btn_button_instructions)
    Button btnButtonInstruction;
    @BindView(R.id.btn_back)
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_safe_guide, R.id.btn_operation_guide, R.id.btn_button_instructions, R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }

    }

}

