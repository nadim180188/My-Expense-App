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
import com.example.expensesplitter.model.CategorySum;
import java.util.List;

public class CategoryPieChartView extends View {
    private Paint paint;
    private RectF rectF;
    private float sweepAngle = 0f;
    private List<CategorySum> data;
    private int[] colors = {
            Color.parseColor("#6C63FF"), // বেগুনি
            Color.parseColor("#FF5252"), // লাল
            Color.parseColor("#FFC107"), // হলুদ
            Color.parseColor("#4CAF50"), // সবুজ
            Color.parseColor("#2196F3")  // নীল
    };

    public CategoryPieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        rectF = new RectF();
    }

    public void setData(List<CategorySum> data) {
        this.data = data;
        animateChart();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data == null || data.isEmpty()) return;

        float width = getWidth();
        float height = getHeight();
        float radius = Math.min(width, height) / 3.2f;
        float strokeWidth = radius * 0.35f; // ডোনাট চার্টের জন্য

        rectF.set(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius);
        paint.setStrokeWidth(strokeWidth);

        double total = 0;
        for (CategorySum c : data) total += c.totalAmount;
        if (total == 0) total = 1;

        float startAngle = -90f;
        for (int i = 0; i < data.size(); i++) {
            float sweep = (float) ((data.get(i).totalAmount / total) * 360f * (sweepAngle / 360f));
            paint.setColor(colors[i % colors.length]);
            canvas.drawArc(rectF, startAngle, sweep, false, paint);
            startAngle += sweep;
        }
    }

    public void animateChart() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 360f);
        animator.setDuration(1200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            sweepAngle = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }
}
