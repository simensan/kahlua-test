package net.simensan.KahluaTest;

import org.junit.runner.RunWith;

@RunWith(value=KahluaRunner.class)
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
