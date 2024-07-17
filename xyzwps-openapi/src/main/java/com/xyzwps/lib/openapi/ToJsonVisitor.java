package com.xyzwps.lib.openapi;

import com.xyzwps.lib.openapi.json.JsonBuilder;

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
    public void visit(Document doc) {
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
                switch (p) {
                    case Parameter it -> it.accept(this);
                    case Reference it -> it.accept(this);
                    default -> throw new IllegalStateException("Unsupported parameter type: " +
                                                               p.getClass().getCanonicalName());
                }
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

    }

    @Override
    public void visit(Responses response) {

    }

    @Override
    public void visit(RequestBody body) {

    }

    @Override
    public void visit(Response response) {

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

    }

    @Override
    public void visit(Example example) {

    }

    @Override
    public void visit(Encoding encoding) {

    }

    @Override
    public void visit(MediaType mediaType) {

    }
}
