package no.simensan.kahluatest.junit;

import no.simensan.kahluatest.KahluaTest;
import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;
import org.slf4j.Logger;
import se.krka.kahlua.integration.LuaReturn;

import java.util.List;

public class LuaErrorLogDispatcher extends TestWatchman {

    private final Logger logger;
    private KahluaTest testRunner;

    public LuaErrorLogDispatcher(Logger logger, KahluaTest testRunner) {
        this.logger = logger;
        this.testRunner = testRunner;
    }

    @Override
    public void failed(Throwable e, FrameworkMethod method) {
        checkLuaReturnsForErrors();
    }

    protected void checkLuaReturnsForErrors() {
        List<LuaReturn> luaReturns = testRunner.getRunner().getLuaReturns();

        for(LuaReturn luaReturn : luaReturns) {
            if(!luaReturn.isSuccess()) {
                logger.error(luaReturn.getErrorString());
                logger.error(luaReturn.getLuaStackTrace());
            }
        }
    }
}
