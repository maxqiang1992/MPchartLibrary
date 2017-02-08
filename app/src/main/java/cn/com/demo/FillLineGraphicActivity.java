package cn.com.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import chart.FillLineGraphicView;
import chart.util.Statscs;
import chart.util.util;
import cn.com.rx_java.R;

/**
 * Created by Administrator on 2017/2/8.
 */

public class FillLineGraphicActivity  extends Activity{

    private FillLineGraphicView fillLineGraphicActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_line);
        fillLineGraphicActivity = (FillLineGraphicView) findViewById(R.id.fill_line);
        fillLineGraphicActivity.setHeight(util.dip2px(this, 400));
        setDate();
        findViewById(R.id.up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
    }

    public void setDate() {
        ArrayList<ArrayList<Statscs>> date = new ArrayList<>();
        Random random = new Random();
        int index = random.nextInt(5)+5;
        for (int i = 0; i < 3; i++) {
            ArrayList<Statscs> list = new ArrayList<>();
            String color = "";
            for (int j = 0; j < 6; j++) {
                int c = random.nextInt(9);
                color = color + c;
            }
         //   list.add(new Statscs(0, Color.parseColor("#30"+color )));
            for (int j = 0; j < index; j++) {
                int num = random.nextInt(30000);
                list.add(new Statscs(num, Color.parseColor("#30" + color)));
            }
         //   list.add(new Statscs(0, Color.parseColor("#30" +color)));
            date.add(list);
        }
        fillLineGraphicActivity.setDate(date);
        fillLineGraphicActivity.setLayout();
    }
}
