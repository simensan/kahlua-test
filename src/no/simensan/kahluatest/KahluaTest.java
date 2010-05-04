package no.simensan.kahluatest;

import no.simensan.kahluatest.junit.KahluaRunner;

public interface KahluaTest {
    void callLuaTest();
    void setRunner(KahluaRunner kahluaRunner);
    void setIsLuaMethod(boolean isLuaMethod);
    KahluaRunner getRunner();
}
