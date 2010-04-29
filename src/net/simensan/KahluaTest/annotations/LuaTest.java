package net.simensan.KahluaTest.annotations;

import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface LuaTest {
	static class None extends Throwable {
		private static final long serialVersionUID= 1L;
		private None() {
		}
	}
	Class<? extends Throwable> expected() default None.class;
	long timeout() default 0L;
    String source();
}