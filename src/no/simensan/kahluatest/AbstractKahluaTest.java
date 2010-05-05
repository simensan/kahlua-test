package no.simensan.kahluatest;

import no.simensan.kahluatest.annotations.LuaTest;
import no.simensan.kahluatest.junit.KahluaRunner;
import no.simensan.kahluatest.junit.LuaErrorLogDispatcher;
import no.simensan.kahluatest.vm.KahluaVm;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.TestWatchman;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(value= KahluaRunner.class)
@LuaTest
public abstract class AbstractKahluaTest implements KahluaTest {
    protected final static Logger logger = LoggerFactory.getLogger(AbstractKahluaTest.class);
    
    @Rule  //Only show lua error messages if the test fails
    public TestWatchman testWatchman = new LuaErrorLogDispatcher(logger, this);
    
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

    public KahluaRunner getRunner() {
        return kahluaRunner;
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
