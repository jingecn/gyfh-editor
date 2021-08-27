package cn.qianzf.widget.letex;

import org.scilab.forge.jlatexmath.Atom;
import org.scilab.forge.jlatexmath.Box;
import org.scilab.forge.jlatexmath.TeXEnvironment;
import org.scilab.forge.jlatexmath.TeXParser;

import java.awt.*;

public class TriangledAtom extends Atom {

    private Atom child;

    protected Color bg = null, line = null;

    public TriangledAtom(final TeXParser tp, String[] args) {
        this.child = new ExtTeXFormula(tp, args[1], false).root;
    }

    @Override
    public Box createBox(TeXEnvironment env) {
        float drt = env.getTeXFont().getDefaultRuleThickness(env.getStyle());
        return new TriangledBox(child.createBox(env),bg,line,drt);
    }
}
