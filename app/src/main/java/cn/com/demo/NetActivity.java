package cn.com.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

import chart.NetChart;
import cn.com.rx_java.R;

/**
 * Created by Administrator on 2017/2/8.
 */

public class NetActivity extends Activity {

    private NetChart netChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);
        netChart = (NetChart) findViewById(R.id.net);
            setData();

        findViewById(R.id.up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });

    }

    private void setData() {
        Random random = new Random();
        netChart.setMax(200);
        netChart.setNetLength(10);
        netChart.setValue(new float[]{random.nextInt(180)+10,
                random.nextInt(180)+20,
                random.nextInt(180)+10,
                random.nextInt(180)+20,
                random.nextInt(180)+10,
                random.nextInt(180)+20,
                random.nextInt(180)+10,
                random.nextInt(180)+20,
                random.nextInt(180)+10,
                random.nextInt(180)+20});
        netChart.Draw();
    }
}
