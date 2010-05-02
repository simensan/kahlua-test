package no.simensan.kahluatest;

import no.simensan.kahluatest.annotations.LuaTest;
import no.simensan.kahluatest.junit.KahluaRunner;
import no.simensan.kahluatest.vm.KahluaVm;
import org.junit.After;
import org.junit.runner.RunWith;

@RunWith(value= KahluaRunner.class)
@LuaTest
public abstract class AbstractKahluaTest implements KahluaTest {
    protected KahluaRunner kahluaRunner;
    protected KahluaVm kahluaVm;
    private boolean hasCalledCallLuaTest = false;
    private boolean isLuaMethod = false;

    public void callLuaTest() {
        hasCalledCallLuaTest = true;
        kahluaRunner.callLuaTest();
    }

    public void setRunner(KahluaRunner kahluaRunner) {
        this.kahluaRunner = kahluaRunner;
        kahluaVm = kahluaRunner.getKahluaVm();
    }

    public void setIsLuaMethod(boolean isLuaMethod) {
        this.isLuaMethod = isLuaMethod;
    }

    @After
    public void ensureCallLuaTest() {
        if(!hasCalledCallLuaTest && !isLuaMethod) {
            throw new IllegalStateException("You must explicitly call callLuaTest() in your java test.");
        }
    }

}
