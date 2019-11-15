package tw.orgiii20.app7;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.LinkedList;

public class MyView extends View {
    private LinkedList<LinkedList<Point>> lines;
    private LinkedList<LinkedList<Point>> recycle;

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //setBackgroundColor(Color.WHITE);
        lines = new LinkedList<>();
        recycle = new LinkedList<>();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float ex = event.getX(), ey  = event.getY();
        Point point = new Point(ex, ey);

        String act = "";
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            setBackgroundColor(Color.WHITE);

            LinkedList<Point> line = new LinkedList<>();
            lines.add(line);//this line's first point

            act = "DOWN";
        }else if (event.getAction() == MotionEvent.ACTION_UP){
            act = "UP";
        }else if (event.getAction() == MotionEvent.ACTION_MOVE){
            act = "MOVE";
        }

        lines.getLast().add(point);//this line's last point
        invalidate();
        recycle.clear();

        Log.i("MyView", "onTouchEvent: X= "+ex+" Y= "+ey+" Action: "+act);
        return true; //false:Only ACTION_DOWN ture:All ACTION
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        @SuppressLint("DrawAllocation") Paint panel = new Paint();
        panel.setColor(Color.GREEN);
        panel.setStrokeWidth(10);

        for (LinkedList<Point> line: lines){
            for(int i=1; i<line.size(); i++){
                Point p0 = line.get(i-1), p1 = line.get(i);
                canvas.drawLine(p0.x, p0.y, p1.x, p1.y, panel);
            }
        }

    }

    public void clear(){
        lines.clear();
        invalidate();
    }

    public void undo(){
        if (lines.size()>0){
            recycle.add(lines.removeLast());
            invalidate();
        }
    }
    public void redo(){
        if (recycle.size()>0){
            lines.add(recycle.removeLast());
            invalidate();
        }
    }

    private class Point{
        float x, y;
        Point(float x, float y){
            this.x = x;
            this.y = y;
        }
    }
}
