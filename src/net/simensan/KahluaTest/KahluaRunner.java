package net.simensan.KahluaTest;

import com.google.common.collect.Maps;
import net.simensan.KahluaTest.annotations.LuaTest;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import se.krka.kahlua.vm.KahluaTableIterator;
import se.krka.kahlua.stdlib.BaseLib;

import java.util.List;
import java.util.Map;

public class KahluaRunner extends BlockJUnit4ClassRunner {

    private List<FrameworkMethod> testMethods;
    private KahluaVm kahluaVm;
    private Map<String, Object> luaTestFunctions = Maps.newHashMap();
    private FrameworkMethod currentMethod;

    private final String luaSourceFile;
    private static final String LUA_BEFORE_FUNCTION = "before";
    private static final String LUA_AFTER_FUNCTION = "after";

    public KahluaRunner(Class<?> clazz) throws InitializationError, NoLuaTestsAvailableException {
        super(clazz);
        
        LuaTest annotation = clazz.getAnnotation(LuaTest.class);
        String luaSourceFile = annotation.source();

        if(luaSourceFile.isEmpty()) {
            luaSourceFile = clazz.getName();
            luaSourceFile = luaSourceFile.substring(luaSourceFile.lastIndexOf(".")+1) + ".lua";
        }

        this.luaSourceFile = luaSourceFile;

        setupLuaVm();
    }

    private void setupLuaVm() throws NoLuaTestsAvailableException {
        kahluaVm = new KahluaVm();
        JunitApiExposer junitApiExposer = new JunitApiExposer();
        kahluaVm.getLuaJavaClassExposer().exposeGlobalFunctions(junitApiExposer);

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
        currentMethod = method;
        
        if(luaTestFunctions.get(method.getName()) == null) {
            Description description= describeChild(method);
		    EachTestNotifier eachTestNotifier = new EachTestNotifier(notifier, description);
            System.out.println("Missing function implementation: " + method.getName() + "\n");
            eachTestNotifier.addFailure(new NonExistantLuaTestException());

            return;
        }
        super.runChild(method, notifier);
    }

    public KahluaVm getKahluaVm() {
        return kahluaVm;
    }

    public void callLuaTest() {
        Object beforeMethod = luaTestFunctions.get(LUA_BEFORE_FUNCTION);
        if(beforeMethod != null) {
            kahluaVm.luaCall(beforeMethod);    
        }
        
        Object luaMethod = luaTestFunctions.get(currentMethod.getName());
        kahluaVm.luaCall(luaMethod);

        Object afterMethod = luaTestFunctions.get(LUA_AFTER_FUNCTION); 
        if(afterMethod != null) {
            kahluaVm.luaCall(afterMethod);
        }
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if(testMethods == null) {
            testMethods = super.computeTestMethods();
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
}
