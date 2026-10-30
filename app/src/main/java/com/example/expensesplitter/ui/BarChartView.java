package com.example.expensesplitter.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import java.util.List;

public class BarChartView extends View {
    private Paint paint;
    private float[] values;
    private String[] labels;
    private float animationProgress = 0f;

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setData(List<Float> dataList, List<String> labelList) {
        values = new float[dataList.size()];
        labels = new String[labelList.size()];
        for (int i = 0; i < dataList.size(); i++) values[i] = dataList.get(i);
        for (int i = 0; i < labelList.size(); i++) labels[i] = labelList.get(i);
        
        animateChart();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (values == null || values.length == 0) return;

        float width = getWidth();
        float height = getHeight();
        float padding = 40f;
        float barWidth = (width - (padding * 2)) / values.length;
        
        float maxVal = 1;
        for (float v : values) if (v > maxVal) maxVal = v;

        paint.setTextSize(30f);
        paint.setTextAlign(Paint.Align.CENTER);

        for (int i = 0; i < values.length; i++) {
            // বারের উচ্চতা হিসাব (অ্যানিমেশন সহ)
            float barHeight = (values[i] / maxVal) * (height - padding * 2) * (animationProgress / 100f);
            float left = padding + (i * barWidth) + (barWidth * 0.2f);
            float right = left + (barWidth * 0.6f);
            float top = height - padding - barHeight;
            float bottom = height - padding;

            // বার আঁকা
            paint.setColor(Color.parseColor("#6C63FF")); // বেগুনি রঙ
            canvas.drawRoundRect(left, top, right, bottom, 10f, 10f, paint);

            // নিচে লেবেল (যেমন: তারিখ বা সপ্তাহ)
            paint.setColor(Color.parseColor("#777777"));
            if (labels != null && i < labels.length) {
                canvas.drawText(labels[i], (left + right) / 2, height - 10f, paint);
            }
        }
    }

    public void animateChart() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 100f);
        animator.setDuration(1200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            animationProgress = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }
}
