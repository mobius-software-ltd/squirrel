package org.squirrelframework.foundation.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class ComponentPostProcessorTest {
    
    @Test
    public void testRegisterPostProcessor() {
        SquirrelPostProcessorProvider.getInstance().register(Person.class, new SquirrelPostProcessor<Person>() {
            @Override
            public void postProcess(Person p) {
                p.setName("Henry");
            }
        });
        
        Person p = SquirrelProvider.getInstance().newInstance(Person.class);
        assertNotNull(p);
        assertTrue(p instanceof PersonImpl);
        assertEquals(p.getName(), "Henry");
        
        SquirrelPostProcessorProvider.getInstance().unregister(Person.class);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testAssignablePostProcessor() {
        SquirrelPostProcessor<Object> pp = new SquirrelPostProcessor<Object>() {
            @Override
            public void postProcess(Object p) {}
        };
        SquirrelPostProcessorProvider.getInstance().register(Person.class, pp);
        List<SquirrelPostProcessor<? super Student>> studentPostProcessors = SquirrelPostProcessorProvider.
                getInstance().getCallablePostProcessors(Student.class);
        assertTrue((SquirrelPostProcessor<Object>)studentPostProcessors.get(0) == pp);
        SquirrelPostProcessorProvider.getInstance().unregister(Person.class);
        
        SquirrelPostProcessorProvider.getInstance().register(Student.class, pp);
        List<SquirrelPostProcessor<? super Person>> personPostProcessors = SquirrelPostProcessorProvider.
                getInstance().getCallablePostProcessors(Person.class);
        assertEquals(personPostProcessors.size(), 0);
        SquirrelPostProcessorProvider.getInstance().unregister(Student.class);
        
        SquirrelPostProcessorProvider.getInstance().register(Person.class, pp);
        SquirrelPostProcessorProvider.getInstance().register(Student.class, pp);
        List<SquirrelPostProcessor<? super Student>> studentAndPersonPostProcessors = SquirrelPostProcessorProvider.
                getInstance().getCallablePostProcessors(Student.class);
        assertTrue((SquirrelPostProcessor<Object>)studentAndPersonPostProcessors.get(0) == pp);
        assertTrue((SquirrelPostProcessor<Object>)studentAndPersonPostProcessors.get(1) == pp);
        SquirrelPostProcessorProvider.getInstance().unregister(Person.class);
        SquirrelPostProcessorProvider.getInstance().unregister(Student.class);
        
    }
    
    @Test
    public void testCompositePostProssor() {
        SquirrelPostProcessor<Person> p1 = new SquirrelPostProcessor<Person>() {
            @Override
            public void postProcess(Person p) {
                p.setName("Henry");
            }
        };
        SquirrelPostProcessor<Student> p2 = new SquirrelPostProcessor<Student>() {
            @Override
            public void postProcess(Student p) {
                p.setSchool("XJTU");
            }
        };
        SquirrelPostProcessorProvider.getInstance().register(Student.class, p1);
        SquirrelPostProcessorProvider.getInstance().register(Student.class, p2);
        
        List<SquirrelPostProcessor<? super Student>> studentPostProcessors = SquirrelPostProcessorProvider.
                getInstance().getCallablePostProcessors(Student.class);
        assertEquals(studentPostProcessors.size(), 1);
        
        Student student = SquirrelProvider.getInstance().newInstance(Student.class);
        assertEquals(student.getName(), "Henry");
        assertEquals(student.getSchool(), "XJTU");
        
        SquirrelPostProcessorProvider.getInstance().unregister(Student.class);
        SquirrelPostProcessorProvider.getInstance().unregister(Student.class);
    }
}
