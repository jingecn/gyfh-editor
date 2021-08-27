package cn.qianzf.widget.letex;

import org.scilab.forge.jlatexmath.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SurfacenessAtom extends Atom {

    public float INTERSPACE = 0.35f;

    protected Color bg = null, line = null;

    private boolean sameRequire = false;

    private Atom textureDirection;//纹理方向

    private Atom allowanceForMachining;//加工余量

    private Atom manufacturingMethod;//加工方法

    private Atom upLimit;//

    private Atom downLimit;//

    private List<Atom> structureRequires = new ArrayList<>();//表面结构要求

    private String type;//基本符号

    public SurfacenessAtom(final TeXParser tp, String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("arguments length must be gt 1 ! " +
                    "{基本符号}{相同要求}{上限值}{下限值}{加工余量}{纹理方向}{加工方法}{表面结构要求1}{表面结构要2}" +
                    "eq. \\surfaceness{1}{0}{}{}{}{}{}");
        }
        type=args[1];
        sameRequire = "1".equals(args[2]);
        upLimit=needAtom(args[3],tp);
        downLimit=needAtom(args[4],tp);
        allowanceForMachining=needAtom(args[5],tp);
        textureDirection=needAtom(args[6],tp);
        manufacturingMethod=needAtom(args[7],tp);
        for(int i=8;i<args.length;i++){
            Atom a=needAtom(args[i],tp);
            if(a!=null){
                structureRequires.add(a);
            }
        }
    }

    private Atom needAtom(String str,final TeXParser tp){
        if(str!=null && str.length()>0){
            return  new ExtTeXFormula(tp, "\\small{"+str+"}", false).root;
        }
        return null;
    }

    @Override
    public Box createBox(TeXEnvironment env) {
        Box[] childBoxes=new Box[structureRequires.size()];
        for(int i=0;i<childBoxes.length;i++){
            childBoxes[i]=createBox(structureRequires.get(i),env);
        }
        float drt = env.getTeXFont().getDefaultRuleThickness(env.getStyle());
        float space = INTERSPACE * SpaceAtom.getFactor(TeXConstants.UNIT_EM, env);
        SurfacenessBox surfacenessBox=null;
        if (bg == null) {
            surfacenessBox= new SurfacenessBox(childBoxes, drt, space, sameRequire,createBox(textureDirection,env),
                    createBox(allowanceForMachining,env),createBox(manufacturingMethod,env),createBox(upLimit,env),
                    createBox(downLimit,env),type);
        } else {
            env.isColored = true;
            surfacenessBox= new SurfacenessBox(childBoxes, drt, space, line, bg, sameRequire,createBox(textureDirection,env),
                    createBox(allowanceForMachining,env),createBox(manufacturingMethod,env),createBox(upLimit,env),
                    createBox(downLimit,env),type);
        }
        return surfacenessBox;
    }

    private Box createBox(Atom atom,TeXEnvironment env){
        if(atom!=null){
            return atom.createBox(env);
        }
        return null;
    }
}
