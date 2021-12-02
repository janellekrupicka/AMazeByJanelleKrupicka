package edu.wm.cs.cs301.janellekrupicka.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MazePanel extends View {
    private static Bitmap bitmap;
    private static Canvas canvas;
    private static Paint paintTest;
    public MazePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        bitmap = bitmap.createBitmap(getContext().getResources().getDisplayMetrics(), 400, 400, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paintTest = new Paint();

    }
    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(1000, 1000);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        paintTest.setColor(Color.RED);
        canvas.drawRect(new Rect(10, 10, 500, 700), paintTest);
    }

}
