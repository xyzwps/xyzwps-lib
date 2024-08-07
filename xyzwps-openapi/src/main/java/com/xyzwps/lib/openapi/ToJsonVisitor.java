package com.xyzwps.lib.openapi;

import com.xyzwps.lib.openapi.json.JsonBuilder;

import java.util.Map;
import java.util.TreeSet;

public class ToJsonVisitor implements OAEVisitor {

    private final JsonBuilder json = new JsonBuilder();

    public String toCompactString() {
        return json.toCompactString();
    }

    public String toPrettyString() {
        return json.toPrettyString();
    }

    @Override
    public void visit(Contact contact) {
        json.objectOpen();

        json.value("name").colon().value(contact.name());

        if (contact.url() != null) {
            json.comma();
            json.value("url").colon().value(contact.url());
        }

        if (contact.email() != null) {
            json.comma();
            json.value("email").colon().value(contact.email());
        }

        json.objectClose();
    }

    @Override
    public void visit(OpenApiDocument doc) {
        json.objectOpen();

        json.value("openapi").colon().value(doc.openapi());

        json.comma();
        json.value("info").colon();
        doc.info().accept(this);

        var servers = doc.servers();
        if (servers != null && !servers.isEmpty()) {
            json.comma();
            json.value("servers").colon().arrayOpen();
            int i = 0;
            for (var server : servers) {
                if (i++ > 0) {
                    json.comma();
                }
                server.accept(this);
            }
            json.arrayClose();
        }

        var paths = doc.paths();
        if (paths != null) {
            json.comma();
            json.value("paths").colon();
            paths.accept(this);
        }

        var tags = doc.tags();
        if (tags != null && !tags.isEmpty()) {
            json.comma();
            json.value("tags").colon().arrayOpen();
            int i = 0;
            for (var tag : tags) {
                if (i++ > 0) {
                    json.comma();
                }
                tag.accept(this);
            }
            json.arrayClose();
        }

        var externalDocs = doc.externalDocs();
        if (externalDocs != null) {
            json.comma();
            json.value("externalDocs").colon();
            externalDocs.accept(this);
        }

        json.objectClose();
    }

    @Override
    public void visit(Info info) {
        json.objectOpen();

        json.value("title").colon().value(info.title());

        if (info.summary() != null) {
            json.comma();
            json.value("summary").colon().value(info.summary());
        }

        if (info.description() != null) {
            json.comma();
            json.value("description").colon().value(info.description());
        }

        if (info.termsOfService() != null) {
            json.comma();
            json.value("termsOfService").colon().value(info.termsOfService());
        }

        if (info.contact() != null) {
            json.comma();
            json.value("contact").colon();
            info.contact().accept(this);
        }

        if (info.license() != null) {
            json.comma();
            json.value("license").colon();
            info.license().accept(this);
        }

        json.comma();
        json.value("version").colon().value(info.version());

        json.objectClose();
    }

    @Override
    public void visit(License license) {
        json.objectOpen();

        json.value("name").colon().value(license.name());

        switch (license) {
            case License.IdLicense idLicense -> {
                if (idLicense.identifier() != null) {
                    json.comma();
                    json.value("identifier").colon().value(idLicense.identifier());
                }
            }
            case License.UrlLicense urlLicense -> {
                if (urlLicense.url() != null) {
                    json.comma();
                    json.value("url").colon().value(urlLicense.url());
                }
            }
        }

        json.objectClose();
    }

    @Override
    public void visit(ExternalDocumentation d) {
        json.objectOpen();

        if (d.description() != null) {
            json.value("description").colon().value(d.description()).comma();
        }

        json.value("url").colon().value(d.url());

        json.objectClose();
    }

    @Override
    public void visit(Paths paths) {
        var pathSet = paths.pathSet();
        json.objectOpen();

        for (var path : pathSet) {
            var item = paths.item(path);
            if (item != null) {
                item.accept(this);
            }
        }

        json.objectClose();
    }

    @Override
    public void visit(Server server) {
        json.objectOpen();

        json.value("url").colon().value(server.url());

        if (server.description() != null) {
            json.comma();
            json.value("description").colon().value(server.description());
        }

        var variables = server.variables();
        if (variables != null && !variables.isEmpty()) {
            json.comma();
            json.value("variables").colon().objectOpen();

            var keySet = new TreeSet<>(variables.keySet());
            int i = 0;
            for (var key : keySet) {
                if (i++ > 0) {
                    json.comma();
                }
                json.value(key).colon();
                server.variables().get(key).accept(this);
            }

            json.objectClose();
        }

        json.objectClose();
    }

    @Override
    public void visit(ServerVariable v) {
        json.objectOpen();

        json.value("default").colon().value(v.defaultValue());

        var enums = v.enums();
        if (enums != null && !enums.isEmpty()) {
            json.comma().value("enum").colon().arrayOpen();

            int i = 0;
            for (var e : enums) {
                if (i++ > 0) {
                    json.comma();
                }
                json.value(e);
            }

            json.arrayClose();
        }

        if (v.description() != null) {
            json.comma();
            json.value("description").colon().value(v.description());
        }

        json.objectClose();

    }

    @Override
    public void visit(Tag tag) {
        json.objectOpen();

        json.value("name").colon().value(tag.name());

        if (tag.description() != null) {
            json.comma();
            json.value("description").colon().value(tag.description());
        }

        if (tag.externalDocs() != null) {
            json.comma();
            json.value("externalDocs").colon();
            tag.externalDocs().accept(this);
        }

        json.objectClose();
    }

    @Override
    public void visit(Operation op) {

    }

    @Override
    public void visit(PathItem item) {
        json.objectOpen();

        boolean isFirst = true;

        var $ref = item.$ref();
        if ($ref != null) {
            isFirst = false;
            json.value("$ref").colon().value($ref);
        }

        var summary = item.summary();
        if (summary != null) {
            if (isFirst) isFirst = false;
            else json.comma();

            json.value("summary").colon().value(summary);
        }

        var description = item.description();
        if (description != null) {
            if (isFirst) isFirst = false;
            else json.comma();

            json.value("description").colon().value(description);
        }

        var get = item.get();
        if (get != null) {
            if (isFirst) isFirst = false;
            else json.comma();

            json.value("get").colon();
            get.accept(this);
        }

        var post = item.post();
        if (post != null) {
            if (isFirst) isFirst = false;
            else json.comma();

            json.value("post").colon();
            post.accept(this);
        }

        var put = item.put();
        if (put != null) {
            if (isFirst) isFirst = false;
            else json.comma();

            json.value("put").colon();
            put.accept(this);
        }

        var delete = item.delete();
        if (delete != null) {
            if (isFirst) isFirst = false;
            else json.comma();

            json.value("delete").colon();
            delete.accept(this);
        }

        var head = item.head();
        if (head != null) {
            if (isFirst) isFirst = false;
            else json.comma();

            json.value("head").colon();
            head.accept(this);
        }

        var patch = item.patch();
        if (patch != null) {
            if (isFirst) isFirst = false;
            else json.comma();

            json.value("patch").colon();
            patch.accept(this);
        }

        var trace = item.trace();
        if (trace != null) {
            if (isFirst) isFirst = false;
            else json.comma();

            json.value("trace").colon();
            trace.accept(this);
        }

        var servers = item.servers();
        if (servers != null && !servers.isEmpty()) {
            if (isFirst) isFirst = false;
            else json.comma();

            json.value("servers").colon().arrayOpen();
            int i = 0;
            for (var server : servers) {
                if (i++ > 0) json.comma();
                server.accept(this);
            }
            json.arrayClose();
        }

        var parameters = item.parameters();
        if (parameters != null && !parameters.isEmpty()) {
            if (isFirst) isFirst = false;
            else json.comma();

            json.value("parameters").colon().arrayOpen();
            int i = 0;
            for (var p : parameters) {
                if (i++ > 0) json.comma();
                p.accept(this);
            }
            json.arrayClose();
        }

        json.objectClose();
    }

    @Override
    public void visit(Parameter p) {

    }

    @Override
    public void visit(Reference r) {
        json.objectOpen();
        json.value("$ref").colon().value(r.$ref());
        json.objectClose();
    }

    @Override
    public void visit(Responses response) {
        json.objectOpen();
        boolean isFirst = true;

        var statusResponses = response.statusResponses();
        if (statusResponses != null && !statusResponses.isEmpty()) {
            var keySet = new TreeSet<>(statusResponses.keySet());
            for (var key : keySet) {
                if (isFirst) isFirst = false;
                else json.comma();

                json.value(key + "").colon();
                statusResponses.get(key).accept(this);
            }
        }

        var defaultResponse = response.responseDefault();
        if (defaultResponse != null) {
            if (!isFirst) json.comma();

            json.value("default").colon();
            defaultResponse.accept(this);
        }

        json.objectClose();
    }

    @Override
    public void visit(RequestBody body) {

    }

    @Override
    public void visit(Response it) {

        json.objectOpen();

        json.value("description").colon().value(it.description());

        var headers = it.headers();
        if (headers != null && !headers.isEmpty()) {
            json.comma();
            json.value("headers").colon().objectOpen();
            var keySet = new TreeSet<>(headers.keySet());
            int i = 0;
            for (var key : keySet) {
                if (i++ > 0) json.comma();
                json.value(key).colon();
                headers.get(key).accept(this);
            }
            json.objectClose();
        }

        var content = it.content();
        if (content != null && !content.isEmpty()) {
            json.comma();
            json.value("content").colon().objectOpen();
            var keySet = new TreeSet<>(content.keySet());
            int i = 0;
            for (var key : keySet) {
                if (i++ > 0) json.comma();
                json.value(key).colon();
                var mediaType = content.get(key);
                mediaType.accept(this);
            }
            json.objectClose();
        }

        var links = it.links();
        if (links != null && !links.isEmpty()) {
            json.comma();
            json.value("links").colon().objectOpen();

            var keySet = new TreeSet<>(links.keySet());
            int i = 0;
            for (var key : keySet) {
                if (i++ > 0) json.comma();
                json.value(key).colon();
                links.get(key).accept(this);
            }
            json.objectClose();
        }


        json.objectClose();
    }

    @Override
    public void visit(Link link) {

    }

    @Override
    public void visit(Style style) {

    }

    @Override
    public void visit(Header header) {

    }

    @Override
    public void visit(Schema schema) {
        switch (schema) {
            case Schema.RefSchema it -> {
                json.objectOpen();
                json.value("$ref").colon().value(it.$ref());
                json.objectClose();
            }
            case Schema.ArraySchema it -> {
            }
            case Schema.BooleanSchema it -> {
            }
            case Schema.IntegerSchema it -> {
            }
            case Schema.ObjectSchema it -> {
            }
            case Schema.StringSchema it -> {
            }
            case Schema.EnumSchema it -> {
            }
        }
    }

    @Override
    public void visit(Example example) {
        json.objectOpen();
        boolean isFirst = true;

        var summary = example.summary();
        if (summary != null) {
            isFirst = false;
            json.value("summary").colon().value(summary);
        }

        var description = example.description();
        if (description != null) {
            if (isFirst) isFirst = false;
            else json.comma();

            json.value("description").colon().value(description);
        }

        var value = example.value();
        if (value != null) {
            if (isFirst) isFirst = false;
            else json.comma();

            switch (value) {
                case String s -> json.value("value").colon().value(s);
                case Map x -> {
                    Map<String, String> m = (Map<String, String>) x;
                    json.value("value").colon().objectOpen();
                    var keySet = new TreeSet<>(m.keySet());
                    int i = 0;
                    for (var key : keySet) {
                        if (i++ > 0) json.comma();
                        var v = m.get(key);
                        json.value(key).colon().value(v);
                    }
                    json.objectClose();
                }
                default -> throw new IllegalStateException("Unsupported value type: " +
                                                           value.getClass().getCanonicalName());
            }
        }

        var externalValue = example.externalValue();
        if (externalValue != null) {
            if (isFirst) isFirst = false;
            else json.comma();

            json.value("externalValue").colon().value(externalValue);
        }

        json.objectClose();
    }

    @Override
    public void visit(Encoding encoding) {

    }

    @Override
    public void visit(MediaType it) {
        json.objectOpen();

        var schema = it.schema();
        boolean isFirst = true;
        if (schema != null) {
            isFirst = false;
            json.value("schema").colon();
            schema.accept(this);
        }

        var example = it.example();
        if (example != null) {
            if (isFirst) isFirst = false;
            else json.comma();

            json.value("example").colon().value(example);
        }

        var examples = it.examples();
        if (examples != null && !examples.isEmpty()) {
            if (isFirst) isFirst = false;
            else json.comma();

            json.value("examples").colon().objectOpen();
            var keySet = new TreeSet<>(examples.keySet());
            int i = 0;
            for (var key : keySet) {
                if (i++ > 0) json.comma();
                json.value(key).colon();
                examples.get(key).accept(this);
            }
            json.objectClose();
        }

        var encoding = it.encoding();
        if (encoding != null && !encoding.isEmpty()) {
            if (isFirst) isFirst = false;
            else json.comma();

            json.value("encoding").colon().objectOpen();
            var keySet = new TreeSet<>(encoding.keySet());
            int i = 0;
            for (var key : keySet) {
                if (i++ > 0) json.comma();

                json.value(key).colon();
                var enc = encoding.get(key);
                enc.accept(this);
            }
            json.objectClose();
        }

        json.objectClose();
    }
}
