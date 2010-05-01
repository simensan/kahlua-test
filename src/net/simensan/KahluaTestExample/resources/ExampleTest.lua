local square, size

function before()
    square = CreateSquare(10)
end


function after()
   assertEquals(size, 10)
end


function testCreateSquare()
    assertNotNull(square)
    size = square:GetSize()
end


function testCreateSquare2()
    local square2 = CreateSquare(12)
    size = square:GetSize()
end


function testLuaOnlyTest()
   assertEquals(size, 10)
end


function testLuaOnlyTest2()
   assertNotNull(square)
end