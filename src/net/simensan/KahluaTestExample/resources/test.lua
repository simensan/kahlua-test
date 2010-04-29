

function testCreateSquare()
   local square = CreateSquare(10)
   assertNotNull(square)
   local size = square:GetSize()
   assertEquals(size, 10)
end
