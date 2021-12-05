package edu.wm.cs.cs301.janellekrupicka.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.fonts.Font;
import android.util.AttributeSet;
import android.view.View;
import edu.wm.cs.cs301.janellekrupicka.gui.Constants;

import androidx.annotation.Nullable;

public class MazePanel extends View implements P5PanelF21 {
    static final int GREEN_WM = 1136448;
    private static int YELLOW_WM = 1136448;
    static final int GOLD_WM = 9531201;
    static final int WHITE = 16777215;
    static final int LIGHT_GRAY = 13882323;
    private static Bitmap bitmap;
    private static Canvas canvasNotes;
    private static Paint paintTest;
    private static int color;
    private static final int WIDTH = 1200;
    public MazePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    //    myTestImage(canvasNotes);
        commit();
    //    paint(canvasNotes);
    }
 //   public MazePanel() {
 //       super();
 //       init();
 //       commit();
 //   }
    private void init() {
        bitmap = bitmap.createBitmap(getContext().getResources().getDisplayMetrics(), WIDTH, WIDTH, Bitmap.Config.ARGB_8888);
        canvasNotes = new Canvas(bitmap);
    //    paintTest = new Paint();
    }
 //   @Override
 //   public void invalidate() {
 //       this.draw(canvasNotes);
 //   }
    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(WIDTH, WIDTH);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, new Rect(0, 0, WIDTH, WIDTH), null);
    //    myTestImage(canvas);
    //    canvas.setBitmap(bitmap);
    //      canvasUsed = canvas;
    //      canvas.drawColor(Color.WHITE);
    //    canvas = this.canvas;
    //      paintTest.setColor(Color.RED);
    //      canvas.drawRect(new Rect(10, 10, 500, 700), paintTest);
    }
    public void setCanvas(Canvas c) {
        canvasNotes = c;
    //    canvasNotes.setBitmap(bitmap);
    }
    public void setBitmap(Bitmap bm) {
        bitmap = bm;
        canvasNotes.setBitmap(bitmap);
    }
    public void paint(Canvas canvas) {
    //    canvas.drawBitmap(bitmap, new Rect(0, 0, 1000, 1000), new Rect(0, 0, 1000, 1000), null);
    //    invalidate();
    //    if (null == g) {
      //      System.out.println("MazePanel.paint: no graphics object, skipping drawImage operation");
      //  }
      //  else {
        //    g.drawImage(bufferImage,0,0,null);
      //  }
    }
    @Override
    public void commit() {
        invalidate();
    //    canvasNotes.drawBitmap(bitmap, new Rect(0, 0, 1000, 1000), new Rect(0, 0, 1000, 1000), null);
    }

    @Override
    public boolean isOperational() {
        return false;
    //  return(graphics!=null);
    }

    @Override
    public void setColor(int rgb) {
        color = rgb;
    //  currentColor = rgb;
        // construct color with Color(int rgb)
    //  Color colorToSet = new Color(currentColor);
        // set color
    //  graphics.setColor(colorToSet);
    }

    @Override
    public int getColor() {
        return color;
    //  return currentColor;
    }

    @Override
    public void addBackground(float percentToExit) {
    //    System.out.print(getBackgroundColor(percentToExit, true));
        setColor(Color.rgb(135,206,250));
        addFilledRectangle(0, 0, WIDTH, WIDTH);
        setColor(Color.rgb(140,171,131));
    //    setColor(getBackgroundColor(percentToExit, false));
        addFilledRectangle(0, WIDTH/2, WIDTH, WIDTH);
    //  setColor(getBackgroundColor(percentToExit, true));
        // graphics.fillRect(0, 0, viewWidth, viewHeight/2);
    //  addFilledRectangle(0, 0, viewWidth, viewHeight);
        // grey rectangle in lower half of screen
        // graphics.setColor(Color.darkGray);
        // dynamic color setting:
    //  setColor(getBackgroundColor(percentToExit, false));
        // graphics.fillRect(0, viewHeight/2, viewWidth, viewHeight/2);
    //  addFilledRectangle(0, viewHeight/2, viewWidth, viewHeight);
        // USES PRIVATE METHODS FROM MAZEPANEL
    }
    private int getBackgroundColor(float percentToExit, boolean top) {
        return top? blend(YELLOW_WM, GOLD_WM, percentToExit) :
                blend(LIGHT_GRAY, GREEN_WM, percentToExit);
    }
    private int blend(int fstColorInt, int sndColorInt, double weightFstColor) {
    //    Color fstColor = new Color(fstColorInt);
    //    Color sndColor = new Color(sndColorInt);
        if (weightFstColor < 0.1)
            return sndColorInt;
        if (weightFstColor > 0.95)
            return fstColorInt;
        double r = weightFstColor * Color.red(fstColorInt) + (1-weightFstColor) * Color.red(sndColorInt);
        double g = weightFstColor * Color.green(fstColorInt) + (1-weightFstColor) * Color.green(sndColorInt);
        double b = weightFstColor * Color.blue(fstColorInt) + (1-weightFstColor) * Color.blue(sndColorInt);
        double a = Math.max(Color.alpha(fstColorInt), Color.alpha(sndColorInt));
        System.out.println(calculateRGB((int) r, (int) g, (int) b));
        return calculateRGB((int) r, (int) g, (int) b);
    }
    /**
     * Calculates the rgb int value based on r, g, and b
     */
    private int calculateRGB(int r, int g, int b) {
        return 65536*r+256*g+b;
    }
    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        Paint fillRectPaint = new Paint();
        fillRectPaint.setColor(color);
        Rect rect = new Rect(x, y, x+width, y+height);
        canvasNotes.drawRect(rect, fillRectPaint);
    //   graphics.fillRect(x, y, width, height/2);
    }

    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        Paint fillPathPaint = new Paint();
        fillPathPaint.setStyle(Paint.Style.FILL);
        fillPathPaint.setColor(color);
        Path fillPath = new Path();
        fillPath.moveTo(xPoints[0], yPoints[0]);
        for(int pnt=1; pnt<nPoints; pnt++) {
            fillPath.lineTo(xPoints[pnt], yPoints[pnt]);
        }
        canvasNotes.drawPath(fillPath, fillPathPaint);
    //    graphics.fillPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        Paint fillPathPaint = new Paint();
        fillPathPaint.setStyle(Paint.Style.STROKE);
        fillPathPaint.setColor(color);
        Path fillPath = new Path();
        fillPath.moveTo(xPoints[0], yPoints[0]);
        for(int pnt=1; pnt<nPoints; pnt++) {
            fillPath.lineTo(xPoints[pnt], yPoints[pnt]);
        }
        canvasNotes.drawPath(fillPath, fillPathPaint);

    //    graphics.drawPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public void addLine(int startX, int startY, int endX, int endY) {
        Paint linePaint = new Paint();
        linePaint.setColor(color);
        canvasNotes.drawLine(startX, startY, endX, endY, linePaint);
    //    graphics.drawLine(startX, startY, endX, endY);
    }

    @Override
    public void addFilledOval(int x, int y, int width, int height) {
        Paint fillOvalPaint = new Paint();
        fillOvalPaint.setColor(color);
        fillOvalPaint.setStyle(Paint.Style.FILL);
        RectF fillOval = new RectF(x, y, x+width, y+height);
        canvasNotes.drawOval(fillOval, fillOvalPaint);
    //    graphics.fillOval(x, y,  width, height);
    }

    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        Paint arcPaint = new Paint();
        arcPaint.setColor(color);
        arcPaint.setStyle(Paint.Style.STROKE);
        RectF arc = new RectF(x, y, x+width, y+height);
        canvasNotes.drawArc(arc, startAngle, arcAngle, false, arcPaint);
    //    graphics.drawArc(x, y, width, height, startAngle, arcAngle);
    }
    @Override
    public void addMarker(float x, float y, String str) {
        Paint markerPaint = new Paint();
        markerPaint.setColor(color);
        markerPaint.setTextSize(16);
        canvasNotes.drawText(str, x, y, markerPaint);
        //    int[] glyphID = {0};
    //    float[] positions = {x, y};
    //    Font font = new Font("serif", Graphics.Font.PlAIN, 16);
    //    canvasNotes.drawGlyphs(glyphID, null, positions, null, null, font, markerPaint);
    //    Font markerFont = Font.decode("Serif-PLAIN-16");
    //    GlyphVector gv = markerFont.createGlyphVector(graphics.getFontRenderContext(), str);
    //    Rectangle2D rect = gv.getVisualBounds();
        // need to update x, y by half of rectangle width, height
        // to serve as x, y coordinates for drawing a GlyphVector
    //    x -= rect.getWidth() / 2;
    //    y += rect.getHeight() / 2;

    //    graphics.drawGlyphVector(gv, x, y);
    }

    @Override
    public void setRenderingHint(P5RenderingHints hintKey, P5RenderingHints hintValue) {
    //    switch(hintKey) {
    //        case KEY_RENDERING:
    //            assert(hintValue==P5RenderingHints.VALUE_RENDER_QUALITY);
    //            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    //            break;
    //        case KEY_ANTIALIASING:
    //            assert(hintValue==P5RenderingHints.VALUE_ANTIALIAS_ON);
    //            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    //            break;
    //        case KEY_INTERPOLATION:
    //            assert(hintValue==P5RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    //            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    //            break;
    //        default:
    //            break;
    //    }
    }
    private void myTestImage(Canvas c) {
        setCanvas(c);
        addBackground(100);
        setColor(Color.RED);
        addFilledRectangle(0, 0, 400, 400);
    // setColor(Color.RED);
        setColor(Color.CYAN);
        addFilledRectangle(100, 100, 100, 70);
        setColor(Color.LTGRAY);
        int[] xPoints = {700, 900, 625};
        int[] yPoints = {700, 850, 1000};
        addFilledPolygon(xPoints, yPoints, 3);
        setColor(Color.BLACK);
        int[] xPoints1 = {600, 800, 525};
        int[] yPoints1 = {600, 750, 900};
        addPolygon(xPoints1, yPoints1, 3);
        addLine(4, 350, 700, 500);
        setColor(Color.GREEN);
        addFilledOval(250, 250, 300, 300);
        setColor(Color.WHITE);
        addArc(300, 25, 300, 400, 100, 270);
        addMarker(800, 300, "E");
    }

}
