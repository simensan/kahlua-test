package no.simensan.kahluatest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class NonExistantLuaRequireFileThrowsExceptionTest extends AbstractKahluaTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testNonExistantLuaRequire() {
        expectedException.expectMessage("resource this file doesnt exist sad face not found.");
        expectedException.expect(IllegalArgumentException.class);
        callLuaTest();       
    }
}
