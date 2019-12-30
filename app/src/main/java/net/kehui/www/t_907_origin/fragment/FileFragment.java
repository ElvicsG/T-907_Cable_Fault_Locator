package net.kehui.www.t_907_origin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.view.ListActivity;
import net.kehui.www.t_907_origin.view.MainActivity;
import net.kehui.www.t_907_origin.view.SaveActivity;
import net.kehui.www.t_907_origin.view.SearchActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author IF
 * @date 2018/3/26
 */
public class FileFragment extends Fragment {
    @BindView(R.id.btn_save)
    Button       btnSave;
    @BindView(R.id.btn_browse)
    Button       btnBrowse;
    @BindView(R.id.btn_search)
    Button       btnDisplay;
    @BindView(R.id.file_sidebar)
    LinearLayout fileSidebar;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fileLayout = inflater.inflate(R.layout.file_layout, container, false);
        unbinder = ButterKnife.bind(this, fileLayout);
        return fileLayout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_save, R.id.btn_browse, R.id.btn_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                Intent intentSave = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(),SaveActivity.class);
                startActivity(intentSave);
                break;
            case R.id.btn_browse:
                Intent it = new Intent(
                        Objects.requireNonNull(getActivity()).getApplicationContext(),
                        ListActivity.class);
                startActivity(it);
                break;
            case R.id.btn_search:
                Intent intentSearch =
                        new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), SearchActivity.class);
                startActivity(intentSearch);
                break;
            default:
                break;
        }
    }
}
