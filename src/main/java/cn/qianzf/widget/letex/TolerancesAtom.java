package cn.qianzf.widget.letex;


import org.scilab.forge.jlatexmath.*;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 形位公差
 */
public class TolerancesAtom extends Atom {

    private float INTERSPACE = 0.55f;

    protected Color bg = null, line = null;

    private boolean quanZhou = false;

    private  List<Atom> childs=new ArrayList<>();

    /**
     * a	倾斜度
     * b	垂直度
     * c	平面度
     * d	面轮廓度
     * e	圆度
     * f	平行度
     * g	圆柱度
     * h	圆跳动
     * i	对称度
     * j	位置度
     * k	线轮廓度
     * r	同轴度
     * t	全跳动
     * u	直线度
     */
    public static Font gdtFont;

    static {
        try {
            java.net.URL url= TolerancesAtom.class.getResource("/GDT.ttf");
            InputStream is=url.openStream();
            gdtFont=Font.createFont(Font.TRUETYPE_FONT,is);
            is.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TolerancesAtom(final TeXParser tp, String[] args) {
        if (args.length < 7) {
            throw new RuntimeException("arguments length must be 6 ! " +
                    "{是否全周符号}{公差代号}{公差数值、相关原则、可逆要求}{基准一}{基准二}{基准三}." +
                    "eq. \\tolerances{1}{}{}{}{}{}");
        }
        quanZhou = "1".equals(args[1]);
        for(int i=2;i<7;i++){
            String arg=args[i];
            if(arg!=null && arg.trim().length()>0){
                char firstChar=arg.charAt(0);
                if(i==2 && (firstChar>='a' && firstChar<='z')){
                    SpecialCharAtom specialCharAtom=new SpecialCharAtom(gdtFont.deriveFont(1.0F),firstChar);
                    childs.add(specialCharAtom);
                    continue;
                }
                childs.add(new ExtTeXFormula(tp, arg, false).root);
            }
        }
    }

    @Override
    public Box createBox(TeXEnvironment env) {
        Box[] childBoxes = new Box[childs.size()];
        for(int i=0;i<childBoxes.length;i++){
            childBoxes[i]=childs.get(i).createBox(env);
        }
        float drt = env.getTeXFont().getDefaultRuleThickness(env.getStyle());
        float space = INTERSPACE * SpaceAtom.getFactor(TeXConstants.UNIT_EM, env);
        if (bg == null) {
            return new TolerancesBox(childBoxes, drt, space,quanZhou);
        } else {
            env.isColored = true;
            return new TolerancesBox(childBoxes, drt, space, line, bg,quanZhou);
        }
    }

}
