package no.simensan.kahluatest;

import no.simensan.kahluatest.junit.KahluaRunner;

public interface KahluaTest {
    public void callLuaTest();
    public void setRunner(KahluaRunner kahluaRunner);
    void setIsLuaMethod(boolean isLuaMethod);
}
