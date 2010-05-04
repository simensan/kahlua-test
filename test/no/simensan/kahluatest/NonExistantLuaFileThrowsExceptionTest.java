package no.simensan.kahluatest;

import no.simensan.kahluatest.annotations.LuaTest;
import no.simensan.kahluatest.junit.KahluaRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.model.InitializationError;

import static junit.framework.Assert.fail;

public class NonExistantLuaFileThrowsExceptionTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Test
    public void testCallingTestRunnerWithNonexistantLuaFileThrowsException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("resource NonExistantLuaFileThrowsExceptionTest$TestClass.lua not found.");

        try {
            KahluaRunner kahluaRunner = new KahluaRunner(TestClass.class);
            fail();
        } catch(InitializationError e) {
            fail();
        }
    }


    @LuaTest
    private class TestClass extends AbstractKahluaTest {
        
    }
}
