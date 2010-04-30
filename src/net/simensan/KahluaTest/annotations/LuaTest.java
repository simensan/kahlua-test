package net.simensan.KahluaTest.annotations;

import org.junit.Test;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface LuaTest {
	static class None extends Throwable {
		private None() {
		}
	}
	Class<? extends Throwable> expected() default None.class;
    String source() default "";
}