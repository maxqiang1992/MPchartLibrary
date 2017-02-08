package cn.com.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import chart.PanelPieChart;
import cn.com.rx_java.R;

/**饼图
 * Created by Administrator on 2017/2/6.
 */
public class PanelPieChartActivity extends Activity {
    private ArrayList<float[]> floats=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_pie);
        floats.add(new float[]{17,17,16,50});
        floats.add(new float[]{17,17,16,20,30});
        floats.add(new float[]{30,10,10,20,30});
        floats.add(new float[]{30,10,10,20,15,15});
        floats.add(new float[]{8,8,9,9,9,13,13,13,18});
        final Random random=new Random();
         final PanelPieChart panelPieChart= (PanelPieChart) findViewById(R.id.panel);

        findViewById(R.id.up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panelPieChart.setArrPer(floats.get(random.nextInt(floats.size() - 1)));
            }
        });
    }
}
