package me.tehbeard.utils.expressions;

/**
 * provides variables for an {@link InFixExpression}
 * @author James
 *
 */
public interface VariableProvider {

    /**
     * Resolves variable var into a value
     * @param var
     * @return
     */
    public int resolveVariable(String var);

	public int[] resolveReference(String array);
}
