package quartz.junit;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import quartz.scheduler.SimpleScheduler;

import static org.junit.Assert.*;


public class SimpleSchedulerTest {

    SimpleScheduler example;
    final static Logger logger = Logger.getLogger(SimpleScheduler.class);


    @Before
    public void setUp() {
        logger.info("Running the test code");
        example = new SimpleScheduler();
    }

    @Test
    public void peselcontrolTest() {
        boolean a = example.peselcontrol("98060606476");
        assertTrue(a);
    }

    @Test
    public void peselcontrolnTest2() {
        boolean a = example.peselcontrol("98060606475");
        assertTrue(!a);
    }

    @Test
    public void peselcontrolTest3() {
        boolean a = example.peselcontrol("Pysznefrytki!!!");
        assertTrue(!a);
    }



}
