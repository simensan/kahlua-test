package net.simensan.KahluaTest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.simensan.KahluaTest.annotations.LuaTest;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import se.krka.kahlua.integration.LuaReturn;
import se.krka.kahlua.vm.KahluaTableIterator;
import se.krka.kahlua.stdlib.BaseLib;

import java.util.List;
import java.util.Map;

public class KahluaRunner extends BlockJUnit4ClassRunner {

    private static final String LUA_BEFORE_FUNCTION = "before";
    private static final String LUA_AFTER_FUNCTION = "after";

    private KahluaVm kahluaVm;
    private List<FrameworkMethod> testMethods;
    private Map<String, Object> luaTestFunctions;
    private List<String> completedMethods = Lists.newArrayList();

    private FrameworkMethod currentMethod;
    private RunNotifier runNotifier;

    public KahluaRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    private void setupLuaVm() throws NoLuaTestsAvailableException {
        LuaTest annotation = getTestClass().getJavaClass().getAnnotation(LuaTest.class);
        String luaSourceFile = annotation.source();

        if(luaSourceFile.isEmpty()) {
            luaSourceFile = getTestClass().getJavaClass().getName();
            luaSourceFile = luaSourceFile.substring(luaSourceFile.lastIndexOf(".")+1) + ".lua";
        }
        
        kahluaVm = new KahluaVm();
        JunitApiExposer junitApiExposer = new JunitApiExposer();
        kahluaVm.getLuaJavaClassExposer().exposeGlobalFunctions(junitApiExposer);
        luaTestFunctions = Maps.newHashMap();
        
        try { //All this needs to be redone each test as each LuaVm holds its own unique reference to the methods
            kahluaVm.loadLuaFromFile(luaSourceFile);
            KahluaTableIterator it = kahluaVm.getEnvironment().iterator();

            boolean hasTest = false;

            while(it.advance()) {
                Object value = it.getValue();
                String name = it.getKey().toString();
                String valueType = BaseLib.type(value);

                boolean foundMatch = (name.startsWith("test")
                                        || name.equals("before")
                                        || name.equals("after"))
                                    && valueType.equals("function");
                if(foundMatch) {
                    hasTest = true;
                    luaTestFunctions.put(name, value);
                }
            }

            if(!hasTest) {
                throw new NoLuaTestsAvailableException();
            }

        } catch(RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        if(runNotifier == null) {
            runNotifier = notifier;
        }
        currentMethod = method;

        if(luaTestFunctions.get(method.getName()) == null) {
            Description description = describeChild(method);
		    EachTestNotifier eachTestNotifier = new EachTestNotifier(notifier, description);
            System.out.println("Missing function implementation: " + method.getName() + "\n");
            eachTestNotifier.addFailure(new NonExistantLuaTestException());
            
            return;
        }
                
        if(method instanceof LuaFrameworkMethod) {
            Description description = describeChild(method);
            EachTestNotifier eachTestNotifier = new EachTestNotifier(notifier, description);
            try {
                eachTestNotifier.fireTestStarted();
                callLuaTest();
                eachTestNotifier.fireTestFinished();
            } catch(RuntimeException e) {
                eachTestNotifier.addFailure(e);
            }
        } else {
             super.runChild(method, notifier);
        }
    }

    public KahluaVm getKahluaVm() {
        return kahluaVm;
    }

    public void callLuaTest() throws RuntimeException {
        Object beforeMethod = luaTestFunctions.get(LUA_BEFORE_FUNCTION);
        if(beforeMethod != null) {
            kahluaVm.luaCall(beforeMethod);    
        }
        
        Object luaMethod = luaTestFunctions.get(currentMethod.getName());
        LuaReturn result = kahluaVm.luaCall(luaMethod);
        if(!result.isSuccess()) {
            throw result.getJavaException();
        }

        Object afterMethod = luaTestFunctions.get(LUA_AFTER_FUNCTION); 
        if(afterMethod != null) {
            kahluaVm.luaCall(afterMethod);
        }

        completedMethods.add(currentMethod.getName());
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if(testMethods == null) {
            try {
                setupLuaVm();
            } catch(NoLuaTestsAvailableException e) {
                e.printStackTrace();
            }
            testMethods = super.computeTestMethods();
            testMethods.addAll(getLuaOnlyTests());
        }

		return testMethods;
    }
    
    @Override
    protected Object createTest() throws Exception {
        setupLuaVm();       
        
        Object testClass = getTestClass().getOnlyConstructor().newInstance();
        ((KahluaTest)testClass).setRunner(this);
        
        return testClass;
    }

    private List<FrameworkMethod> getLuaOnlyTests() {
        List<FrameworkMethod> luaMethods = Lists.newArrayList();
        
        for (String key : luaTestFunctions.keySet()) {
		    if(!key.equals("after") && !key.equals("before")) {
                try {
                    //Force a get on the function, if it doesnt exist in the java class then it is a lua only test
                    getTestClass().getJavaClass().getMethod(key);
                } catch(NoSuchMethodException e) {
                    LuaFrameworkMethod luaMethod = new LuaFrameworkMethod(key);
                    luaMethods.add(luaMethod);
                }
            }
        }
        
        return luaMethods;
    }
}
