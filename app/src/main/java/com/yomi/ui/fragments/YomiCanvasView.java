package com.yomi.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class YomiCanvasView extends View {
    private Paint paint;
    private Path currentPath;
    private int currentColor = Color.BLACK;
    private float currentStrokeWidth = 8f;
    private boolean isEraserMode = false;
    
    private final List<DrawPath> paths = new ArrayList<>();
    private final List<DrawPath> undonePaths = new ArrayList<>();

    public YomiCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
    }

    private void setupPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw stored paths
        for (DrawPath dp : paths) {
            paint.setColor(dp.color);
            paint.setStrokeWidth(dp.strokeWidth);
            if (dp.isEraser) {
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            } else {
                paint.setXfermode(null);
            }
            canvas.drawPath(dp.path, paint);
        }

        // Draw current path
        if (currentPath != null) {
            paint.setColor(currentColor);
            paint.setStrokeWidth(currentStrokeWidth);
            if (isEraserMode) {
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            } else {
                paint.setXfermode(null);
            }
            canvas.drawPath(currentPath, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                undonePaths.clear();
                currentPath = new Path();
                currentPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                currentPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                paths.add(new DrawPath(new Path(currentPath), currentColor, currentStrokeWidth, isEraserMode));
                currentPath = null;
                break;
        }
        invalidate();
        return true;
    }

    public void setPaintColor(int color) {
        this.currentColor = color;
        this.isEraserMode = false;
    }

    public void setEraserMode(boolean enabled) {
        this.isEraserMode = enabled;
    }

    public void setStrokeWidth(float width) {
        this.currentStrokeWidth = width;
    }

    public void undo() {
        if (!paths.isEmpty()) {
            undonePaths.add(paths.remove(paths.size() - 1));
            invalidate();
        }
    }

    public void clear() {
        paths.clear();
        undonePaths.clear();
        invalidate();
    }

    public Bitmap getBitmap() {
        Bitmap result = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        // Ensure background is transparent or white for export
        canvas.drawColor(Color.WHITE);
        draw(canvas);
        return result;
    }

    private static class DrawPath {
        Path path;
        int color;
        float strokeWidth;
        boolean isEraser;

        DrawPath(Path path, int color, float strokeWidth, boolean isEraser) {
            this.path = path;
            this.color = color;
            this.strokeWidth = strokeWidth;
            this.isEraser = isEraser;
        }
    }
}
