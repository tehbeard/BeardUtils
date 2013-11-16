package com.tehbeard.utils.expressions;

/**
 * provides variables for an {@link InFixExpression}
 * 
 * @author James
 * 
 */
public interface VariableProvider {

    /**
     * Resolves variable var into a value
     * 
     * @param var
     * @return
     */
    public int resolveVariable(String var);

    /**
     * Returns a number of variables linked to a reference.
     * @param array
     * @return 
     */
    public int[] resolveReference(String array);
}
