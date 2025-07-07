package com.example.qgassessment.Weather;

import androidx.fragment.app.Fragment;

import org.xutils.http.RequestParams;
import org.xutils.x;
import org.xutils.common.Callback;



//1.声明整体模块
//2.执行网络请求操作
public class BaseFragment extends Fragment implements Callback.CommonCallback<String> {

    public void loadData(String path){
        RequestParams params = new RequestParams(path);
        x.http().get(params,this);
    }
    //获取数据成功时回调的接口
    @Override
    public void onSuccess(String result){

    }
    //获取数据失败时回调的接口
    @Override
    public void onError(Throwable ex, boolean isOnCallback){

    }
    //取消请求时回调的接口
    @Override
    public void onCancelled(CancelledException cex) {

    }

    //请求完成时回调的接口
    @Override
    public void onFinished(){

    }
}
