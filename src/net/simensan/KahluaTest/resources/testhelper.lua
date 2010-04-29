function assertEquals(obj1, obj2)
    assertParamTypesEqual(obj1, obj2)

    if obj1 ~= obj2 then
        throwNewAssertionFailedError(" expected:<" .. tostring(obj1) .."> but was:<" .. tostring(obj2) ..">")
    end
end

function assertNotEquals(obj1, obj2)
    assertParamTypesEqual(obj1, obj2)

    if obj1 == obj2 then
        throwNewAssertionFailedError(" expected:<" .. tostring(obj1) .."> but was:<" .. tostring(obj2) ..">")
    end
end

function assertNotNull(obj)
    if obj == nil then
        throwNewAssertionFailedError(" expected not null but got:<" .. tostring(obj) ..">")    
    end
end

function assertNull(obj)

end

function assertSame(obj)

end


function assertNotSame(obj)

end


function assertSame(obj)

end

function assertParamTypesEqual(param1, param2)
    if(type(param1) ~= type(param2)) then
        throwNewAssertionFailedError( "Param1 of type '" .. type(param1) ..
                                      "' does not match type of param2: '" .. type(param2) .."'")
    end
end