package no.simensan.kahluatest;

import org.junit.Before;
import org.junit.Test;
import se.krka.kahlua.integration.annotations.LuaClass;
import se.krka.kahlua.integration.annotations.LuaMethod;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.spy;
import static se.mockachino.Mockachino.verifyOnce;


public class BindingsTest extends AbstractKahluaTest {

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
        callLuaTest();
        verifyOnce().on(squareBindings).createSquare(12);
   }


    private class SquareBindings {
        @LuaMethod(global=true, name="CreateSquare")
        public Square createSquare(int size) {
            return new Square(size);
        }
    }

    @LuaClass
    private class Square {
        private int size = 0;

        public Square(int size) {
            this.size = size;
        }


        @LuaMethod(name="SetSize")
        public void setSize(int size) {
            this.size = size;
        }

        @LuaMethod(name="GetSize")
        public int getSize() {
            return size;
        }

    }
}
