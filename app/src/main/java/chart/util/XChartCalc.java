package chart.util;

/**
 * Created by Administrator on 2017/1/4.
 */

public class XChartCalc {
    /**
     *  floats[0] x轴  floats[1]Y轴
     * @param cirX  中心x轴
     * @param cirY  中心y轴
     * @param radius  半径
     * @param cirAngle 角度
     * @return
     */
    //依圆心坐标，半径，扇形角度，计算出扇形终射线与圆弧交叉点的xy坐标
    public float [] CalcArcEndPointXY(float cirX, float cirY, float radius, float cirAngle){
        float []  floats=new float[2];
        //将角度转换为弧度
        float arcAngle = (float) (2*Math.PI * cirAngle / 360.0);
        floats[0] = cirX + (float)(Math.cos(arcAngle)) * radius;
        floats[1]= cirY + (float)(Math.sin(arcAngle)) * radius;
        return floats;
    }
}
