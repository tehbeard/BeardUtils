package me.tehbeard.utils.testSuite.factory;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import me.tehbeard.utils.factory.ConfigurableFactory;

public class FactoryTest {

	
	
		
	

	

	private ConfigurableFactory<TestPattern, FactoryTag> factory;
	
	@Before
	public void setup(){
		factory = new ConfigurableFactory<TestPattern, FactoryTag>(FactoryTag.class) {
			
			@Override
			public String getTag(FactoryTag annotation) {
				return annotation.value();
			}
		};
		
		factory.addProduct(Alpha.class);
		factory.addProduct(Beta.class);
		factory.addProduct(Gamma.class);
		
	}
	
	@Test
	public void testFactoryGet(){
		
		assertTrue("alpha",factory.getProduct("alpha") instanceof Alpha);
		
		assertTrue("beta",factory.getProduct("beta") instanceof Beta);
		
		assertTrue("gamma",factory.getProduct("gamma") instanceof Gamma);
		
		assertFalse("delta",factory.getProduct("delta") instanceof Gamma);
		
		assertFalse(factory.getProduct("alpha") instanceof Gamma);
		
		
	}
	
}
