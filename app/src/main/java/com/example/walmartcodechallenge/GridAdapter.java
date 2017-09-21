package com.example.walmartcodechallenge;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.walmartcodechallenge.model.ForecastCondition;

import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private List<ForecastCondition> list;
    private LayoutInflater layoutInflater;

    public GridAdapter(Context context, List<ForecastCondition> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View grid = view;
        if(grid==null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = layoutInflater.inflate(R.layout.custom_layout,null);
        }

        TextView tv1 = (TextView) grid.findViewById(R.id.timeCheck);
        ImageView img1 = (ImageView) grid.findViewById(R.id.imageCheck);
        TextView tv2 = (TextView) grid.findViewById(R.id.tempCheck);

        tv1.setText(list.get(i).getFactTime().getCivil());
        switch (list.get(i).getCondition()){
            case "Clear":
                if((Integer.parseInt(list.get(i).getFactTime().getHour())>=19 && Integer.parseInt(list.get(i).getFactTime().getHour())<=23)||(Integer.parseInt(list.get(i).getFactTime().getHour())>=0 && Integer.parseInt(list.get(i).getFactTime().getHour())<=6)){
                    img1.setImageResource(R.drawable.if_moon_black_110831);
                }else{
                    img1.setImageResource(R.drawable.if_54_sun_183467);
                }
                break;
            case "Partly Cloudy":
                if((Integer.parseInt(list.get(i).getFactTime().getHour())>=19 && Integer.parseInt(list.get(i).getFactTime().getHour())<=23)||(Integer.parseInt(list.get(i).getFactTime().getHour())>=0 && Integer.parseInt(list.get(i).getFactTime().getHour())<=6)){
                    img1.setImageResource(R.drawable.if_moon_cloud_black_110830);
                }else{
                    img1.setImageResource(R.drawable.if_2_cloud_sun_183429);
                }
                break;
            default:
                    img1.setImageResource(R.drawable.if_4_cloud_rain_183451);
        }
        tv2.setText(list.get(i).getTemp().getMetric());

        return grid;
    }
}
