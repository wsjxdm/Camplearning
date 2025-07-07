package com.example.qgassessment.Weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.qgassessment.R;
import com.google.gson.Gson;

import java.util.List;

public class WeatherFragment extends BaseFragment {

    TextView currentTemp,currentCity,currentCondition,currentDate,rangeLowTemperature,rangeHighTemperature;
    TextView aqiAirQuality,aqiExercise,aqiOutgoing,aqiWindows;
    LinearLayout futureLayout;
    String url="http://gfeljm.tianqiapi.com/api?unescape=1&version=v91&appid=41427363&appsecret=tJ2W7To7&ext=&cityid=";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        initView(view);
        Bundle bundle = getArguments();
//      调用父类获取数据的方法
        loadData(url);
        return view;
    }

    @Override
    public void onSuccess(String result) {
//解析并展示数据
        parseShowData(result);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        super.onError(ex, isOnCallback);
    }

    private void parseShowData(String result) {
//      使用Gson解析数据
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class);
        WeatherBean.DataBean dataBean = weatherBean.getData().get(0);
//      获取指数信息集合列表
//      设置textView
        currentDate.setText(dataBean.getDate());
        currentCity.setText(weatherBean.getCity());
        rangeLowTemperature.setText(dataBean.getTem2());
        rangeHighTemperature.setText(dataBean.getTem1());
        currentCondition.setText(dataBean.getWea());
        currentTemp.setText(dataBean.getTem());
//        Picasso.with(getActivity()).load(dataBean.getWea_day_img()).into(currentConditionImage);
//获取未来5天天气情况加载到layout中
        List<WeatherBean.DataBean> forecastList = weatherBean.getData();
        forecastList.remove(0);
        for (int i = 0; i < forecastList.size()-1; i++){
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.weather_forecast_list,null);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            futureLayout.addView(itemView);
            TextView itemDate = itemView.findViewById(R.id.weather_forecast_date);
            TextView itemCondition = itemView.findViewById(R.id.weather_forecast_condition);
            TextView itemLowTemp = itemView.findViewById(R.id.weather_forecast_temperature_range_low);
            TextView itemHighTemp = itemView.findViewById(R.id.weather_forecast_temperature_range_high);

            WeatherBean.DataBean dataBean1 = forecastList.get(i);
            itemDate.setText(dataBean1.getWeek());
            itemCondition.setText(dataBean1.getWea());
            itemLowTemp.setText(dataBean1.getTem2());
            itemHighTemp.setText(dataBean1.getTem1());
        }

        aqiAirQuality.setText(weatherBean.getAqi().getAir_tips());
        aqiExercise.setText(weatherBean.getAqi().getYundong());
        aqiOutgoing.setText(weatherBean.getAqi().getWaichu());
        aqiWindows.setText(weatherBean.getAqi().getKaichuang());
    }

    private void initView(View view) {          //用于初始化所有控件
        currentTemp = view.findViewById(R.id.current_temperature);
        currentCity = view.findViewById(R.id.city);
        currentCondition = view.findViewById(R.id.weather_condition);
        currentDate = view.findViewById(R.id.weather_date);
        futureLayout = view.findViewById(R.id.weather_forecast_layout);
        rangeLowTemperature = view.findViewById(R.id.weather_temperature_range_low);
        rangeHighTemperature = view.findViewById(R.id.weather_temperature_range_high);
        aqiAirQuality = view.findViewById(R.id.air_quality);
        aqiExercise = view.findViewById(R.id.exercise);
        aqiOutgoing = view.findViewById(R.id.outgoing);
        aqiWindows = view.findViewById(R.id.windows);
    }

}
