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
import java.util.Locale;

public class PieChartView extends View {
    private Paint paint;
    private Paint textPaint;
    private Paint subTextPaint;
    private RectF rectF;
    private float sweepAngle = 0f;
    private float[] values = {50f, 50f}; // ডিফল্ট
    private int[] colors = {Color.parseColor("#6C63FF"), Color.parseColor("#FF5252")}; // বেগুনি (Paid) এবং লাল (Due)
    private float totalDue = 0f; // মাঝখানে দেখানোর জন্য

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        rectF = new RectF();

        // মাঝখানের বড় টেক্সট (টাকার পরিমাণ)
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#2D2D2D"));
        textPaint.setTextSize(65f); // ফন্ট সাইজ
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);

        // মাঝখানের ছোট টেক্সট ("Due")
        subTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        subTextPaint.setColor(Color.parseColor("#777777"));
        subTextPaint.setTextSize(40f); // ফন্ট সাইজ
        subTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    // গ্রাফে আসল ডেটা পাঠানোর মেথড
    public void setData(float paidAmount, float dueAmount) {
        this.totalDue = dueAmount; // বাকি টাকা সেভ করে রাখলাম
        
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
        
        // চার্টের রেডিয়াস (স্ক্রিনের সাইজ অনুযায়ী)
        float radius = Math.min(width, height) / 3.2f;
        float strokeWidth = radius * 0.35f;
        
        rectF.set(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius);
        paint.setStrokeWidth(strokeWidth);

        float total = 0;
        for (float v : values) total += v;
        if (total == 0) total = 1;

        // ১. গ্রাফ আঁকা
        float startAngle = -90f;
        for (int i = 0; i < values.length; i++) {
            float sweep = (values[i] / total) * 360f * (sweepAngle / 360f);
            paint.setColor(colors[i]);
            canvas.drawArc(rectF, startAngle, sweep, false, paint);
            startAngle += sweep;
        }

        // ২. মাঝখানে টেক্সট আঁকা (অ্যানিমেশন সহ)
        float centerX = width / 2f;
        float centerY = height / 2f;

        // টেক্সটকে ঠিক মাঝখানে বসানোর জন্য Y পজিশন ক্যালকুলেশন
        float textY = centerY - ((textPaint.descent() + textPaint.ascent()) / 2) - 15f;
        float subTextY = centerY + 35f;

        // অ্যানিমেশন অনুযায়ী টাকার পরিমাণ বাড়বে (0 থেকে actual)
        float currentDue = totalDue * (sweepAngle / 360f);
        
        canvas.drawText(String.format(Locale.US, "৳%.0f", currentDue), centerX, textY, textPaint);
        canvas.drawText("Due", centerX, subTextY, subTextPaint);
    }

    public void animateChart() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 360f);
        animator.setDuration(1200); // ১.২ সেকেন্ড
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            sweepAngle = (float) animation.getAnimatedValue();
            invalidate(); // স্ক্রিন রিফ্রেশ করবে
        });
        animator.start();
    }
}
