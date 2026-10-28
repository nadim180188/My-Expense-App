package com.example.expensesplitter.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class PieChartView extends View {
    private Paint paint;
    private RectF rectF;
    private float sweepAngle = 0f; // অ্যানিমেশনের জন্য
    private float[] values = {40, 30, 20, 10}; // ডামি ডেটা
    private int[] colors = {
            Color.parseColor("#6C63FF"), 
            Color.parseColor("#FF6584"), 
            Color.parseColor("#FFC107"), 
            Color.parseColor("#4CAF50")
    };

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        rectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();
        float radius = Math.min(width, height) / 2.5f;
        
        // স্ক্রিনের মাঝখানে বসানো
        rectF.set(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius);

        float startAngle = -90f;
        for (int i = 0; i < values.length; i++) {
            float sweep = (values[i] / 100f) * 360f * (sweepAngle / 360f);
            paint.setColor(colors[i]);
            canvas.drawArc(rectF, startAngle, sweep, true, paint);
            startAngle += sweep;
        }
    }

    public void animateChart() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 360f);
        animator.setDuration(1500); // ১.৫ সেকেন্ড অ্যানিমেশন
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            sweepAngle = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }
}
