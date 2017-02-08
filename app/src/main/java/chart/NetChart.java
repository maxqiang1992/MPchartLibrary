package chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import chart.util.XChartCalc;

/**网图
 * Created by Administrator on 2017/2/8.
 */

public class NetChart extends View{
    private int NetLength =6;
    private float NetAngle=360/NetLength; //蜘蛛网的间距
    private float radius;//半径
    private float centerX;//中心X轴
    private float centerY;//半径Y轴
    private Paint mainPaint =new Paint(); //绘制多边形画笔
    private Paint valuePaint=new Paint();//绘制属性多边形画笔
    private Paint valuePaint1=new Paint(); //绘制属性
    private  Paint valuePaint2=new Paint();
    private String[] textStr=new String[]{"法术","智力","攻击","力量","速度","敏捷","防御","耐力","气血","体质"};
    private float Max=100;
    private float[] value =new float[]{80,60,90,30,20,99,60,77};//网属性
    //设置多边形
    public  void setNetLength(int NetLength){
        this.NetLength=NetLength;
        NetAngle=360/NetLength;
    }
    //设置属性名
    public  void setTextStr(String[]  textStr){
        this.textStr=textStr;
    }
    //设置属性最大值
    public  void setMax(float Max){
        this.Max=Max;
    }
    //设置 各项属性的值
    public  void setValue(float[] value){
        this.value=value;
    }
    //绘制
    public  void  Draw(){
        postInvalidate();
    }

    public NetChart(Context context) {
        super(context);
    }

    public NetChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        mainPaint.setColor(Color.RED);
        mainPaint.setStyle(Paint.Style.STROKE);
        valuePaint.setColor(Color.parseColor("#200094ff"));
        valuePaint1.setTextSize(20);
        valuePaint1.setColor(Color.RED);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = Math.min(h, w)/2*0.7f;
        //中心坐标
        centerX = w/2;
        centerY = h/2;
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPolygon(canvas);
        drawValuePolygon(canvas);
        drawLines(canvas);
        drawVlaueText(canvas);
        drawText(canvas);
    }

    /**
     * 绘制多边形
     * @param canvas
     */
    public void drawPolygon(Canvas canvas){
        Path path = new Path();
        for (int j = 1; j <6 ; j++) {
            for (int i = 0; i <NetLength+1 ; i++) {
                    if (i==0){
                        path.moveTo((float) (centerX+radius*0.2*j),centerY);
                    }else {
                        float[] NetXY=new XChartCalc().CalcArcEndPointXY(centerX,centerY, (float) (radius*0.2*j),i*NetAngle);
                        path.lineTo(NetXY[0],NetXY[1]);
                    }
            }
        }
        canvas.drawPath(path, mainPaint);
    }

    /**
     * 绘制属性网
     * @param canvas
     */
    public void drawValuePolygon(Canvas canvas){
        Path path = new Path();
        Path path1 = new Path();
        for (int i = 0; i < NetLength; i++) {
            double mRadius= radius/Max*value[i];
            if(i==0){
                path.moveTo((float) (centerX+mRadius),centerY);
                path1.moveTo((float) (centerX+mRadius),centerY);
            }else {
                float[] NetXY=new XChartCalc().CalcArcEndPointXY(centerX,centerY, (float) mRadius,i*NetAngle);
                path.lineTo(NetXY[0],NetXY[1]);
                path1.lineTo(NetXY[0],NetXY[1]);
            }
            if(i==NetLength-1){
                path1.lineTo((float) (centerX+radius/Max*value[0]),centerY);
            }
        }
        path.close();
        canvas.drawPath(path,valuePaint);
        valuePaint2.setStyle(Paint.Style.STROKE);
        valuePaint2.setColor(Color.parseColor("#0094ff"));
        valuePaint2.setStrokeWidth(5);
        valuePaint2.setAntiAlias(true);
        canvas.drawPath(path1,valuePaint2);

    }
    /**
     * 绘制直线
     */
    private void drawLines(Canvas canvas){
        Path path = new Path();
        for (int i = 0; i <NetLength+1 ; i++) {
            float[] NetXY=new XChartCalc().CalcArcEndPointXY(centerX,centerY, radius,i*NetAngle);
                path.moveTo(centerX,centerY);
                path.lineTo(NetXY[0],NetXY[1]);
        }
        canvas.drawPath(path,mainPaint);
    }
    /**
     * 绘制属性值
     */
    private void drawVlaueText(Canvas canvas){
        for (int i = 0; i <5 ; i++) {
            float value= (float) (radius*0.2*i);
            int value1= (int) (Max/5*i);
            canvas.drawText(Float.toString(value1),centerX, centerY-value ,valuePaint1);
        }
    }
    /**
     * 绘制文字
     * @param canvas
     */
    private void drawText(Canvas canvas){

        for(int i=0;i<NetLength;i++){
            float[] xy=new XChartCalc().CalcArcEndPointXY(centerX,centerY,radius,NetAngle*i);
            float length=valuePaint1.measureText(textStr[i]);

            if(NetAngle*i==90){
                canvas.drawText(textStr[i],xy[0]-length/2, xy[1]+20 ,valuePaint1);
            }else if (NetAngle*i==270){
                canvas.drawText(textStr[i],xy[0]-length/2, xy[1] ,valuePaint1);
            }else if (NetAngle*i==0){
                canvas.drawText(textStr[i],xy[0], xy[1]+10 ,valuePaint1);
            } else if (NetAngle*i==180){
                canvas.drawText(textStr[i],xy[0]-length, xy[1]+10 ,valuePaint1);
            } else if (NetAngle*i>90 &&NetAngle*i<270){
                canvas.drawText(textStr[i],xy[0]-length, xy[1] ,valuePaint1);
            }else {
                canvas.drawText(textStr[i], xy[0], xy[1], valuePaint1);
            }
        }
    }
}
