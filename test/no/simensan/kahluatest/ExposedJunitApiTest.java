package no.simensan.kahluatest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ExposedJunitApiTest extends AbstractKahluaTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test(expected = RuntimeException.class)
    public void testFail() {
        callLuaTest();
    }

    @Test
    public void testFailWithMessage() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Fail with message.");

        callLuaTest();
    }
}
