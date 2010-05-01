package net.simensan.KahluaTestExample;

import net.simensan.KahluaTest.AbstractKahluaTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.*;

//@LuaTest(source = "blah) - Can be override from default that bases on class name
public class ExampleTest extends AbstractKahluaTest {
    private SquareBindings squareBindings;

    @Before
    public void setup() {
        squareBindings = spy(new SquareBindings());
        kahluaVm.getLuaJavaClassExposer().exposeClass(Square.class);
        kahluaVm.getLuaJavaClassExposer().exposeGlobalFunctions(squareBindings);
    }

    @Test
    public void testCreateSquare() {
        callLuaTest();
        verifyOnce().on(squareBindings).createSquare(10);
   }

    @Test
    public void testCreateSquare2() {
        assertEquals(1,1);
        callLuaTest();
        verifyOnce().on(squareBindings).createSquare(12);
   }
}