package cn.qianzf.widget.letex;

import org.scilab.forge.jlatexmath.ParseException;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXParser;

public class ExtTeXFormula extends TeXFormula {

    public ExtTeXFormula(TeXParser tp, String s, boolean firstpass) throws ParseException {
        super(tp, s, firstpass);
    }
}
