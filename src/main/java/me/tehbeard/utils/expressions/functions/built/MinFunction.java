package me.tehbeard.utils.expressions.functions.built;

import me.tehbeard.utils.expressions.UnresolvedFunctionException;
import me.tehbeard.utils.expressions.functions.FunctionProvider;

public class MinFunction implements FunctionProvider {

    public int resolveFunction(String function, int[] params)
            throws UnresolvedFunctionException {
        if(!function.equalsIgnoreCase("min")){throw new UnresolvedFunctionException();}
        int i = 0;
        for(int k : params){
            i = k>i ? k : i;
        }
        return i;
    }

}
