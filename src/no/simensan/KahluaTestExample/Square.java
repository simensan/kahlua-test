package no.simensan.KahluaTestExample;

import se.krka.kahlua.integration.annotations.LuaClass;
import se.krka.kahlua.integration.annotations.LuaMethod;

@LuaClass
public class Square {
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
