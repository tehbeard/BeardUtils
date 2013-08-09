package me.tehbeard.utils.expressions.functions.built;

import me.tehbeard.utils.expressions.UnresolvedFunctionException;
import me.tehbeard.utils.expressions.functions.Function;
import me.tehbeard.utils.expressions.functions.FunctionProvider;

@Function("max")
public class MaxFunction implements FunctionProvider {

    public int resolveFunction(String function, int[] params)
            throws UnresolvedFunctionException {
        if(!function.equalsIgnoreCase("max")){throw new UnresolvedFunctionException();}
        int i = 0;
        for(int k : params){
            i = k>i ? k : i;
        }
        return i;
    }

}
