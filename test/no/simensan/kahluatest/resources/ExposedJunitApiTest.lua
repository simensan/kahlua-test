function testAssertEquals()
  local table = {a = 1}
  assertEquals(2, 2)
  assertEquals("a", "a")
  assertEquals(table, table)
  assertEquals(2.2, 2.2)
end

function testAssertSame()
  local t = {}

  assertSame(t, t)
  assertSame(1.1, 1.1)
  assertSame("a", "a")
end

function  testAssertNotSame()
  assertNotSame(2, 3)
  assertNotSame("a", "b")
  assertNotSame({}, {})
  assertNotSame(2.2, 2.22)
end

function testAssertTrue()
  assertTrue(true)
  assertTrue(false == false)
end

function testAssertFalse()
  assertFalse(false)
end

function testAssertNotNull()
  assertNotNull(true)
end

function testAssertNull()
  assertNull(nil)
end

function testFail()
  fail()
end

function testFailWithMessage()
  fail("Fail with message.")
end


