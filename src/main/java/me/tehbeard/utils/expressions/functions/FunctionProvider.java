package me.tehbeard.utils.expressions.functions;

import me.tehbeard.utils.expressions.UnresolvedFunctionException;

/**
 * Provides functions to an expression
 * @author James
 *
 */
public interface FunctionProvider {

    
    public int resolveFunction(String function,int[] params) throws UnresolvedFunctionException;
}
