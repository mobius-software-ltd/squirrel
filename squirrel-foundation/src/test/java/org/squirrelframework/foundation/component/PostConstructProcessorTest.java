package org.squirrelframework.foundation.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.squirrelframework.foundation.component.impl.PostConstructPostProcessorImpl;

public class PostConstructProcessorTest {
    
    @Test
    public void testPostConstruct() {
        SquirrelPostProcessorProvider.getInstance().register(Programmer.class, PostConstructPostProcessorImpl.class);
        Programmer p = SquirrelProvider.getInstance().newInstance(Programmer.class);
        assertEquals(p.getName(), "Henry");
        assertEquals(p.getLanguage(), "Java");
        SquirrelPostProcessorProvider.getInstance().unregister(Programmer.class);
    }
}
