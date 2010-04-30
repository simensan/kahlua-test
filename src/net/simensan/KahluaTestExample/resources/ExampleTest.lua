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
    assertNotNull(square)
    size = square:GetSize()
end
