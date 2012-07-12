package me.tehbeard.utils.expressions.functions;

import me.tehbeard.utils.expressions.UnresolvedFunctionException;

public interface FunctionProvider {

    
    public int resolveFunction(String function,int[] params) throws UnresolvedFunctionException;
}
