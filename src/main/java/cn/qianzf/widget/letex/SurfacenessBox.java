package cn.qianzf.widget.letex;

import org.scilab.forge.jlatexmath.Atom;
import org.scilab.forge.jlatexmath.Box;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class SurfacenessBox extends Box {

    protected final Box[] structureRequires;
    protected float thickness;
    protected float space,innerSpace=0.1F;
    private Color line;
    private Color bg;
    private final boolean sameRequire ;

    private final Box textureDirection;//纹理方向

    private final Box allowanceForMachining;//加工余量

    private final Box manufacturingMethod;//加工方法

    private final Box upLimit;//

    private final Box downLimit;//

    private final String type;//基本符号

    final float triangleW=0.6F;
    double atan30=Math.sqrt(3)/3;
    final float triangleH= (float) (triangleW/2/atan30);
    float structureRequiresH=0,leftW=0,rightW=0,leftH=0;

    public SurfacenessBox(Box[] structureRequires, float thickness, float space, boolean sameRequire,Box textureDirection,
                          Box allowanceForMachining,Box manufacturingMethod,Box upLimit,Box downLimit,String type) {
        this.structureRequires = structureRequires;
        // this.depth = box.depth + thickness + space;
        //this.shift = box.shift;
        this.thickness = thickness;
        this.space = space;
        this.sameRequire = sameRequire;
        this.textureDirection=textureDirection;
        this.allowanceForMachining=allowanceForMachining;
        this.manufacturingMethod=manufacturingMethod;
        this.type=type;
        this.upLimit=upLimit;
        this.downLimit=downLimit;
        float hTemp=calHeight();
        float wTemp=calWidth();
        this.width = wTemp + 2 * thickness + 2 * space;
        this.height = hTemp + thickness + innerSpace;
    }

    public SurfacenessBox(Box[] box, float thickness, float space, Color line, Color bg, boolean sameRequire,Box textureDirection,
                          Box allowanceForMachining,Box manufacturingMethod,Box upLimit,Box downLimit,String type) {
        this(box, thickness, space, sameRequire,textureDirection,
                allowanceForMachining,manufacturingMethod,upLimit,downLimit,type);
        this.line = line;
        this.bg = bg;
    }

    private float calHeight(){
        if(structureRequires!=null && structureRequires.length>0){
            for(Box b:structureRequires){
                structureRequiresH=structureRequiresH+b.getHeight()+innerSpace;
            }
        }
        //textureDirection 纹理方向文字认为只有一行，不高
        //allowanceForMachining;//加工余量文字认为只有一行，不高
        if(upLimit!=null){
            leftH=leftH+upLimit.getHeight();
        }
        if(downLimit!=null){
            if(leftH>0){
                leftH=leftH+innerSpace;
            }
            leftH=leftH+downLimit.getHeight();
        }
        float tth=triangleH;
        if(allowanceForMachining!=null){
            tth=Math.max(tth,allowanceForMachining.getHeight());
        }
        if(leftH==0){
            leftH=1;
        }else{
            leftH=leftH+tth+innerSpace+innerSpace;
        }

        leftH=Math.max(leftH,structureRequiresH);

        if(manufacturingMethod!=null){
            return leftH+manufacturingMethod.getHeight();
        }else{
            return leftH;
        }
    }

    private float calWidth(){
        leftW=triangleW;
        if(allowanceForMachining!=null){
            leftW=leftW+allowanceForMachining.getWidth();
        }
        if(upLimit!=null){
            leftW=Math.max(leftW,upLimit.getWidth());
        }
        if(downLimit!=null){
            leftW=Math.max(leftW,downLimit.getWidth());
        }

        float rw= (float) (leftH*atan30-triangleW/2);//右边斜线上半段宽度


        if(structureRequires!=null && structureRequires.length>0){
            for(Box b:structureRequires){
                rightW=Math.max(rightW,b.getWidth());
            }
        }

        if(manufacturingMethod!=null){
            rightW=Math.max(rightW,manufacturingMethod.getWidth());
        }

        return leftW+rw+rightW;
    }

    public void draw(Graphics2D g2,final float x,final float y) {
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

        float left=x+space;
        float tth=triangleH;
        if(allowanceForMachining!=null){
            allowanceForMachining.draw(g2,left+(leftW-allowanceForMachining.getWidth()-triangleW),y);
            tth=Math.max(tth,allowanceForMachining.getHeight());
        }
        float topX=  (float) (left+(leftW)+leftH*Math.sqrt(3)/3-triangleW/2);
        Line2D line2D=new Line2D.Float(left+(leftW-triangleW/2),y,left+(leftW-triangleW),y-triangleH);//左边斜线
        g2.draw(line2D);
        line2D=new Line2D.Float(left+(leftW-triangleW/2),y,topX,y-leftH);//右边斜线
        g2.draw(line2D);

        if("2".equals(type)){
            line2D=new Line2D.Float(left+(leftW-triangleW),y-triangleH,left+leftW,y-triangleH);
            g2.draw(line2D);
        }else if("3".equals(type)){
            Ellipse2D circle = new Ellipse2D.Float(left+(leftW-triangleW)+0.15F,y - triangleH-0.01F,triangleW*0.5F,triangleW*0.5F);
            g2.draw(circle);
        }

        if(sameRequire){
            float r=triangleW*0.6F;
            Ellipse2D circle = new Ellipse2D.Float( topX-r/2,y-leftH-r/2,r,r);
            g2.draw(circle);
        }
        float tempY=tth+innerSpace;
        if(downLimit!=null){
            downLimit.draw(g2,left+leftW-downLimit.getWidth(),y-tempY);
            tempY=tempY+downLimit.getHeight()+innerSpace;
        }else{
            tempY=tempY+0.2F;
        }
        if(upLimit!=null){
            upLimit.draw(g2,left+leftW-upLimit.getWidth(),y-tempY);
        }
        if(textureDirection!=null){
            //纹理方向
            textureDirection.draw(g2,left+leftW-triangleW/2+2*innerSpace,y);
        }
        float topX2=x+getWidth()-innerSpace;
        if(manufacturingMethod!=null){
            manufacturingMethod.draw(g2,topX+(topX2-topX-manufacturingMethod.getWidth())/2,y-leftH-innerSpace);
        }

        if(manufacturingMethod!=null || structureRequiresH>0){
            //上面横线
            line2D=new Line2D.Float(topX,y-leftH,topX2,y-leftH);
            g2.draw(line2D);
        }
        float srSpace=( leftH-structureRequiresH)/structureRequires.length;
        tempY=y-leftH+thickness+srSpace;
        if(structureRequiresH>0){
            for(Box b:structureRequires){
                b.draw(g2,topX+(topX2-topX-b.getWidth())/2,tempY+b.getHeight());
                tempY=tempY+b.getHeight()+srSpace;
            }
        }

        g2.setColor(prev);
        g2.setStroke(st);
    }

    public int getLastFontId() {
        return 0;
    }
}
