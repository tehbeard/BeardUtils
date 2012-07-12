package me.tehbeard.utils.expressions.functions;

import java.util.ArrayList;
import java.util.List;

import me.tehbeard.utils.expressions.UnresolvedFunctionException;

public class FunctionCatalogue implements FunctionProvider {

    
    List<FunctionProvider> providers;
    
    public FunctionCatalogue(){
        providers = new ArrayList<FunctionProvider>();
    }
    
    public FunctionCatalogue(FunctionProvider... providers){
        this();
        for(FunctionProvider provider : providers){
        this.providers.add(provider);
        }
    }
    
    
    public int resolveFunction(String function, int[] params)
            throws UnresolvedFunctionException {
        for(FunctionProvider provider : providers){
            try{
            return provider.resolveFunction(function, params);
            }catch(UnresolvedFunctionException e){
                
            }
        }
        throw new UnresolvedFunctionException();
    }

}
