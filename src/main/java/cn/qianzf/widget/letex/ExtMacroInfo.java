package cn.qianzf.widget.letex;

import org.scilab.forge.jlatexmath.Atom;
import org.scilab.forge.jlatexmath.MacroInfo;
import org.scilab.forge.jlatexmath.ParseException;
import org.scilab.forge.jlatexmath.TeXParser;

import java.lang.reflect.InvocationTargetException;

public class ExtMacroInfo extends MacroInfo {

    private Class<? extends Atom> atom;

    public ExtMacroInfo(Class<? extends Atom> atom, int nbArgs, int posOpts) {
        super(nbArgs, posOpts);
        this.atom = atom;
    }

    @Override
    public Object invoke(TeXParser tp, String[] args) throws ParseException {
        try {
            return atom.getConstructor(tp.getClass(), args.getClass()).newInstance(tp,args);
        } catch (Exception e) {
            if(e instanceof ParseException){
                throw  (ParseException)e;
            }else if(e instanceof InvocationTargetException){
                InvocationTargetException targetException=(InvocationTargetException)e;
                if(targetException.getTargetException()!=null && targetException.getTargetException() instanceof ParseException){
                    throw (ParseException)targetException.getTargetException();
                }
            }
            throw new ParseException("Not find Constructor(TeXParser tp, String[] args)",e);
        }
    }


}
