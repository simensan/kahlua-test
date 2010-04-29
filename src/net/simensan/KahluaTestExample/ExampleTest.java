package net.simensan.KahluaTestExample;

import net.simensan.KahluaTest.AbstractKahluaTest;
import net.simensan.KahluaTest.annotations.LuaTest;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static se.mockachino.Mockachino.*;

@LuaTest(source="test.lua")
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
        assertEquals(1,1);
        callLuaTest();
        verifyOnce().on(squareBindings).createSquare(10);
   }
}