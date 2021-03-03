package com.hjd.m_utilslib;


import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.bumptech.glide.Glide;
import com.hjd.apputils.base.BaseBindingActivity;
import com.hjd.apputils.utils.StatusBarUtil;
import com.hjd.apputils.utils.ToastUtils;
import com.hjd.m_utilslib.databinding.ActivityMainBinding;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static java.lang.annotation.ElementType.*;


public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {

    OneFragment oneFragment;
    TwoFragment twoFragment;


    @Override
    public void initData() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);

        Glide.with(this).load("http://p1.pstatp.com/large/166200019850062839d3").into(binding.img);

        binding.tv.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                ToastUtils.showShort("1111");
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.122:8081/").build();
                TestApi api = retrofit.create(TestApi.class);
                api.getBlog().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse
                            (Call<ResponseBody> call, Response<ResponseBody> response) {

                        try {
                            Logger.d(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Logger.e(t.getMessage());
                    }
                });
            }
        });

        binding.one.setOnClickListener(v -> {
            initTab(0);
        });
        binding.two.setOnClickListener(v -> {
            initTab(1);
        });
    }


    private void initTab(int i) {
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        switch (i) {
            case 0:
                if (oneFragment == null) {
                    oneFragment = new OneFragment();
                    transaction.add(R.id.ll_fragment, oneFragment);
                } else {
                    transaction.show(oneFragment);
                }
                break;
            case 1:
                if (twoFragment == null) {
                    twoFragment = new TwoFragment();
                    transaction.add(R.id.ll_fragment, twoFragment);
                } else {
                    transaction.show(twoFragment);
                }
                break;

        }
        transaction.commitAllowingStateLoss();

    }

    private void hideFragments(FragmentTransaction transaction) {
        if (oneFragment != null) {
            transaction.hide(oneFragment);
        }
        if (twoFragment != null) {
            transaction.hide(twoFragment);
        }


    }

    @Override
    public int checkPermission(String permission, int pid, int uid) {
        return super.checkPermission(permission, pid, uid);
    }

    @Target(TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ContentView {
        int value();
    }
}