package no.simensan.kahluatest;

import no.simensan.kahluatest.annotations.LuaTest;
import no.simensan.kahluatest.junit.KahluaRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.model.InitializationError;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
