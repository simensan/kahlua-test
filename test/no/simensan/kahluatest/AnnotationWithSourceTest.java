package no.simensan.kahluatest;

import no.simensan.kahluatest.annotations.LuaTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@LuaTest(source="default.lua")
public class AnnotationWithSourceTest extends AbstractKahluaTest {

    @Test
    public void testAnnotationWithSourceValue() {
        assertEquals(kahluaRunner.getLuaSourceFile(), "default.lua");
        callLuaTest();
    }
}
