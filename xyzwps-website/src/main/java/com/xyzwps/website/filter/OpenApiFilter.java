package com.xyzwps.website.filter;

import com.xyzwps.lib.express.Filter;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.lib.http.HttpMethod;
import com.xyzwps.lib.openapi.ApisAdder;
import com.xyzwps.lib.openapi.OpenApiDocument;
import com.xyzwps.lib.openapi.Info;
import com.xyzwps.lib.openapi.ToJsonVisitor;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class OpenApiFilter implements Filter {

    private final OpenApiDocument doc;

    public OpenApiFilter(List<ApisAdder> adders) {
        var info = new Info("xyzwps-website api", "0.0.1"); // TODO: 如何获取 maven 项目的版本号
        this.doc = new OpenApiDocument(info);
        adders.forEach(it -> it.add(doc));
    }

    @Override
    public void filter(HttpRequest request, HttpResponse response, Next next) {
        if (request.method() == HttpMethod.GET && request.path().equals("/openapi.json")) {
            response.ok();
            response.headers().set("Content-Type", "application/json");
            var toJsonVisitor = new ToJsonVisitor();
            doc.accept(toJsonVisitor);
            response.send(toJsonVisitor.toCompactString().getBytes());
        } else {
            next.next(request, response);
        }
    }
}
