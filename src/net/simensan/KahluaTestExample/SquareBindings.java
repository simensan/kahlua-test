package net.simensan.KahluaTestExample;

import se.krka.kahlua.integration.annotations.LuaMethod;

public class SquareBindings {
    @LuaMethod(global=true, name="CreateSquare")
    public Square createSquare(int size) {
        return new Square(size);
    }
}
