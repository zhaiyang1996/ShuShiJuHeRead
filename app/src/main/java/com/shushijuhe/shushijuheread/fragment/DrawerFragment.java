package com.shushijuhe.shushijuheread.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.AboutUsActivity;

import butterknife.BindView;

public class DrawerFragment extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.drawer_me)
    TextView textView;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_drawer;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.drawer_me:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
        }
    }
}
