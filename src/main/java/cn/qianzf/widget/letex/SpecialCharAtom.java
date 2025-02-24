package cn.qianzf.widget.letex;

import org.scilab.forge.jlatexmath.*;

import java.awt.*;

public class SpecialCharAtom extends Atom {

   private Font font;

   private char c;

    public SpecialCharAtom(Font font, char c) {
        this.font = font;
        this.c = c;
    }

    @Override
    public Box createBox(TeXEnvironment teXEnvironment) {
        font=font.deriveFont(teXEnvironment.getScaleFactor());
        Metrics m=new Metrics(1F,0.9F,0.0F,0,teXEnvironment.getScaleFactor(), teXEnvironment.getSize() );
        Char theChar=new Char(c,font,font.hashCode(),m);
        SpecialCharBox charBox=new SpecialCharBox(theChar,font);//teXEnvironment.getTeXFont().getSize()
        return charBox;
    }
}
