package cn.com.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import chart.LineChart;
import chart.StatscsView;
import chart.util.Statscs;
import cn.com.rx_java.R;

/**
 * Created by Administrator on 2017/1/6.
 */

public class MainActivity extends Activity implements View.OnClickListener {

    private LineChart lineView;
    private StatscsView statscsView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.circle).setOnClickListener(this);
        findViewById(R.id.line).setOnClickListener(this);
        findViewById(R.id.panel).setOnClickListener(this);
        findViewById(R.id.statscs).setOnClickListener(this);
        findViewById(R.id.line_graphic).setOnClickListener(this);
        findViewById(R.id.net).setOnClickListener(this);
        findViewById(R.id.fill_line).setOnClickListener(this);
    }

    public void setDate() {
        ArrayList<ArrayList<Statscs>> date = new ArrayList<>();
        Random random = new Random();
        int index = random.nextInt(9) + 2;
        for (int i = 0; i < index; i++) {
            ArrayList<Statscs> list = new ArrayList<>();
            String color = "";
            for (int j = 0; j < 6; j++) {
                int c = random.nextInt(9);
                color = color + c;
            }
            for (int j = 0; j < index; j++) {
                int num = random.nextInt(30000);
                list.add(new Statscs(num, Color.parseColor("#" + color)));
            }
            date.add(list);
        }
        lineView.setDate(date);
        lineView.setLayout();
        statscsView.setDate(date);
        statscsView.setLayout();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.circle:
                startActivity(new Intent(MainActivity.this, CircleMenuLayoutActivity.class));
                break;
            case R.id.line:
                startActivity(new Intent(MainActivity.this, LineChartActivity.class));
                break;
            case R.id.statscs:
                startActivity(new Intent(MainActivity.this, StatscsViewActivity.class));
                break;
            case R.id.panel:
                startActivity(new Intent(MainActivity.this, PanelPieChartActivity.class));
                break;
            case R.id.line_graphic:
                startActivity(new Intent(MainActivity.this, LineGraphicActivity.class));
                break;
            case R.id.net:
                startActivity(new Intent(MainActivity.this, NetActivity.class));
                break;
            case R.id.fill_line:
                startActivity(new Intent(MainActivity.this, FillLineGraphicActivity.class));
                break;
        }
    }
}
