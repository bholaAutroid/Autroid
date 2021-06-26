package autroid.business.view.customviews;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Gallery;

import autroid.business.interfaces.OnPositionChangeListener;


public class CarouselView extends Gallery {
    private final Matrix mMatrix = new Matrix();
    /**
     * Graphics Camera used for transforming the matrix of ImageViews
     */
    private final Camera mCamera = new Camera();
    private int lastPosition;
    private OnPositionChangeListener mListener;
    private final Context context;

    /**
     * The maximum angle the Child ImageView will be rotated by
     */
    private int mMaxRotationAngle = 60; // 60

    public CarouselView(Context context) {
        super(context);
        this.setStaticTransformationsEnabled(true);
        this.context = context;
    }

    public CarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setStaticTransformationsEnabled(true);
        this.context = context;
    }

    public CarouselView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setStaticTransformationsEnabled(true);
        this.context = context;
    }

    /**
     * Get the max rotational angle of the image
     *
     * @return the mMaxRotationAngle
     */
    public int getMaxRotationAngle() {
        return mMaxRotationAngle;
    }

    /**
     * Set the max rotational angle of each image
     *
     * @param maxRotationAngle the mMaxRotationAngle to set
     */
    public void setMaxRotationAngle(int maxRotationAngle) {
        mMaxRotationAngle = maxRotationAngle;
    }

    /**
     * This is called during layout when the size of this view has changed. If
     * you were just added to the view hierarchy, you're called with the old
     * values of 0.
     *
     * @param w    Current width of this view.
     * @param h    Current height of this view.
     * @param oldw Old width of this view.
     * @param oldh Old height of this view.
     */
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (velocityX > 3000.0f) {
            velocityX = 3000.0f;
        } else if (velocityX < -3000.0f) {
            velocityX = -3000.0f;
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        setAnimationDuration(1000);
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean ret;
        // Android SDK 4.1
        if (android.os.Build.VERSION.SDK_INT > 15) {
            final float offset = calculateOffsetOfCenter(child);
            getTransformationMatrix(child, offset);
            //child.setAlpha(1 - Math.abs(offset));
            //code commented by Abhishek to remove alpha from child elements
           // child.setAlpha(Math.max((1 - Math.abs(offset)), 0.5f));
            final int saveCount = canvas.save();
            canvas.concat(mMatrix);
            ret = super.drawChild(canvas, child, drawingTime);
            canvas.restoreToCount(saveCount);
        } else {
            ret = super.drawChild(canvas, child, drawingTime);
        }
        return ret;
    }

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        if (android.os.Build.VERSION.SDK_INT > 15) {
            return false;
        } else {
            t.clear();
            t.setTransformationType(Transformation.TYPE_MATRIX);
            final float offset = calculateOffsetOfCenter(child);
            transformViewRoom(child, t, offset);
            return true;
        }

    }

    private int getCenterOfCoverflow() {
        return ((getWidth() - getPaddingLeft() - getPaddingRight()) >> 1) + getPaddingLeft();
    }

    private int getCenterOfView(View view) {
        return view.getLeft() + (view.getWidth() >> 1);
    }

    private float calculateOffsetOfCenter(View view) {
        final int pCenter = getCenterOfCoverflow();
        final int cCenter = getCenterOfView(view);

        float offset = (cCenter - pCenter) / (pCenter * 1.0f);
        offset = Math.min(offset, 1.0f);
        offset = Math.max(offset, -1.0f);
        if (mListener != null) {
            mListener.onPositionChanged(Integer.valueOf(view.getTag().toString()), offset);
        }
        return offset;
    }

    private void transformViewRoom(View child, Transformation t, float race) {
        Camera mCamera = new Camera();
        mCamera.save();
        final Matrix matrix = t.getMatrix();
        final int halfHeight = child.getMeasuredHeight() >> 1;
        final int halfWidth = child.getMeasuredWidth() >> 1;

        mCamera.translate(-race * 50, 0.0f, Math.abs(race) * 200);
        mCamera.getMatrix(matrix);
        matrix.preTranslate(-halfWidth, -halfHeight);
        matrix.postTranslate(halfWidth, halfHeight);
        mCamera.restore();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (i == 0)
            lastPosition = 0;

        int centerPosition = getSelectedItemPosition() - getFirstVisiblePosition();

        if (i == childCount - 1) {
            return centerPosition;
        } else if (i >= centerPosition) {
            lastPosition++;
            return childCount - lastPosition;
        } else {
            return i;
        }
    }

    private void getTransformationMatrix(View child, float offset) {
        final int halfWidth = child.getLeft() + (child.getMeasuredWidth() >> 1);
        final int halfHeight = child.getMeasuredHeight() >> 1;

        mCamera.save();
        mCamera.translate(-offset * 50, 0.0f, Math.abs(offset) * 100);

        mCamera.getMatrix(mMatrix);
        mCamera.restore();
        mMatrix.preTranslate(-halfWidth, -halfHeight);
        mMatrix.postTranslate(halfWidth, halfHeight);
    }

    /**
     * Method to set the position change listener
     *
     * @param listener -> the class to accept the event
     */
    public void setOnPositionChangeListener(OnPositionChangeListener listener) {
        this.mListener = listener;
    }


    final class TransformationAnimation extends Animation {
        final View v;

        TransformationAnimation(View _v) {
            v = _v;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            getChildStaticTransformation(v, t);
        }
    }
}
