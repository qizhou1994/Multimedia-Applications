package com.zq.mediaplay.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author zq
 * Created by CTSIG on 2018/4/25.
 * Email : qizhou1994@126.com
 * 水波纹特效
 */

public class MyVisualizerView extends View {

    //bytes数据保存了波形的抽样点
    private byte[] bytes;
    //保存点
    private float[] points;
    //画笔
    private Paint paint;
    //形状
    private Rect rect;
    //类型
    private byte type;
    public MyVisualizerView(Context context) {
        super(context);
        initView();
    }

    private void initView(){
        paint = new Paint();
        rect = new Rect();
        bytes = null;

        //画笔属性
        paint.setStrokeWidth(1f);
        paint.setAntiAlias(true);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
    }

    /**
     * 绘制视图
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //无数据直接返回
        if(bytes == null){
            return;
        }
        //绘制白色背景
        canvas.drawColor(Color.WHITE);
        //使用rect对象记录该组件的宽度和高度
        rect.set(0,0,getWidth(),getHeight());
        //总数据的长度
        int sum = bytes.length-1;
        switch (type){
            case 0:
                for(int i = 0 ; i < sum;i++){
                    float left = getWidth()*i / sum;
                    //根据波形的值计算该矩形的高度
                    float top = rect.height() - (byte)(bytes[i+1]+128) * rect.height()/128;
                    float right = left + 1;
                    float bottom = rect.height();
                    canvas.drawRect(left,top,right,bottom,paint);
                }
                break;
            case 1:
                //绘制柱状的波形图 隔18个样式绘制一个
                for(int i = 0 ; i < sum;i+=18){
                    float left = getWidth()*i / sum;
                    //根据波形的值计算该矩形的高度
                    float top = rect.height() - (byte)(bytes[i+1]+128) * rect.height()/128;
                    float right = left + 6;
                    float bottom = rect.height();
                    canvas.drawRect(left,top,right,bottom,paint);
                }
                break;
            //绘制曲线波形图
            case 2:
                //万一point数组未初始化
                if(points == null || points.length < bytes.length * 4){
                    points = new float[bytes.length * 4];
                }
                for (int i = 0 ; i < sum ; i ++ ){
                    //计算第i个点的x
                    points[i*4] = rect.width()*i / sum;
                    //根据byte[i]的值(波形点的值) 计算低i个点的y坐标
                    points[i*4 + 1] = rect.height()/2 + (bytes[i] + 128)*128/rect.height()/2;

                    //计算第i+1个点的x
                    points[i*4 + 2] = rect.width()*i / sum;
                    //根据byte[i+3]的值(波形点的值) 计算低i+1个点的y坐标
                    points[i*4 + 1] = rect.height()/2 + (bytes[i] + 128)*128/rect.height()/2;
                }
                //绘制
                canvas.drawLines(points,paint);
                break;

            default:;
        }
    }

    /**
     * 更新视图数据
     * @param bytes
     */
    public void updateVisualizer(byte[] bytes){
        this.bytes = bytes;
        //重新绘制
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
       if(event.getAction() == MotionEvent.ACTION_DOWN){
           return false;
       }
        type ++;
        if(type>=3){
            type = 0;
        }
        return true;
    }
}
