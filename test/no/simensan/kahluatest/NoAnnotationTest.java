package no.simensan.kahluatest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NoAnnotationTest extends AbstractKahluaTest {

    @Test
    public void testNoAnnotationUsesDefaultBehaviour() {
        assertEquals(kahluaRunner.getLuaSourceFile(), "NoAnnotationTest.lua");
        callLuaTest();
    }
}
