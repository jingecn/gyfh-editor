package cn.qianzf.widget.letex;

import org.scilab.forge.jlatexmath.*;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class SpecialCharBox extends Box {

    private final CharFont cf;
    private final float size;
    private Font font;
    public SpecialCharBox(Char c,Font font) {
        cf = c.getCharFont();
        size = c.getMetrics().getSize();
        width = c.getWidth();
        height = c.getHeight();
        depth = c.getDepth();
        this.font=font;
    }

    @Override
    protected void startDraw(Graphics2D g2, float x, float y) {
        setWidth(g2.getFontMetrics(font).charWidth(cf.c));
        super.startDraw(g2, x, y);
    }

    @Override
    public void draw(Graphics2D g2, float x, float y) {
        AffineTransform at = g2.getTransform();
        g2.translate(x, y);
//        if (Math.abs(size - TeXFormula.FONT_SCALE_FACTOR) > 0.0000001f) {
//            g2.scale(size / TeXFormula.FONT_SCALE_FACTOR,
//                    size / TeXFormula.FONT_SCALE_FACTOR);
//        }
        Font oldFont=g2.getFont();
        g2.setFont(font);
        char[] arr = new char[1];
        arr[0] = cf.c;
        g2.drawChars(arr, 0, 1, 0, 0);
        g2.setTransform(at);
        g2.setFont(oldFont);
    }

    @Override
    public int getLastFontId() {
        return 0;
    }
}
