package com.tehbeard.utils.expressions.functions.built;

import com.tehbeard.utils.expressions.UnresolvedFunctionException;
import com.tehbeard.utils.expressions.functions.Function;
import com.tehbeard.utils.expressions.functions.FunctionProvider;

@Function("mod")
public class ModFunction implements FunctionProvider {

    @Override
    public int resolveFunction(String function, int[] params) throws UnresolvedFunctionException {
        if (!function.equalsIgnoreCase("mod") || (params.length != 2)) {
            throw new UnresolvedFunctionException();
        }

        return params[1] % params[0];
    }

}
