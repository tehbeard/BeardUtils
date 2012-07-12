package me.tehbeard.utils.expressions.functions.built;

import me.tehbeard.utils.expressions.UnresolvedFunctionException;
import me.tehbeard.utils.expressions.functions.FunctionProvider;

public class ModFunction implements FunctionProvider {

    public int resolveFunction(String function, int[] params)
            throws UnresolvedFunctionException {
        if(!function.equalsIgnoreCase("mod") || params.length!=2){throw new UnresolvedFunctionException();}
        
        return params[1] % params[0];
    }

}
