package com.shushijuhe.shushijuheread.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.adapter.BookCategoryAdapter;
import com.shushijuhe.shushijuheread.bean.CategoriesBean;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;

public class BookCategoryFragment extends Fragment implements View.OnClickListener {

    private TextView tv_male, tv_female;
    private View view;
    private RecyclerView rv_male, rv_female;
    private BookCategoryAdapter adapter_male, adapter_female;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bookcategory, container, false);
        initView();
        return view;
    }

    private void initView() {
        tv_female = view.findViewById(R.id.bookcategory_tv_show_female);
        tv_male = view.findViewById(R.id.bookcategory_tv_show_male);
        tv_female.setOnClickListener(this);
        tv_male.setOnClickListener(this);

        adapter_male = new BookCategoryAdapter(getActivity());
        rv_male = view.findViewById(R.id.bookcategory_rv_show_male);
        rv_male.setAdapter(adapter_male);
        rv_male.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        adapter_female = new BookCategoryAdapter(getActivity());
        rv_female = view.findViewById(R.id.bookcategory_rv_show_female);
        rv_female.setAdapter(adapter_female);
        rv_female.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        setTvBg(tv_male);
        setData();
    }

    private void setData() {
        DataManager.getInstance().getCategories(new ProgressSubscriber<CategoriesBean>(new SubscriberOnNextListenerInstance() {
            @Override
            public void onNext(Object o) {
                adapter_female.setList((CategoriesBean) o, 0);
                adapter_male.setList((CategoriesBean) o, 1);
            }
        }, getActivity(), "获取分类信息中。。。"));
    }

    @Override
    public void onClick(View v) {
        if (v == tv_female) {
            setTvBg(tv_female);
            rv_male.setVisibility(View.GONE);
            rv_female.setVisibility(View.VISIBLE);
        } else if (v == tv_male) {
            setTvBg(tv_male);
            rv_female.setVisibility(View.GONE);
            rv_male.setVisibility(View.VISIBLE);
        }
    }

    private void setTvBg(TextView tv) {
        tv_female.setBackgroundResource(R.drawable.text_bg);
        tv_male.setBackgroundResource(R.drawable.text_bg);
        tv.setBackgroundResource(R.color.itemBg);
    }
}
