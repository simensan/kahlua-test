package net.simensan.KahluaTest;

import com.google.common.collect.Maps;
import net.simensan.KahluaTest.annotations.LuaTest;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import se.krka.kahlua.stdlib.BaseLib;

import java.util.List;
import java.util.Map;

public class KahluaRunner extends BlockJUnit4ClassRunner {

    private List<FrameworkMethod> testMethods;
    private KahluaVm kahluaVm;
    private Map<String, Object> luaTestFunctions = Maps.newHashMap();
    private FrameworkMethod currentMethod;

    public KahluaRunner(Class<?> clazz) throws InitializationError {
        super(clazz);

        LuaTest annotation = clazz.getAnnotation(LuaTest.class);
        kahluaVm = new KahluaVm();
        JunitApiExposer junitApiExposer = new JunitApiExposer();
        kahluaVm.getLuaJavaClassExposer().exposeGlobalFunctions(junitApiExposer);

        try {
            kahluaVm.loadLuaFromFile("testhelper.lua");
            kahluaVm.loadLuaFromFile(annotation.source());
            KahluaTable env = kahluaVm.getEnvironment();

            KahluaTableIterator it = env.iterator();

            boolean hasTest = false;

            while(it.advance()) {
                Object value = it.getValue();
                String name = (String)it.getKey();
                String valueType = BaseLib.type(value);

                boolean foundMatch = name.startsWith("test") && valueType.equals("function");
                if(foundMatch) {
                    hasTest = true;
                    luaTestFunctions.put(name, value);
                }
            }

            if(!hasTest) {
                //no test error
            }
            
        } catch(RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        System.out.println("Run child");
        currentMethod = method;
        
        if(luaTestFunctions.get(method.getName()) == null) {
            Description description= describeChild(method);
		    EachTestNotifier eachTestNotifier = new EachTestNotifier(notifier, description);
            System.out.println("\nMissing function implementation: " + method.getName() + "\n");
            eachTestNotifier.addFailure(new NonExistantLuaTestException());

            return;
        }

        super.runChild(method, notifier);
    }

    public KahluaVm getKahluaVm() {
        return kahluaVm;
    }

    public void callLuaTest() {
        Object luaMethod = luaTestFunctions.get(currentMethod.getName());
        kahluaVm.luaCall(luaMethod);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        List<FrameworkMethod> baseMethods = super.computeTestMethods();

        if(testMethods == null) {
            testMethods = baseMethods;
        }

		return baseMethods;
    }

    @Override
    protected Object createTest() throws Exception {
        Object testClass = getTestClass().getOnlyConstructor().newInstance();
        ((KahluaTest)testClass).setRunner(this);

        return testClass;
    }
}
