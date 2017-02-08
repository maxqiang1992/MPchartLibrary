package chart.util;

/**
 * Created by Administrator on 2017/1/11.
 */

public class  Statscs{
    private  int  value; // 值
    private  int  colour;//颜色
    public  Statscs(int  value,int colour){
        this.value=value;
        this.colour=colour;
    }
    public  int  getValue(){
        return  value;
    }
    public  int  getColour(){
        return  colour;
    }
}
