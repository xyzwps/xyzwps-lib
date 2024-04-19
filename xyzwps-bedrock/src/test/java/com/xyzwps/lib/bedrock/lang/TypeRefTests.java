package com.xyzwps.lib.bedrock.lang;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class TypeRefTests {

    @Test
    void test() throws Exception {
        var ref = new TypeRef<List<Map<String, Integer>>>() {
        };


        System.out.println("----------- " + ref.type);
    }

}
