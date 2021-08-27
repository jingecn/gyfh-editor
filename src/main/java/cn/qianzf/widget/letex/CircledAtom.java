package cn.qianzf.widget.letex;

import org.scilab.forge.jlatexmath.*;
import sun.font.FontDesignMetrics;

import java.awt.*;

public class CircledAtom extends Atom {

    private Atom child;

    protected Color bg = null, line = null;

    public CircledAtom(final TeXParser tp, String[] args) {
        this.child = new ExtTeXFormula(tp, args[1], false).root;
    }

    @Override
    public Box createBox(TeXEnvironment env) {
        float drt = env.getTeXFont().getDefaultRuleThickness(env.getStyle());
        return new CircledBox(child.createBox(env),bg,line,drt);
    }
}
