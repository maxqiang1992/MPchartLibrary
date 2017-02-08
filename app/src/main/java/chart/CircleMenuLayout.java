package chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import chart.util.XChartCalc;
import cn.com.rx_java.R;

/**
 * Created by Administrator on 2017/1/13.
 */

public class CircleMenuLayout extends ViewGroup {
    private LayoutInflater inflater;  //自定义加载布局...
    private XChartCalc xChartCalc = new XChartCalc();
    private String[] ItemTexts = new String[]{"HTML", "CSS", "JS",
            "JQuery", "DOM", "TEMP"};
    private int[] ItemImgs = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private float angle = 360 / (ItemImgs.length - 1);

    private float[] cirAngle = new float[]{0, 60, 120, 180, 240, 300};//初始化位置

    public CircleMenuLayout(Context context) {
        super(context);
    }

    /**
     *
     * @param ItemTexts   text
     * @param ItemImgs    图片
     * @param cirAngle    初始化位置  0-360
     */
    public void setAllDate(String[] ItemTexts, int[] ItemImgs,float[] cirAngle){
        this.ItemImgs=ItemImgs;
        this.ItemTexts=ItemTexts;
        this.cirAngle=cirAngle;
    }
    private int ViewW;
    private int ViewH;
    private int radius;
    private int x;//圆中心x
    private int y;//圆中心y
    private ArrayList<Rect> rectArrayList = new ArrayList<>();

    public CircleMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        inflater = LayoutInflater.from(context);
        for (int i = 0; i < ItemImgs.length; i++) {
            final int j = i;
            View view = inflater.inflate(R.layout.web_circle, this, false);
            view.measure(width, height);
            ViewW = view.getMeasuredWidth();
            ViewH = view.getMeasuredHeight();
            rectArrayList.add(new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
            ImageView iv = (ImageView) view.findViewById(R.id.id_circle_menu_item_image);
            TextView tv = (TextView) view.findViewById(R.id.id_circle_menu_item_text);
            iv.setImageResource(ItemImgs[i]);
            tv.setText(ItemTexts[i]);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(getContext(), ItemTexts[j], Toast.LENGTH_SHORT).show();
                }
            });
            addView(view);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        x = (getRight() - getLeft()) / 2;
        y = (getBottom() - getTop()) / 2;
        radius = getWidth() / 4;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            float[] xy = xChartCalc.CalcArcEndPointXY(x, y, radius, cirAngle[i]);
            View child = getChildAt(i);
            rectArrayList.set(i, new Rect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom()));
            int x = (int) xy[0];
            int y = (int) xy[1];
            child.layout(x - ViewW / 2, y - ViewH / 2, x + ViewW / 2, y + ViewH / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("xy", x + "x" + y + "y" + radius + "r");

        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        canvas.drawCircle(x, y, radius, paint);
    }

    private float endY = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private long mDownTime;
    private AutoFlingRunnable mFlingRunnable;
    private Boolean isFling = false;
    private float startY;
    private float speed = 3;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float Y = ev.getY();
        float X = ev.getX();
        switch (ev.getAction()) {
            //按下操作...事件机制自带的变量...
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                endY = ev.getY();
                mDownTime = System.currentTimeMillis();
                // 如果当前已经在快速滚动
                if (isFling) {
                    // 移除快速滚动的回调
                    removeCallbacks(mFlingRunnable);
                    isFling = false;
                    return true;
                }
            case MotionEvent.ACTION_MOVE:

                if (ev.getY() > endY) {
                    for (int i = 0; i < cirAngle.length; i++) {
                        cirAngle[i] = cirAngle[i] + speed >= 360 ? cirAngle[i] + speed - 360 : cirAngle[i] + speed;
                    }
                } else if (ev.getY() < endY) {
                    for (int i = 0; i < cirAngle.length; i++) {
                        cirAngle[i] = cirAngle[i] - speed <= 0 ? 360 + (cirAngle[i] - speed) : cirAngle[i] - speed;
                    }
                }
                endY = ev.getY();
                requestLayout();
                break;
            case MotionEvent.ACTION_UP:
                long anglePerSecond =
                        System.currentTimeMillis() - mDownTime;
                Log.i("long", anglePerSecond + "");
                //判定0.15秒内 移动超过150 就快速滑动
                if (anglePerSecond < 150 && Math.abs(startY - endY) > 100) {
                    post(mFlingRunnable = new AutoFlingRunnable(30, startY - endY < 0 ? true : false));
                }


                break;
        }
        //事件分发，点击控件位置 事件分发下去
        for (int j = 0; j < rectArrayList.size(); j++) {
            Rect rect = rectArrayList.get(j);
            if (X >= rect.left && X <= rect.right && Y >= rect.top && Y <= rect.bottom) {
                return super.dispatchTouchEvent(ev);
            }
        }
        return true;
    }


    private class AutoFlingRunnable implements Runnable {

        private float a;
        private Boolean isUp;

        public AutoFlingRunnable(float velocity, Boolean isUp) {
            this.a = velocity;
            this.isUp = isUp;
        }

        public void run() {
            a--;
            if (a >= 0) {
                isFling = true;
                if (!isUp) {
                    for (int i = 0; i < cirAngle.length; i++) {
                        cirAngle[i] = cirAngle[i] - a <= 0 ? 360 + (cirAngle[i] - a) : cirAngle[i] - a;
                    }
                } else {
                    for (int i = 0; i < cirAngle.length; i++) {
                        cirAngle[i] = cirAngle[i] + a >= 360 ? cirAngle[i] + a - 360 : cirAngle[i] + a;
                    }
                }
                //减速旋转...
                postDelayed(this, 100);//需要保证时刻对页面进行刷新..因为始终要进行新的布局...
                requestLayout();
            } else {
                isFling = false;
            }

        }
    }
}
