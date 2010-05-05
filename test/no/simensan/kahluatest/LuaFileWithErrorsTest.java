package no.simensan.kahluatest;

import no.simensan.kahluatest.junit.LuaErrorLogDispatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;
import org.slf4j.Logger;

import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.any;

public class LuaFileWithErrorsTest { // extends AbstractKahluaTest {
    private static final Logger errorLogger = mock(Logger.class);   

   /* @Rule
    public TestWatchman checkForErrorLogs = new LuaErrorLogDispatcher(errorLogger, this) {
        @Override
        public void finished(FrameworkMethod method) {
            checkLuaReturnsForErrors();
            verifyExactly(2).on(errorLogger).error(any(String.class));
        }
    };*/

   /* @Test(expected=RuntimeException.class)
    public void testErrorThrowsExceptionAndLogsOutput() {
        //callLuaTest();
    } */
}
