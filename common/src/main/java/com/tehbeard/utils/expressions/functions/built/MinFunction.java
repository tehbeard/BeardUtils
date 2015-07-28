package com.tehbeard.utils.expressions.functions.built;

import com.tehbeard.utils.expressions.UnresolvedFunctionException;
import com.tehbeard.utils.expressions.functions.Function;
import com.tehbeard.utils.expressions.functions.FunctionProvider;

@Function("min")
public class MinFunction implements FunctionProvider {

    @Override
    public int resolveFunction(String function, int[] params) throws UnresolvedFunctionException {
        if (!function.equalsIgnoreCase("min")) {
            throw new UnresolvedFunctionException();
        }
        int i = 0;
        for (int k : params) {
            i = k > i ? k : i;
        }
        return i;
    }

}
