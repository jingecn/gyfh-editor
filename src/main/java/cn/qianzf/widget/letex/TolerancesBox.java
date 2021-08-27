package cn.qianzf.widget.letex;

import org.scilab.forge.jlatexmath.Box;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class TolerancesBox extends Box {

    protected Box[] box;
    protected float thickness;
    protected float space;
    private Color line;
    private Color bg;
    private float qzSpace=0;
    private boolean quanZhou = false;

    public TolerancesBox(Box[] box, float thickness, float space, boolean quanZhou) {
        this.box = box;
        float w = 0, h = 1;
        if (box != null) {
            for(int i=0;i<box.length;i++){
                h = Math.max(h, box[i].getHeight());
                w = w + box[i].getWidth();
                if(i<box.length-1){
                    w=w+space;
                }
            }
        }
        if (quanZhou) {
            qzSpace=h*0.96F / 2;
            w = w + qzSpace;
        } else if (w == 0) {
            w = h;
        }
        this.width = w + 2 * thickness + 1 * space+0.2F;
        this.height = h + thickness + space;
        // this.depth = box.depth + thickness + space;
        //this.shift = box.shift;
        this.thickness = thickness;
        this.space = space;
        this.quanZhou = quanZhou;
    }

    public TolerancesBox(Box[] box, float thickness, float space, Color line, Color bg, boolean quanZhou) {
        this(box, thickness, space, quanZhou);
        this.line = line;
        this.bg = bg;
    }

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

        if(quanZhou){
            g2.draw(new Rectangle2D.Float(x + th+qzSpace, y - height + th, width - thickness-qzSpace, height + depth - thickness));
            Ellipse2D circle = new Ellipse2D.Float(x + th,y - (height )/2-qzSpace,qzSpace*2,qzSpace*2);
            g2.draw(circle);
        }else {
            g2.draw(new Rectangle2D.Float(x + th, y - height + th, width - thickness, height + depth - thickness));
        }
        //box.draw(g2, x + space + thickness, y);
        if (box != null) {
            float left=x + 0.2F + thickness+qzSpace;
            for(int i=0;i<box.length;i++){
                box[i].draw(g2, left, y- (height -  box[i].getHeight())/2);
                left=left+ box[i].getWidth();
                if(i<box.length-1){
                    Line2D line2D=new Line2D.Float(left+space/2,y - height + th,left+space/2,y);
                    g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
                    g2.draw(line2D);
                    left=left+space;
                    g2.setStroke(st);
                }
            }
        }
        g2.setColor(prev);
        //drawDebug(g2, x, y);
        g2.setStroke(st);
    }

    public int getLastFontId() {
        if (box != null && box.length > 0) {
            return box[box.length - 1].getLastFontId();
        }
        return 0;
    }
}
