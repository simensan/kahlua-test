package net.simensan.KahluaTest.annotations;

import org.junit.Test;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface LuaTest {
    String source() default "";
}