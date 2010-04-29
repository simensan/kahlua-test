package net.simensan.KahluaTest;

import junit.framework.AssertionFailedError;
import org.junit.ComparisonFailure;
import se.krka.kahlua.integration.annotations.LuaMethod;
import se.krka.kahlua.vm.KahluaTable;

public class JunitApiExposer {
    
    @LuaMethod(global=true, name="throwNewAssertionFailedError")
    public static void throwNewAssertionFailedError(String message) {
        throw new AssertionFailedError("Assertion failed:" + message);
    }

}
