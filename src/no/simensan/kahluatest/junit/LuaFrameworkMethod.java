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
    public Object invokeExplosively(Object target, Object... params) throws Throwable {
        return null;
    }

    @Override
    public String getName() {
        return luaMethodName;
    }

    @Override
    public void validatePublicVoidNoArg(boolean isStatic, List<Throwable> errors) {

    }

    @Override
    public void validatePublicVoid(boolean isStatic, List<Throwable> errors) {

    }

    @Override
    public boolean isShadowedBy(FrameworkMethod other) {
        return false;
    }

    @Override
    public boolean producesType(Class<?> type) {
        return false;
    }

    @Override
    public Annotation[] getAnnotations() {
        return new Annotation[0];
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return null;
    }

    @Override
    public int hashCode() {
        return luaMethodName.hashCode();
    }
}
