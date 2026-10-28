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
    private float sweepAngle = 0f;
    private float[] values = {50f, 50f}; // ডিফল্ট
    private int[] colors = {Color.parseColor("#6C63FF"), Color.parseColor("#FF5252")}; // বেগুনি (Paid) এবং লাল (Due)

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        rectF = new RectF();
    }

    // গ্রাফে আসল ডেটা পাঠানোর মেথড
    public void setData(float paidAmount, float dueAmount) {
        if (paidAmount == 0 && dueAmount == 0) {
            values = new float[]{100f}; // কিছু না থাকলে ফাঁকা দেখাবে না
            colors = new int[]{Color.parseColor("#E0E0E0")};
        } else {
            values = new float[]{paidAmount, dueAmount};
            colors = new int[]{Color.parseColor("#6C63FF"), Color.parseColor("#FF5252")};
        }
        animateChart();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();
        float radius = Math.min(width, height) / 2.5f;
        
        rectF.set(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius);

        // ভেতরে সাদা গোল তৈরি করা (Donut Chart)
        float strokeWidth = radius * 0.4f;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        
        float total = 0;
        for (float v : values) total += v;
        if (total == 0) total = 1;

        float startAngle = -90f;
        for (int i = 0; i < values.length; i++) {
            float sweep = (values[i] / total) * 360f * (sweepAngle / 360f);
            paint.setColor(colors[i]);
            canvas.drawArc(rectF, startAngle, sweep, false, paint);
            startAngle += sweep;
        }
    }

    public void animateChart() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 360f);
        animator.setDuration(1500);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            sweepAngle = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }
}
