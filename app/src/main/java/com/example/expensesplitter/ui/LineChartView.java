package com.example.expensesplitter.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import java.util.ArrayList;
import java.util.List;

public class LineChartView extends View {
    private Paint linePaintExpense, linePaintSettlement, dotPaint, gridPaint;
    private List<Float> expenseData = new ArrayList<>();
    private List<Float> settlementData = new ArrayList<>();
    private float animationProgress = 0f;
    
    private int maxPoints = 7; // সর্বোচ্চ ৭ দিনের ডেটা দেখাবে

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaintExpense = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaintExpense.setColor(Color.parseColor("#FF5252")); // লাল (খরচ)
        linePaintExpense.setStyle(Paint.Style.STROKE);
        linePaintExpense.setStrokeWidth(6f);

        linePaintSettlement = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaintSettlement.setColor(Color.parseColor("#4CAF50")); // সবুজ (শোধ)
        linePaintSettlement.setStyle(Paint.Style.STROKE);
        linePaintSettlement.setStrokeWidth(6f);

        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setStyle(Paint.Style.FILL);

        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(Color.parseColor("#E0E0E0"));
        gridPaint.setStrokeWidth(2f);
    }

    public void setData(List<Float> expenses, List<Float> settlements) {
        this.expenseData = expenses;
        this.settlementData = settlements;
        animateChart();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (expenseData.isEmpty() && settlementData.isEmpty()) return;

        float width = getWidth();
        float height = getHeight();
        float padding = 50f;

        // গ্রিড লাইন আঁকা
        for (int i = 0; i <= 4; i++) {
            float y = padding + (i * (height - 2 * padding) / 4);
            canvas.drawLine(padding, y, width - padding, y, gridPaint);
        }

        float maxVal = 1f;
        for (float v : expenseData) if (v > maxVal) maxVal = v;
        for (float v : settlementData) if (v > maxVal) maxVal = v;

        float xStep = (width - 2 * padding) / Math.max(1, maxPoints - 1);

        // ১. খরচের লাইন আঁকা (লাল)
        drawLine(canvas, expenseData, xStep, maxVal, height, padding, linePaintExpense, Color.parseColor("#FF5252"));

        // ২. শোধের লাইন আঁকা (সবুজ)
        drawLine(canvas, settlementData, xStep, maxVal, height, padding, linePaintSettlement, Color.parseColor("#4CAF50"));
    }

    private void drawLine(Canvas canvas, List<Float> data, float xStep, float maxVal, float height, float padding, Paint paint, int dotColor) {
        if (data.isEmpty()) return;
        Path path = new Path();
        dotPaint.setColor(dotColor);

        for (int i = 0; i < data.size(); i++) {
            float x = padding + (i * xStep);
            // Y-অক্ষ হিসাব (উপর থেকে নিচে)
            float y = height - padding - ((data.get(i) / maxVal) * (height - 2 * padding) * animationProgress);

            if (i == 0) path.moveTo(x, y);
            else path.lineTo(x, y);

            // ডট আঁকা
            canvas.drawCircle(x, y, 8f, dotPaint);
        }
        canvas.drawPath(path, paint);
    }

    public void animateChart() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(1500);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            animationProgress = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }
}
