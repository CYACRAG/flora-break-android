package com.florabreak.app.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

public class StressGaugeView extends View {

    private final Paint basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint thumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float stressScore = 0f;
    private final float maxScore = 10f;

    public StressGaugeView(Context context) {
        super(context);
        init();
    }

    public StressGaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StressGaugeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        basePaint.setStyle(Paint.Style.STROKE);
        basePaint.setStrokeCap(Paint.Cap.ROUND);
        basePaint.setStrokeWidth(22f);
        basePaint.setColor(Color.parseColor("#E6ECE7"));

        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(22f);

        thumbPaint.setStyle(Paint.Style.FILL);
        updateThumbColor();
    }

    public void setStressScore(float value) {
        stressScore = Math.max(0f, Math.min(value, maxScore));
        updateThumbColor();
        invalidate();
    }

    private void updateThumbColor() {
        if (stressScore <= 3f) {
            thumbPaint.setColor(Color.parseColor("#2F9E44"));
        } else if (stressScore <= 6f) {
            thumbPaint.setColor(Color.parseColor("#F2B84B"));
        } else {
            thumbPaint.setColor(Color.parseColor("#E8787E"));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float w = getWidth();
        float h = getHeight();

        float cx = w / 2f;
        float cy = h * 0.80f;
        float radius = Math.min(w * 0.43f, h * 0.60f);

        RectF arcRect = new RectF(
                cx - radius,
                cy - radius,
                cx + radius,
                cy + radius
        );

        float startAngle = 150f;
        float totalSweep = 240f;
        float progressSweep = (stressScore / maxScore) * totalSweep;

        canvas.drawArc(arcRect, startAngle, totalSweep, false, basePaint);

        int[] colors = {
                Color.parseColor("#2F9E44"),
                Color.parseColor("#F2B84B"),
                Color.parseColor("#FF8A00"),
                Color.parseColor("#E8787E")
        };

        float[] positions = {0f, 0.45f, 0.75f, 1f};

        SweepGradient sweepGradient = new SweepGradient(cx, cy, colors, positions);
        Matrix matrix = new Matrix();
        matrix.postRotate(startAngle - 90f, cx, cy);
        sweepGradient.setLocalMatrix(matrix);
        progressPaint.setShader(sweepGradient);

        canvas.drawArc(arcRect, startAngle, progressSweep, false, progressPaint);

        double angleRad = Math.toRadians(startAngle + progressSweep);
        float thumbX = (float) (cx + radius * Math.cos(angleRad));
        float thumbY = (float) (cy + radius * Math.sin(angleRad));

        canvas.drawCircle(thumbX, thumbY, 13f, thumbPaint);
    }
}
