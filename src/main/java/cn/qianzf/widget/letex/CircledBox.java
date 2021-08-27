package cn.qianzf.widget.letex;

import org.scilab.forge.jlatexmath.Box;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class CircledBox extends Box {

    private final Box child;

    protected Color bg = null, line = null;

    float thickness;

    public CircledBox(Box child, Color bg,Color line,float thickness ) {
        this.child=child;
        float r= (float) Math.sqrt(child.getHeight()*child.getHeight()+child.getWidth()*child.getWidth());
        this.height=r;
        this.width=r;
        this.bg=bg;
        this.line=line;
        this.thickness=thickness;
    }



    @Override
    public void draw(Graphics2D g2, float x, float y) {
        Stroke st = g2.getStroke();
        g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
        float th = thickness / 2;
        if (bg != null) {
            Color prev = g2.getColor();
            g2.setColor(bg);
            g2.fill(new Rectangle2D.Float(x + th, y - height + th, width - thickness, height + depth - thickness));
            g2.setColor(prev);
        }
        Color prev = g2.getColor();
        if (line != null) {
            g2.setColor(line);
        }
        Ellipse2D circle = new Ellipse2D.Float(x,y-getHeight(),getWidth(),getHeight());
        g2.draw(circle);

        child.draw(g2,x+(getWidth()-child.getWidth())/2,y-(getHeight()-child.getHeight())/2);

        g2.setColor(prev);
        //drawDebug(g2, x, y);
        g2.setStroke(st);
    }

    @Override
    public int getLastFontId() {
        return 0;
    }
}
