package com.xyzwps.lib.ap.api;

import com.xyzwps.lib.ap.*;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.MirroredTypesException;
import java.util.ArrayList;
import java.util.List;

public record ApiInfo(String method, String path, List<String> filters, ApiMethodArguments arguments) {
    public ApiInfo {
        switch (method) {
            case "get", "post", "put", "delete", "patch" -> {
            }
            default -> throw new IllegalArgumentException("Invalid method: " + method);
        }
    }

    public static ApiInfo from(ExecutableElement method) {
        List<String> filters = new ArrayList<>();

        var get = method.getAnnotation(GET.class);
        if (get != null) {
            try {
                for (var filter : get.filters()) {
                    filters.add(filter.getCanonicalName());
                }
            } catch (MirroredTypesException e) {
                for (var type : e.getTypeMirrors()) {
                    filters.add(type.toString());
                }
            }
            return new ApiInfo("get", get.value(), filters, ApiMethodArguments.from(method));
        }

        var post = method.getAnnotation(POST.class);
        if (post != null) {
            try {
                for (var filter : post.filters()) {
                    filters.add(filter.getCanonicalName());
                }
            } catch (MirroredTypesException e) {
                for (var type : e.getTypeMirrors()) {
                    filters.add(type.toString());
                }
            }
            return new ApiInfo("post", post.value(), filters, ApiMethodArguments.from(method));
        }

        var put = method.getAnnotation(PUT.class);
        if (put != null) {
            try {
                for (var filter : put.filters()) {
                    filters.add(filter.getCanonicalName());
                }
            } catch (MirroredTypesException e) {
                for (var type : e.getTypeMirrors()) {
                    filters.add(type.toString());
                }
            }
            return new ApiInfo("put", put.value(), filters, ApiMethodArguments.from(method));
        }

        var delete = method.getAnnotation(DELETE.class);
        if (delete != null) {
            try {
                for (var filter : delete.filters()) {
                    filters.add(filter.getCanonicalName());
                }
            } catch (MirroredTypesException e) {
                for (var type : e.getTypeMirrors()) {
                    filters.add(type.toString());
                }
            }
            return new ApiInfo("delete", delete.value(), filters, ApiMethodArguments.from(method));
        }

        var patch = method.getAnnotation(PATCH.class);
        if (patch != null) {
            try {
                for (var filter : patch.filters()) {
                    filters.add(filter.getCanonicalName());
                }
            } catch (MirroredTypesException e) {
                for (var type : e.getTypeMirrors()) {
                    filters.add(type.toString());
                }
            }
            return new ApiInfo("patch", patch.value(), filters, ApiMethodArguments.from(method));
        }

        // TODO: more methods
        throw new RuntimeException("Maybe a bug!");
    }


}
