package chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import chart.util.Statscs;
import chart.util.util;


/**
 * 这是一个折线图
 * Created by Administrator on 2017/1/11.
 */

public class LineChart  extends View{
    private int[] yTitlesStrings =
            new int[]{30000,25000,20000,15000,10000,5000,0};
    private ArrayList<ArrayList<Rect>> rectLists; //所有柱子的rect
    private  int leftPading =50;// 左边距
    private  int  max= 30000;//value 最大值
    private  int rectWidth =100; //边距
    private  int boot =620; //控件高度
    private  int leftHeight = 600;// 左侧外周的 需要划分的高度：
    private  ArrayList<ArrayList<Statscs>> date; //数据


    //  坐标轴 轴线 画笔：
    private Paint axisLinePaint;
    //  坐标轴水平内部 虚线画笔
    private Paint hLinePaint;
    //  绘制文本的画笔
    private Paint titlePaint;
    //  矩形画笔 柱状图的样式信息
    private Paint recPaint;
    private Paint timePaint;
    private Context context;
    public LineChart(Context context) {
        super(context);
        init(context);
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private void init(Context context) {
        this.context=context;
        rectWidth=util.dip2px(context,100);
        axisLinePaint = new Paint();
        hLinePaint = new Paint();
        titlePaint = new Paint();
        recPaint = new Paint();
        timePaint=new Paint();
        axisLinePaint.setColor(Color.DKGRAY);
        hLinePaint.setColor(Color.LTGRAY);
        titlePaint.setColor(Color.BLACK);
        timePaint.setColor(Color.RED);
        titlePaint.setTextSize(util.dip2px(context,10));
        leftPading=util.dip2px(context,40);
    }
    /**
     * 设置 竖线边距
     * @param leftPading
     */
    public void  setLeftPading(int leftPading){
        this.leftPading=leftPading;
    }

    /**
     *
     * @param yTitlesStrings 设置 数据值
     */
    public  void  setyTitlesStrings(int[] yTitlesStrings){
        this.yTitlesStrings=yTitlesStrings;
        if(yTitlesStrings.length>=1){
            this.max=yTitlesStrings[0];
        }
    }

    /**
     * 设置柱子的宽度
     * @param rectWidth
     */
    public  void  setRectWidth(int  rectWidth){
        this.rectWidth= util.dip2px(context,rectWidth);
    }

    /**
     * 设置控件高度
     * @param leftHeight
     */
    public  void  setHeight(int  leftHeight ){
        this.leftHeight=leftHeight;
        this.boot=leftHeight+20;
    }

    /**
     * 设置 柱状 参数
     * @param date
     */
    public  void  setDate(ArrayList<ArrayList<Statscs>> date){
        this.date=date;
        postInvalidate();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getXY()[0],getXY()[1]);
    }
    public  void  setLayout(){
        layout(getLeft(),getTop(),getXY()[0],getTop()+getXY()[1]);
    }
    private float temLeft=0;
    private float temTop=0;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        // 1 绘制坐标线：
        //绘制竖线
        canvas.drawLine(leftPading, 10, leftPading, boot, axisLinePaint);
        //绘制横线
        canvas.drawLine(leftPading, boot, width - 10, boot, axisLinePaint);


        int hPerHeight = leftHeight / (yTitlesStrings.length-1);
        hLinePaint.setTextAlign(Paint.Align.CENTER);
        // 2 绘制坐标内部的水平线
        for (int i = 0; i < yTitlesStrings.length-1; i++) {
            canvas.drawLine(leftPading, 20 + i * hPerHeight, width - 10, 20 + i * hPerHeight, hLinePaint);
        }
        // 3 绘制 Y 周坐标
        Paint.FontMetrics metrics = titlePaint.getFontMetrics();
        int descent = (int) metrics.descent;
        titlePaint.setTextAlign(Paint.Align.RIGHT);
        for (int i = 0; i < yTitlesStrings.length; i++) {
            canvas.drawText(yTitlesStrings[i]+"", leftPading, 20 + i * hPerHeight + descent, titlePaint);
        }
        DrawLine(canvas);
    }
    private  void DrawLine(Canvas canvas){
        if(date!=null && date.size()>=1){
            for (int i = 0; i <date.size() ; i++) {
                Path mPath = new Path();
                Paint p = new Paint();
                p.setStyle(Paint.Style.STROKE);
                p.setColor(Color.GREEN);
                p.setStrokeWidth((float) 5);
                ArrayList<Statscs> list=date.get(i);
                for (int j = 0; j <list.size() ; j++) {
                    Statscs statscs=list.get(j);
                    int  value= statscs.getValue();
                    int colour=statscs.getColour();
                    double t;
                    t = (double) max / leftHeight;
                    recPaint.setColor(colour);
                    p.setColor(colour);
                    Rect rect = new Rect();
                    //int steps=((date.size()+1)*rectWidth);
                    rect.left  = leftPading +rectWidth*j+leftPading;
//                  当前的相对高度：
                    float v= (float) (value/t);
                    int h = (int) (leftHeight - v + 20);
                    rect.top = h;
                    rect.bottom = boot;
              /*      Path path2=new Path();
                    path2.moveTo(100, 320);//设置Path的起点
                    path2.quadTo(150, 310, 170, 400); //设置贝塞尔曲线的控制点坐标和终点坐标
                    canvas.drawPath(path2, p);//画出贝塞尔曲线*/
                    if(j==0){
                        temLeft=rect.left;
                        temTop=rect.top;
                        mPath.moveTo(rect.left,rect.top);
                    }else {
                        mPath.lineTo(rect.left,rect.top);
                    }
                    if(j==list.size()-1){
                        canvas.drawPath(mPath, p);
                    }
                    canvas.drawCircle(rect.left,rect.top, 8,titlePaint);
                    //测量字符串宽度
                    float textLength = titlePaint.measureText(value+"");
                    //添加 点上文字
                    canvas.drawText(value+ "", rect.left+textLength/2, rect.top-10, titlePaint);
                }
            }
        }
    }
    public  int[]  getXY(){
        int[] xy=new int[2];
        if(date==null||date.size()==0){
            xy[0]=0;
            xy[1]=0;
            return xy;
        }
        xy[0]=date.get(0).size()*rectWidth+leftPading*1;
        xy[1]=boot+20;
       return xy;
    }
}
