package me.tehbeard.utils.testSuite.expressions;

import static org.junit.Assert.*;

import org.junit.Test;

import me.tehbeard.utils.expressions.InFixExpressionV2;
import me.tehbeard.utils.expressions.functions.FunctionCatalogue;
import me.tehbeard.utils.expressions.functions.FunctionProvider;
import me.tehbeard.utils.expressions.functions.built.AvgFunction;
import me.tehbeard.utils.expressions.functions.built.ModFunction;
import me.tehbeard.utils.expressions.functions.built.SumFunction;

public class ExpressionTest {

    @Test
    public void TestComplex(){

        String sub = "@avg(8,@sum(1,2,3)) + @mod(22,12)";
        InFixExpressionV2 i = new InFixExpressionV2(sub);

        FunctionProvider funcProvider = new FunctionCatalogue(
                new AvgFunction(),
                new SumFunction(),
                new ModFunction()
                );
        assertEquals("Maths works",17,i.getValue(null, funcProvider ));



    }
}
