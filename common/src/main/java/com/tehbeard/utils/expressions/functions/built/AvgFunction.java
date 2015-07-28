package com.tehbeard.utils.expressions.functions.built;

import com.tehbeard.utils.expressions.UnresolvedFunctionException;
import com.tehbeard.utils.expressions.functions.Function;
import com.tehbeard.utils.expressions.functions.FunctionProvider;

@Function("avg")
public class AvgFunction implements FunctionProvider {

    @Override
    public int resolveFunction(String function, int[] params) throws UnresolvedFunctionException {
        if (!function.equalsIgnoreCase("avg")) {
            throw new UnresolvedFunctionException();
        }
        int i = 0;
        for (int k : params) {
            i += k;
        }
        return i / params.length;
    }

}
