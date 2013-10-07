package com.tehbeard.utils.expressions.functions;

import com.tehbeard.utils.expressions.UnresolvedFunctionException;

/**
 * Provides functions to an expression
 * 
 * @author James
 * 
 */
public interface FunctionProvider {

    public int resolveFunction(String function, int[] params) throws UnresolvedFunctionException;
}
