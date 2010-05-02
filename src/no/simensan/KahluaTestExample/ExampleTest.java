package no.simensan.KahluaTestExample;

import no.simensan.kahluatest.AbstractKahluaTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.*;

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