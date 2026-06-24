package com.florabreak.app.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class StressGaugeView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float score = 7.6f;

    public StressGaugeView(Context context) {
        super(context);
    }

    public StressGaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScore(float score) {
        this.score = score;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float strokeWidth = 22f;
        float padding = 28f;

        float width = getWidth();
        float height = getHeight();

        RectF arcRect = new RectF(
                padding,
                padding,
                width - padding,
                height * 2f - padding
        );

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);

        // Hintergrundbogen
        paint.setColor(0xFFE3E9E1);
        canvas.drawArc(arcRect, 180, 180, false, paint);

        // Grün
        paint.setColor(0xFF8BCB8F);
        canvas.drawArc(arcRect, 180, 60, false, paint);

        // Gelb
        paint.setColor(0xFFF3C96B);
        canvas.drawArc(arcRect, 240, 60, false, paint);

        // Rot
        paint.setColor(0xFFE8787E);
        canvas.drawArc(arcRect, 300, 60, false, paint);

        // Zeigerpunkt
        float angle = 180f + (score / 10f) * 180f;
        double radians = Math.toRadians(angle);

        float radiusX = arcRect.width() / 2f;
        float radiusY = arcRect.height() / 2f;
        float centerX = arcRect.centerX();
        float centerY = arcRect.centerY();

        float dotX = centerX + (float) Math.cos(radians) * radiusX;
        float dotY = centerY + (float) Math.sin(radians) * radiusY;

        paint.setStyle(Paint.Style.FILL);

        paint.setColor(0xFFFFFFFF);
        canvas.drawCircle(dotX, dotY, 15f, paint);

        paint.setColor(0xFFE8787E);
        canvas.drawCircle(dotX, dotY, 10f, paint);
    }
}