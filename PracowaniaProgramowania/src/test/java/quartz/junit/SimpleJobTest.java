package quartz.junit;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import quartz.job.SimpleJob;

import java.time.LocalTime;
import static org.junit.Assert.*;


public class SimpleJobTest {

    SimpleJob example;
    final static Logger logger = Logger.getLogger(SimpleJob.class);


    @Before
    public void setUp() {
        logger.info("Running the test code");
        example = new SimpleJob();
    }


    //Tuesday, 8:14 - break
    @Test
    public void CheckBreakTest1() {
        LocalTime test = LocalTime.of(8,14);
        int DayofWeek = 2;
        boolean a = example.checkBreak(DayofWeek, test);
        assertFalse(a);
    }

    //Tuesday, 8:15 - not break
    @Test
    public void CheckBreakTest4() {
        LocalTime test = LocalTime.of(8,15);
        int DayofWeek = 2;
        boolean a = example.checkBreak(DayofWeek, test);
        assertTrue(a);
    }

    //Sunday, 22:43 - break;
    @Test
    public void CheckBreakTest2() {
        LocalTime test = LocalTime.of(22,43);
        int DayofWeek = 0;
        boolean a = example.checkBreak(DayofWeek, test);
        assertFalse(a);
    }


    //Wednesday, 18:12 - not break;
    @Test
    public void CheckBreakTest3() {
        LocalTime test = LocalTime.of(18,12);
        int DayofWeek = 3;
        boolean a = example.checkBreak(DayofWeek, test);
        assertTrue(a);


    }



}
