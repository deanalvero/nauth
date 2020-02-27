package com.lowbottgames.nauth.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class DirectionView extends View {

    private int height, width = 0;
    private int padding = 0;
    private int fontSize = 0;
    private int radius = 0;
    private Paint paint;
    private boolean isInit;
    private int numbers = 36;
    private Rect rect = new Rect();

    public DirectionView(Context context) {
        super(context);
    }

    public DirectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DirectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initUI() {
        height = getHeight();
        width = getWidth();
        padding = 50;
        fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13,
                getResources().getDisplayMetrics());
        int min = Math.min(height, width);
        radius = min / 2 - padding;
        paint = new Paint();
        isInit = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit) {
            initUI();
        }

        canvas.drawColor(Color.WHITE);
        drawCircle(canvas);
        paint.setStyle(Paint.Style.FILL);
        drawNumbers(canvas);
        drawDividers(canvas);

        postInvalidateDelayed(500);
        invalidate();
    }

    private void drawNumbers(Canvas canvas) {
        paint.setTextSize(fontSize);

        for (int i = 0; i < numbers; i++) {
            canvas.save();

            String tmp = String.valueOf(i);
            paint.getTextBounds(tmp, 0, tmp.length(), rect);

            double division = (2 * Math.PI) / numbers;
            double angle = (i * division - (Math.PI / 2)) + (division/2);
            int x = (int) (width / 2 + Math.cos(angle) * radius - rect.width() / 2);
            int y = (int) (height / 2 + Math.sin(angle) * radius + rect.height() / 2);
//            canvas.rotate(i * (360/ numbers), x, y);
            canvas.drawText(tmp, x, y, paint);

            canvas.restore();
        }
    }
    private void drawDividers(Canvas canvas) {
        paint.setStrokeWidth(3);
        for (int i = 0; i < numbers; i++) {
//            double angle = ((i * 2 * Math.PI) / numbers) - (Math.PI) - (Math.PI / 2);
            double angle = ((i * 2 * Math.PI) / numbers) - (Math.PI / 2);
            int handRadius = (int) (radius * 1.1f);
            canvas.drawLine(width / 2, height / 2,
                    (float) (width / 2 + Math.cos(angle) * handRadius),
                    (float) (height / 2 + Math.sin(angle) * handRadius),
                    paint);
        }
    }

    private void drawCircle(Canvas canvas) {
        paint.reset();
        paint.setColor(getResources().getColor(android.R.color.black));
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, height / 2, radius + padding - 10, paint);
    }

}
