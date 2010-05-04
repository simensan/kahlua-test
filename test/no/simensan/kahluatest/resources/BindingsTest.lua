local square, size

function before()
    square = CreateSquare(10)
end


function after()
   size = square:GetSize()
   assertEquals(10, size)
end


function testCreateSquare()
    assertNotNull(square)
end


function testCreateSquare2()
    local square2 = CreateSquare(12)
end


function testLuaOnlyTest()
   assertNull(blah)
end


function testLuaOnlyTest2()
   assertNotNull(square)
end