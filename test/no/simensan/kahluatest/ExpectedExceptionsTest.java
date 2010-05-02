package no.simensan.kahluatest;

import no.simensan.kahluatest.annotations.LuaTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@LuaTest(source="empty.lua")
public class ExpectedExceptionsTest extends AbstractKahluaTest{

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Test
    public void testNotCallingCallLuaTestThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("You must explicitly call callLuaTest() in your java test.");
    }

    @Test
    public void testCallingWithoutAnyLuaTestThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("There is no lua test function defined matching java test:");
        callLuaTest();
    }
}
