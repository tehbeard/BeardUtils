package me.tehbeard.utils.expressions.functions.built;

import me.tehbeard.utils.expressions.UnresolvedFunctionException;
import me.tehbeard.utils.expressions.functions.Function;
import me.tehbeard.utils.expressions.functions.FunctionProvider;

@Function("sum")
public class SumFunction implements FunctionProvider {

    @Override
    public int resolveFunction(String function, int[] params) throws UnresolvedFunctionException {
        if (!function.equalsIgnoreCase("sum")) {
            throw new UnresolvedFunctionException();
        }
        int i = 0;
        for (int k : params) {
            i += k;
        }
        return i;
    }

}
