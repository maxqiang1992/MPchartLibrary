package chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import chart.util.Statscs;
import chart.util.util;

/**
 * 这是一个柱状图
 */
public class StatscsView extends View {

    public StatscsView(Context context) {
        super(context);
        init(context, null);
    }

    public StatscsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context, attrs);
    }
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
    private void init(Context context, AttributeSet attrs)
    {
        this.context=context;
        rectWidth=util.dip2px(context,20);
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
     * 跟新自身的数据 需要View子类重绘。
     *
     * 主线程 刷新控件的时候调用：
     * this.invalidate();  失效的意思。
     * this.postInvalidate();  可以子线程 更新视图的方法调用。
     *
     * */
    private int[] yTitlesStrings =
            new int[]{30000,25000,20000,15000,10000,5000,0};
    private ArrayList<ArrayList<Rect>> rectLists; //所有柱子的rect
    private  int leftPading =50;// 左边距
    private  int  max= 30000;//value 最大值
    private  int rectWidth =10; //柱子  宽度
    private Boolean overlap=true;

    private  int boot =620;
    private  int leftHeight = 600;// 左侧外周的 需要划分的高度：
    private  ArrayList<ArrayList<Statscs>> date;

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
        requestLayout();
    }

    /**
     * 设置 柱状 参数
     * @param date
     */
    public  void  setDate(ArrayList<ArrayList<Statscs>> date){
        this.date=date;
        postInvalidate();
    }

/*    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }*/
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasure(widthMeasureSpec,heightMeasureSpec);
        }
    public  void  setMeasure(int w,int h){
        if(date==null){
            return;
        }
        int length=0;
        for (int i = 0; i <date.size() ; i++) {
            length++;
            for (int j = 0; j <date.get(i).size() ; j++) {
                length++;
            }
        }
        setMeasuredDimension(length*rectWidth+leftPading+rectWidth*2, boot+20);
    }
    public  void  setLayout(){
        if(date==null){
            return;
        }
        int length=0;
        for (int i = 0; i <date.size() ; i++) {
            length++;
            for (int j = 0; j <date.get(i).size() ; j++) {
                length++;
            }
        }
        layout(getLeft(),getTop(),length*rectWidth+leftPading+rectWidth*1, getTop()+boot+40);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if(date==null){
            return;
        }
        rectLists = new ArrayList<>();
        int width = getWidth();
        // 1 绘制坐标线：
        //绘制竖线
        canvas.drawLine(leftPading, 10, leftPading, boot, axisLinePaint);
        //绘制横线
        canvas.drawLine(leftPading, boot, width - 10, boot, axisLinePaint);

        // 2 绘制坐标内部的水平线
        int hPerHeight = leftHeight / (yTitlesStrings.length-1);

        hLinePaint.setTextAlign(Paint.Align.CENTER);
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
        // 4  绘制 X 周 做坐标
        int xAxisLength = width - leftPading;
        int step = xAxisLength / date.size();
        if(date!=null && date.size()>1){
            for (int i = 0; i <date.size() ; i++) {

                ArrayList<Statscs> list=date.get(i);
                ArrayList<Rect> rects=new ArrayList<>();
                for (int j = 0; j <list.size() ; j++) {
                    Statscs statscs=list.get(j);
                    int  value= statscs.getValue();
                    int colour=statscs.getColour();
                    double t;
                    t = (double) max / leftHeight;
                    recPaint.setColor(colour);
                    Rect rect = new Rect();
                      //  rect.left = leftPading + step * (j + 0) + rectWidth;
                      //  rect.right = leftPading + step * (j + 0)+rectWidth*2;
                        int steps=((date.size()+1)*rectWidth);
                        rect.left  = leftPading + steps * (j+0)  +rectWidth +rectWidth*i;
                        rect.right = leftPading + steps * (j+0)  +rectWidth*i +rectWidth*2;
//                  当前的相对高度：
                    float v= (float) (value/t);
                    int h = (int) (leftHeight - v + 20);
                    rect.top = h;
                    rect.bottom = boot;
                    canvas.drawRect(rect, recPaint);
                    rects.add(rect);

                    float textLength = titlePaint.measureText(value+"");
                    //顶部添加数字
                    //canvas.drawText(value+ "", rect.right+textLength/2-((rect.right-rect.left)/2), rect.top, titlePaint);
                }
                rectLists.add(rects);
            }
            if(date!=null && date.size()>1) {
                for (int i = 0; i < date.size(); i++) {
                    ArrayList<Statscs> list = date.get(i);
                    ArrayList<Rect> rects = rectLists.get(i);
                    for (int j = 0; j < rects.size(); j++) {
                        Statscs statscs = list.get(j);
                        Rect rect = rects.get(j);
                        int value = statscs.getValue();
                        float textLength = titlePaint.measureText(value + "");
                        //顶部添加数字
                        canvas.drawText(value + "", rect.right + textLength / 2 - ((rect.right - rect.left) / 2), rect.top, titlePaint);
                    }
                }
            }

        }
 }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(onRectClickListen==null){
            return super.onTouchEvent(event);
        }
        int  startX=0;
        int  startY=0;
        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                startX= (int) event.getX();
                startY= (int) event.getY();
            case  MotionEvent.ACTION_UP:
                int  X= (int) event.getX();
                int  Y= (int) event.getY();
                    if(startX>=X-10 && startX<=X+10 && startY>=Y-10 &&startY<=Y+10){
                        for (int i = 0; i <rectLists.size() ; i++) {
                            ArrayList<Rect>  rects=  rectLists.get(i);
                            for (int j = 0; j <rects.size() ; j++) {
                                Rect  rect=rects.get(j);
                                if(X>= rect.left&& X<=rect.right&& Y>=rect.top&&Y<=rect.bottom ){
                                    Toast.makeText(context,i+"---"+j, Toast.LENGTH_LONG).show();
                                    onRectClickListen.RectClick(i,j);
                                    return super.onTouchEvent(event);
                                }
                            }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }
    public  void  setOnRectClickListen(OnRectClickListen onRectClickListen){
        this.onRectClickListen=onRectClickListen;
    }
    public  interface OnRectClickListen{
        Void  RectClick(int index0, int index1);
    }
    public OnRectClickListen onRectClickListen;
}


