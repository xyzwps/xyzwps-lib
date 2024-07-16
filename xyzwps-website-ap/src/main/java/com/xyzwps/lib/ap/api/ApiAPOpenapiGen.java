package com.xyzwps.lib.ap.api;

import javax.lang.model.element.TypeElement;

public class ApiAPOpenapiGen extends ApiAPGen {

    public ApiAPOpenapiGen(TypeElement typeElement) {
        super(typeElement);
    }

    public void generate() {
        var methods = collectApiMethods();
        for (var method : methods) {
            var apiInfo = ApiInfo.from(method);
            System.out.println("🚩ApiAPOpenapiGen: " + apiInfo);
        }
    }
}
