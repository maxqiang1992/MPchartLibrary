package chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import chart.util.XChartCalc;
import chart.util.util;

/**
 * 这是一个饼图
 * Created by Administrator on 2017/1/11.
 */

public class PanelPieChart extends View {
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    //外部传入的比例参数
    private float[] arrPer = new float[]{10,10,10,10,10,20,15,15};
    //RGB颜色数组
    private  int arrColorRgb[][] = { {77, 83, 97},
            {148, 13, 181},
            {253, 46, 90},
            {52, 46, 188},
            {58, 234, 188},
            {57, 111, 188},
            {55, 43, 188},
            {56, 46, 188},
            {21, 13, 181},
            {35, 46, 90},
            {255, 46, 188},
            {33, 46, 188},
            {88, 46, 188},
            {99, 46, 188},
            {56, 67, 188},
            {148, 99, 181},
            {253, 200, 90},
            {52, 46, 255},
            {58, 46, 35},
            {57, 46, 199},
            {55, 46, 253},
            {56, 255, 255}} ;
    //指定突出哪个块
    private final int offsetBlock = 11111;
    private float currPer=0f;

    public PanelPieChart(Context context) {
        super(context);
    }
    public PanelPieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
    // 画笔
    private Paint paint;
    // 声明路径对象数组
    Path[] paths = new Path[3];
    private Context context;
    public PanelPieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        // 初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.CYAN);
        paint.setStrokeWidth(1);

    }
    //设置百分比
    public  void  setArrPer(float[]  floats){
        this.arrPer=floats;
        postInvalidate();
    }
    //设置饼图 各区域颜色
    public  void  setArrColorRgb(int[][] arrColorRgb){
        this.arrColorRgb=arrColorRgb;
    }
    public void onDraw(Canvas canvas){
        //画布背景
        canvas.drawColor(Color.WHITE);
        float cirX = getWidth()/2;
        float cirY = getHeight()/2 ;
        float radius = getHeight() / 3.3f ;//150;
        float arcLeft = cirX - radius;
        float arcTop  = cirY - radius ;
        float arcRight = cirX + radius ;
        float arcBottom = cirY + radius ;
        RectF arcRF0 = new RectF(arcLeft ,arcTop,arcRight,arcBottom);
        //画笔初始化
        Paint PaintArc = new Paint();
        Paint PaintLabel = new Paint();
        PaintLabel.setColor(Color.RED);
        PaintLabel.setTextSize(util.dip2px(context,10));

        Paint paint= new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setTextSize(util.dip2px(context,10));
        Paint centrePaint= new Paint();
        centrePaint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
       // paint.setStyle(Paint.Style.STROKE);
        //位置计算类
        XChartCalc xChartCalc = new XChartCalc();


        float Percentage = 0.0f;
        currPer=0f;
        int i= 0;
        for(i=0; i<arrPer.length; i++)
        {
            //将百分比转换为饼图显示角度
            Percentage = 360 * (arrPer[i]/ 100);
            Percentage = (float)(Math.round(Percentage *100))/100;
            //分配颜色
            PaintArc.setARGB(255,arrColorRgb[i][0], arrColorRgb[i][1], arrColorRgb[i][2]);
            if( i == offsetBlock) //指定突出哪个块
            {
                //偏移圆心点位置
                float newRadius = radius /10;
                //计算百分比标签
                float[] floats=xChartCalc.CalcArcEndPointXY(cirX,cirY,newRadius, currPer +Percentage/2);
                //计算线到达的xy轴
                float[] endLineFloats =xChartCalc.CalcArcEndPointXY(cirX,cirY, (float) (radius*1.2), currPer +Percentage/2);
                //计算开始线xy轴
                float[] startLineFloats =xChartCalc.CalcArcEndPointXY(cirX,cirY, (float) (radius*0.5), currPer +Percentage/2);
                //计算百分比标签
                float[] textFloats =xChartCalc.CalcArcEndPointXY(cirX,cirY, (float) (radius*1.3), currPer +Percentage/2);
                arcLeft = floats[0] - radius;
                arcTop  = floats[1] - radius ;
                arcRight = floats[0]+ radius ;
                arcBottom =floats[1] + radius ;
                RectF arcRF1 = new RectF(arcLeft ,arcTop,arcRight,arcBottom);   //确定突出部分的圆心
               // canvas.drawRect(arcRF1, paint);
                //在饼图中显示所占比例
                canvas.drawArc(arcRF1, currPer, Percentage, true, PaintArc);    //圆的半径不变，圆心移动
                canvas.drawText(Float.toString(arrPer[i])+"%",textFloats[0], textFloats[1] ,PaintLabel);
                canvas.drawLine(startLineFloats[0],startLineFloats[1],endLineFloats[0],endLineFloats[1],paint);
            }else{
                float newRadius = radius /10;
                float[] floats=xChartCalc.CalcArcEndPointXY(cirX,cirY,newRadius, currPer +Percentage/2);
                //计算线到达的xy轴
                float[] endLineFloats =xChartCalc.CalcArcEndPointXY(cirX,cirY, (float) (radius*1.5), currPer +Percentage/2);
                //计算开始线xy轴
                float[] startLineFloats =xChartCalc.CalcArcEndPointXY(cirX,cirY, (float) (radius*0.8), currPer +Percentage/2);
                //计算百分比标签
                float[] textFloats =xChartCalc.CalcArcEndPointXY(cirX,cirY, (float) (radius*1.3), currPer +Percentage/2);
                //绘制
                canvas.drawArc(arcRF0, currPer, Percentage, true, PaintArc);
                //绘制百分比
                //canvas.drawText(Float.toString(arrPer[i])+"%",textFloats[0]-xChartCalc.getX(paint,arrPer[i]+"",CurrPer), textFloats[1] ,PaintLabel);
                //绘制线
                Path path=new Path();
                PathMeasure pathMeasure=new PathMeasure();
                if(currPer >90 && currPer <270){
                    path.moveTo(endLineFloats[0],endLineFloats[1]);
                    path.lineTo(startLineFloats[0],startLineFloats[1]);
                    pathMeasure.setPath(path,false);
                    canvas.drawTextOnPath(Float.toString(arrPer[i])+"%",path,0,-2,PaintLabel);
                }else {
                    path.moveTo(startLineFloats[0],startLineFloats[1]);
                    path.lineTo(endLineFloats[0],endLineFloats[1]);
                    pathMeasure.setPath(path,false);
                    float  len= PaintLabel.measureText(Float.toString(arrPer[i])+"%");
                    Log.i("len", len +"");
                    canvas.drawTextOnPath(Float.toString(arrPer[i])+"%",path,0+pathMeasure.getLength()-len,-2,PaintLabel);
                }
                //围绕圆形绘制 text
                AroundThePathLocation(canvas,Float.toString(arrPer[i])+"%",arcRF0,paint,currPer,Percentage);

                canvas.drawLine(startLineFloats[0],startLineFloats[1],endLineFloats[0],endLineFloats[1],paint);
            }
            //下次的起始角度
            currPer += Percentage;
        }
        float newRadius = radius /10;
        //绘制中心圆
        canvas.drawCircle(cirX,cirY, newRadius,centrePaint);


    }

    /**
     * 围绕 圆形路径 绘制 字符串
     * @param canvas
     * @param text 字符串
     * @param arcRF0 路径
     * @param paint 画笔
     * @param currPer 开始角度
     * @param Percentage 结束角度
     */

    private void AroundThePathLocation(Canvas canvas, String text,RectF arcRF0,Paint paint,float currPer, float Percentage) {
        PathMeasure pathMeasure=new PathMeasure();
        Path path=new Path();
        path.addOval(arcRF0, Path.Direction.CW);
        pathMeasure.setPath(path,false);
        //计算 字符串长度
        float  len1= paint.measureText(text);
        //计算每个角度的 占据长度
        double db= pathMeasure.getLength()/360;
        //计算开始是的 偏移量
        float start= (float) (db*currPer);
        //计算结束时的偏移量
        float end= (float) db*(currPer+Percentage);
        //计算开始到结束的 路径长度
        float left=end-start;
        //计算 绘制到中间的距离的 偏移量
        float x=end-left/2-len1/2;
        //绘制text
        canvas.drawTextOnPath(text,path,x,0,paint);
    }
}
