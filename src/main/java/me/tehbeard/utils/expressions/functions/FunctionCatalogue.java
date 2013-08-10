package me.tehbeard.utils.expressions.functions;

import java.util.HashMap;
import java.util.Map;

import me.tehbeard.utils.expressions.UnresolvedFunctionException;

/**
 * Implements a composite of of FunctionProviders, enabling reuse
 * 
 * @author James
 * 
 */
public class FunctionCatalogue implements FunctionProvider {

    Map<String, FunctionProvider> providers = new HashMap<String, FunctionProvider>();

    public FunctionCatalogue() {
    }

    /**
     * Constructs a function provider using these providers NOTE: for providers
     * with conflicting function names, order in this constructor provides
     * presendence.
     * 
     * @param providers
     */

    public FunctionCatalogue(FunctionProvider... providers) {
        this();
        for (FunctionProvider provider : providers) {
            Function f = provider.getClass().getAnnotation(Function.class);
            if (f != null) {
                for (String name : f.value()) {
                    this.providers.put(name, provider);
                }
            }

        }
    }

    @Override
    public int resolveFunction(String function, int[] params) throws UnresolvedFunctionException {
        FunctionProvider provider = this.providers.get(function);
        if (provider == null) {
            throw new UnresolvedFunctionException();
        }
        return provider.resolveFunction(function, params);
    }

}
