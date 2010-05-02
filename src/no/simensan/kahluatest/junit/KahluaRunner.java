package no.simensan.kahluatest.junit;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import no.simensan.kahluatest.*;
import no.simensan.kahluatest.annotations.LuaTest;
import no.simensan.kahluatest.vm.KahluaVm;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import se.krka.kahlua.integration.LuaReturn;
import se.krka.kahlua.vm.KahluaTableIterator;
import se.krka.kahlua.stdlib.BaseLib;

import java.util.List;
import java.util.Map;

public class KahluaRunner extends BlockJUnit4ClassRunner {

    private static final String LUA_BEFORE_FUNCTION = "before";
    private static final String LUA_AFTER_FUNCTION = "after";

    private KahluaVm kahluaVm;
    private FrameworkMethod currentMethod;
    private String luaSourceFile;
    private List<FrameworkMethod> testMethods;
    private Map<String, Object> luaTestFunctions;
    private List<String> hasCalledLuaTest = Lists.newArrayList();

    public KahluaRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    public void callLuaTest() throws RuntimeException {
        hasCalledLuaTest.add(currentMethod.getName());
        
        Object beforeMethod = luaTestFunctions.get(LUA_BEFORE_FUNCTION);
        if(beforeMethod != null) {
            kahluaVm.luaCall(beforeMethod);
        }

        Object luaMethod = luaTestFunctions.get(currentMethod.getName());

        if(luaMethod == null) {
            throw new IllegalStateException("There is no lua test function defined matching java test: " + currentMethod.getName());
        }
        
        LuaReturn result = kahluaVm.luaCall(luaMethod);
        if(!result.isSuccess()) {
            throw result.getJavaException();
        }

        Object afterMethod = luaTestFunctions.get(LUA_AFTER_FUNCTION);
        if(afterMethod != null) {
            kahluaVm.luaCall(afterMethod);
        }
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object testClass) {
       currentMethod = method;
        
       if(method instanceof LuaFrameworkMethod) {
           ((KahluaTest)testClass).setIsLuaMethod(true);
           
            Statement statement = new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    callLuaTest();
                }
            };

            statement = withBefores(method, testClass, statement);
            statement = withAfters(method, testClass, statement);
           
           return statement;
        } else {
             return super.methodInvoker(method, testClass);
        }
	}

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if(testMethods == null) {
            setupLuaVm();
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

    private void setupLuaVm() {
        LuaTest annotation = getTestClass().getJavaClass().getAnnotation(LuaTest.class);
        String luaSourceFile = annotation.source();

        if(luaSourceFile.isEmpty()) {
            luaSourceFile = getTestClass().getJavaClass().getName();
            luaSourceFile = luaSourceFile.substring(luaSourceFile.lastIndexOf(".")+1) + ".lua";
        }

        this.luaSourceFile = luaSourceFile;

        kahluaVm = new KahluaVm();
        JunitApiExposer junitApiExposer = new JunitApiExposer();
        kahluaVm.getLuaJavaClassExposer().exposeGlobalFunctions(junitApiExposer);
        luaTestFunctions = Maps.newHashMap();

        try { //All this needs to be redone each test as each LuaVm holds its own unique reference to the methods
            kahluaVm.loadLuaFromFile(luaSourceFile);
            KahluaTableIterator it = kahluaVm.getEnvironment().iterator();

            while(it.advance()) {
                Object value = it.getValue();
                String name = it.getKey().toString();
                String valueType = BaseLib.type(value);

                boolean foundMatch = (name.startsWith("test")
                                        || name.equals("before")
                                        || name.equals("after"))
                                    && valueType.equals("function");
                if(foundMatch) {
                    luaTestFunctions.put(name, value);
                }
            }
        } catch(RuntimeException e) {
            e.printStackTrace();
        }
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

    public String getLuaSourceFile() {
        return this.luaSourceFile;
    }

    public KahluaVm getKahluaVm() {
        return kahluaVm;
    }
}