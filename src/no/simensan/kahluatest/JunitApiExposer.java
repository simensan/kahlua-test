package no.simensan.kahluatest;

import org.junit.Assert;
import se.krka.kahlua.integration.annotations.LuaMethod;

public class JunitApiExposer {
    
    @LuaMethod(global = true)
    public void assertTrue(boolean condition) {
        Assert.assertTrue(condition);
    }

	@LuaMethod(global = true)
	public void assertFalse(boolean condition) {
		Assert.assertFalse(condition);
	}

	@LuaMethod(global = true)
	public void fail() {
		Assert.fail();
	}

	@LuaMethod(global = true)
	public void assertNull(Object object) {
		Assert.assertNull(object);
	}

	@LuaMethod(global = true)
	public void assertNotNull(Object object) {
		Assert.assertNotNull(object);
	}

	@LuaMethod(global = true)
	public void assertSame(Object expected, Object actual) {
		Assert.assertSame(expected, actual);
	}

	@LuaMethod(global = true)
	public void assertNotSame(Object unexpected, Object actual) {
		Assert.assertNotSame(unexpected, actual);
	}

	@LuaMethod(global = true)
	public void assertEquals(Object expected, Object actual) {
		Assert.assertEquals(expected, actual);
    }
}