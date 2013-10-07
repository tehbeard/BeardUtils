package com.tehbeard.utils.testSuite.factory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.tehbeard.utils.factory.ConfigurableFactory;

import org.junit.Before;
import org.junit.Test;

public class FactoryTest {

    private ConfigurableFactory<TestPattern, FactoryTag> factory;

    @Before
    public void setup() {
        this.factory = new ConfigurableFactory<TestPattern, FactoryTag>(FactoryTag.class) {

            @Override
            public String getTag(FactoryTag annotation) {
                return annotation.value();
            }
        };

        this.factory.addProduct(Alpha.class);
        this.factory.addProduct(Beta.class);
        this.factory.addProduct(Gamma.class);

    }

    @Test
    public void testFactoryGet() {

        assertTrue("alpha", this.factory.getProduct("alpha") instanceof Alpha);

        assertTrue("beta", this.factory.getProduct("beta") instanceof Beta);

        assertTrue("gamma", this.factory.getProduct("gamma") instanceof Gamma);

        assertFalse("delta", this.factory.getProduct("delta") instanceof Gamma);

        assertFalse(this.factory.getProduct("alpha") instanceof Gamma);

    }

}
