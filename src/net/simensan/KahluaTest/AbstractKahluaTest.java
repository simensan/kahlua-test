package net.simensan.KahluaTest;

import net.simensan.KahluaTest.annotations.LuaTest;
import org.junit.runner.RunWith;

@RunWith(value=KahluaRunner.class)
@LuaTest
public abstract class AbstractKahluaTest implements KahluaTest {
    private KahluaRunner kahluaRunner;
    protected KahluaVm kahluaVm;

    public void callLuaTest() {
        kahluaRunner.callLuaTest();
    }

    public void setRunner(KahluaRunner kahluaRunner) {
        this.kahluaRunner = kahluaRunner;
        kahluaVm = kahluaRunner.getKahluaVm();
    }
}
