package cn.qianzf.widget.letex;

import org.scilab.forge.jlatexmath.Box;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class TriangledBox extends Box {

    private final Box child;

    protected Color bg = null, line = null;

    private float thickness;

    private float upDownSpace=0.5F,leftSpace=0.1F,rightSpace=1;

    public TriangledBox(Box child, Color bg, Color line, float thickness ) {
        this.child=child;
        this.height=2*upDownSpace+child.getHeight()+thickness*2;
        this.width=thickness*2+child.getWidth()+leftSpace+rightSpace;
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
        Line2D left=new Line2D.Float(x,y,x,y-getHeight());
        g2.draw(left);
        Line2D up=new Line2D.Float(x,y,x+getWidth(),y-getHeight()/2);
        g2.draw(up);
        Line2D down=new Line2D.Float(x,y-getHeight(),x+getWidth(),y-getHeight()/2);
        g2.draw(down);
        child.draw(g2,x+leftSpace,y-(getHeight()-child.getHeight())/2);

        g2.setColor(prev);
        //drawDebug(g2, x, y);
        g2.setStroke(st);
    }

    @Override
    public int getLastFontId() {
        return 0;
    }
}
