package autroid.business.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.List;

public class DrawingView extends androidx.appcompat.widget.AppCompatImageView {

    private boolean isShotAvailable=false;

    private float width=10f;

    private List<Pen> penList=new ArrayList<>();

    public DrawingView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        setDrawingCacheEnabled(true);
    }

    public void setShotAvailable(boolean shotAvailable){
        isShotAvailable=shotAvailable;
    }

    public void undo(){
        if(isShotAvailable && penList.size()>0){
            penList.remove(penList.size()-1);
            invalidate();
        }
    }

    private class Pen {

        Path path;
        Paint paint;

        Pen(int color, float width ) {
            path = new Path();
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(width);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Pen pen : penList) {
            canvas.drawPath(pen.path, pen.paint);
        }
    }


    public boolean onTouchEvent(MotionEvent event) {

        float eventX = event.getX();
        float eventY = event.getY();

        if(!isShotAvailable){
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                penList.add(new Pen(Color.WHITE, width));
                penList.get(penList.size() - 1).path.moveTo(eventX, eventY);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                penList.get(penList.size() - 1).path.lineTo(eventX, eventY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;

        }

        return false;
    }
}
