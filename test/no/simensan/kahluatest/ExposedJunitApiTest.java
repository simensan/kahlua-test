package no.simensan.kahluatest;

import org.junit.Test;

public class ExposedJunitApiTest extends AbstractKahluaTest {


    @Test(expected = RuntimeException.class)
    public void testFail() {
        callLuaTest();
    }
}
