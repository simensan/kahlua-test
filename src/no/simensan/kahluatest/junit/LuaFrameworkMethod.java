package no.simensan.kahluatest.junit;

import org.junit.runners.model.FrameworkMethod;

import java.lang.annotation.Annotation;
import java.util.List;

public class LuaFrameworkMethod extends FrameworkMethod {

    private String luaMethodName;

    public LuaFrameworkMethod(String luaMethodName) {
        super(null);
        this.luaMethodName = luaMethodName;
    }

    @Override
    public String getName() {
        return luaMethodName;
    }

    @Override
    public Annotation[] getAnnotations() {
        return new Annotation[0];
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return null;
    }

}
