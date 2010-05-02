package no.simensan.kahluatest.vm;

import se.krka.kahlua.converter.LuaConverterManager;
import se.krka.kahlua.integration.annotations.LuaMethod;
import se.krka.kahlua.integration.expose.LuaJavaClassExposer;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.Platform;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class NotPooLuaJavaClassExposer extends LuaJavaClassExposer {
    private KahluaTable environment;
    
    public NotPooLuaJavaClassExposer(LuaConverterManager manager, Platform platform, KahluaTable environment) {
        super(manager, platform, environment);

        this.environment = environment;
    }


   @Override
   public void exposeGlobalFunctions(Object object) {
        Class<?> clazz = object.getClass();
       
        for(Class<?> subClazz : getSuperClasses(clazz)) {
            for (Method method : subClazz.getMethods()) {
                if (method.isAnnotationPresent(LuaMethod.class)) {
                    LuaMethod luaMethod = method.getAnnotation(LuaMethod.class);

                    String methodName;
                    if (luaMethod.name().equals("")) {
                        methodName = method.getName();
                    } else {
                        methodName = luaMethod.name();
                    }
                    if (luaMethod.global()) {
                        exposeGlobalObjectFunction(environment, object, method, methodName);
                    }
                }
            }
        }
    }

    private List<Class<?>> getSuperClasses(Class<?> testClass) {
        List<Class<?>> superClasses = new ArrayList<Class<?>>();

        while (testClass != null) {
            superClasses.add(testClass);
            testClass = testClass.getSuperclass();
        }

        return superClasses;
    }
}
