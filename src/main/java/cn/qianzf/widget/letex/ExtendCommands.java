package cn.qianzf.widget.letex;

import org.scilab.forge.jlatexmath.MacroInfo;

public class ExtendCommands {

    public static void regPreExtCommands(){
        MacroInfo.Commands.put("tolerances", new ExtMacroInfo(TolerancesAtom.class, 6,0));
        MacroInfo.Commands.put("surfaceness", new ExtMacroInfo(SurfacenessAtom.class, 22,0));
        MacroInfo.Commands.put("circled", new ExtMacroInfo(CircledAtom.class, 1,0));
        MacroInfo.Commands.put("triangled",new ExtMacroInfo(TriangledAtom.class, 1,0));
    }
}
