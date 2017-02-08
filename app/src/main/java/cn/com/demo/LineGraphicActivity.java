package cn.com.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import chart.LineGraphicView;
import chart.util.Statscs;
import chart.util.util;
import cn.com.rx_java.R;

/**
 * Created by Administrator on 2017/2/7.
 */

public class LineGraphicActivity extends Activity {

    private LineGraphicView lineGraphicView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graphic);
        lineGraphicView = (LineGraphicView) findViewById(R.id.line);
        lineGraphicView.setHeight(util.dip2px(this, 400));
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
        int index = random.nextInt(5)+2;
        for (int i = 0; i < 1; i++) {
            ArrayList<Statscs> list = new ArrayList<>();
            String color = "";
            for (int j = 0; j < 6; j++) {
                int c = random.nextInt(9);
                color = color + c;
            }
            for (int j = 0; j < index; j++) {
                int num = random.nextInt(30000);
                list.add(new Statscs(num, Color.parseColor("#30" + color)));
            }
            date.add(list);
        }
        lineGraphicView.setDate(date);
        lineGraphicView.setLayout();
    }
}
