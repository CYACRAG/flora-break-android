package com.florabreak.app.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.florabreak.app.data.local.BreakEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StressHistoryChartView extends View {

    private final Paint axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final List<BreakEntity> breaks = new ArrayList<>();

    public StressHistoryChartView(Context context) {
        super(context);
        init();
    }

    public StressHistoryChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StressHistoryChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        axisPaint.setColor(android.graphics.Color.parseColor("#C7D1C9"));
        axisPaint.setStrokeWidth(2f);

        linePaint.setColor(android.graphics.Color.parseColor("#2F6B45"));
        linePaint.setStrokeWidth(5f);
        linePaint.setStyle(Paint.Style.STROKE);

        pointPaint.setColor(android.graphics.Color.parseColor("#1E7A43"));
        pointPaint.setStyle(Paint.Style.FILL);

        textPaint.setColor(android.graphics.Color.parseColor("#637568"));
        textPaint.setTextSize(28f);
    }

    public void setBreaks(List<BreakEntity> newBreaks) {
        breaks.clear();

        if (newBreaks != null) {
            breaks.addAll(newBreaks);
        }

        Collections.reverse(breaks);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        int left = 56;
        int right = width - 24;
        int top = 24;
        int bottom = height - 42;

        canvas.drawLine(left, top, left, bottom, axisPaint);
        canvas.drawLine(left, bottom, right, bottom, axisPaint);

        canvas.drawText("10", 8, top + 10, textPaint);
        canvas.drawText("0", 22, bottom + 8, textPaint);

        if (breaks.isEmpty()) {
            canvas.drawText("Noch keine Stressdaten", left + 20, height / 2f, textPaint);
            return;
        }

        if (breaks.size() == 1) {
            float x = (left + right) / 2f;
            float y = scoreToY(breaks.get(0).stressScore, top, bottom);
            canvas.drawCircle(x, y, 9f, pointPaint);
            return;
        }

        float previousX = 0f;
        float previousY = 0f;

        int count = breaks.size();

        for (int i = 0; i < count; i++) {
            BreakEntity item = breaks.get(i);

            float progress = i / (float) (count - 1);
            float x = left + progress * (right - left);
            float y = scoreToY(item.stressScore, top, bottom);

            if (i > 0) {
                canvas.drawLine(previousX, previousY, x, y, linePaint);
            }

            canvas.drawCircle(x, y, 8f, pointPaint);

            previousX = x;
            previousY = y;
        }
    }

    private float scoreToY(int score, int top, int bottom) {
        int clamped = Math.max(0, Math.min(10, score));
        float ratio = clamped / 10f;

        return bottom - ratio * (bottom - top);
    }
}
