package com.xyzwps.lib.openapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class ToYamlVisitor implements OAEVisitor {

    private final List<Line> lines = new ArrayList<>();

    private int indent = 0;

    public String toYaml() {
        var sb = new StringBuilder();
        boolean nextIsItem = false;
        for (var line : lines) {
            if (line == null) {
                nextIsItem = true;
            } else {
                if (nextIsItem) {
                    nextIsItem = false;
                    addIndent(sb, line.indent - 1);
                    sb.append("- ");
                    sb.append(line.text).append("\n");
                } else {
                    addIndent(sb, line.indent);
                    sb.append(line.text).append("\n");
                }
            }
        }
        return sb.toString();
    }

    private static void addIndent(StringBuilder sb, int indent) {
        if (indent < 0) return;

        sb.append("  ".repeat(indent));
    }

    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[1-9][0-9]*$");

    private static String unescape(String s) {
        if (NUMBER_PATTERN.matcher(s).matches()) {
            return '\'' + s + "'";
        }

        // TODO: unescape
        return s;
    }

    @Override
    public void visit(Contact contact) {
        lines.add(new Line(indent, "name: " + unescape(contact.name())));

        if (contact.url() != null) {
            lines.add(new Line(indent, "url: " + unescape(contact.url())));
        }

        if (contact.email() != null) {
            lines.add(new Line(indent, "email: " + unescape(contact.email())));
        }
    }

    @Override
    public void visit(Document doc) {
        lines.add(new Line(indent, "openapi: " + doc.openapi()));

        lines.add(new Line(indent, "info:"));
        indent++;
        doc.info().accept(this);
        indent--;

        var servers = doc.servers();
        if (servers != null && !servers.isEmpty()) {
            lines.add(new Line(indent, "servers:"));
            indent++;
            for (var server : servers) {
                lines.add(null);
                server.accept(this);
            }
            indent--;
        }

        var paths = doc.paths();
        if (paths != null) {
            indent++;
            paths.accept(this);
            indent--;
        }

        var tags = doc.tags();
        if (tags != null && !tags.isEmpty()) {
            lines.add(new Line(indent, "tags:"));
            indent++;
            for (var tag : tags) {
                lines.add(null);
                tag.accept(this);
            }
            indent--;
        }

        var externalDocs = doc.externalDocs();
        if (externalDocs != null) {
            lines.add(new Line(indent, "externalDocs:"));
            indent++;
            externalDocs.accept(this);
            indent--;
        }
    }

    @Override
    public void visit(Info info) {
        lines.add(new Line(indent, "title: " + unescape(info.title())));

        if (info.summary() != null) {
            lines.add(new Line(indent, "summary: " + unescape(info.summary())));
        }

        if (info.description() != null) {
            lines.add(new Line(indent, "description: " + unescape(info.description())));
        }

        if (info.termsOfService() != null) {
            lines.add(new Line(indent, "termsOfService: " + unescape(info.termsOfService())));
        }

        if (info.contact() != null) {
            lines.add(new Line(indent, "contact:"));
            indent++;
            info.contact().accept(this);
            indent--;
        }

        if (info.license() != null) {
            lines.add(new Line(indent, "license:"));
            indent++;
            info.license().accept(this);
            indent--;
        }

        lines.add(new Line(indent, "version: " + unescape(info.version())));
    }

    @Override
    public void visit(License license) {

        lines.add(new Line(indent, "name: " + unescape(license.name())));

        switch (license) {
            case License.IdLicense idLicense -> {
                if (idLicense.identifier() != null) {
                    lines.add(new Line(indent, "identifier: " + unescape(idLicense.identifier())));
                }
            }
            case License.UrlLicense urlLicense -> {
                if (urlLicense.url() != null) {
                    lines.add(new Line(indent, "url: " + unescape(urlLicense.url())));
                }
            }
        }
    }

    @Override
    public void visit(ExternalDocumentation d) {
        if (d.description() != null) {
            lines.add(new Line(indent, "description: " + unescape(d.description())));
        }

        lines.add(new Line(indent, "url: " + unescape(d.url())));
    }

    @Override
    public void visit(Paths paths) {
        var pathSet = paths.pathSet();
        for (var path : pathSet) {
            var item = paths.item(path);
            if (item != null) {
                lines.add(new Line(indent, path + ":"));
                indent++;
                item.accept(this);
                indent--;
            }
        }
    }

    @Override
    public void visit(Server server) {
        lines.add(new Line(indent, "url: " + unescape(server.url())));

        if (server.description() != null) {
            lines.add(new Line(indent, "description: " + unescape(server.description())));
        }

        var variables = server.variables();
        if (variables != null && !variables.isEmpty()) {
            lines.add(new Line(indent, "variables:"));

            var keySet = new TreeSet<>(variables.keySet());
            for (var key : keySet) {
                indent++;
                lines.add(new Line(indent, key + ":"));
                indent++;
                variables.get(key).accept(this);
                indent -= 2;
            }
        }
    }

    @Override
    public void visit(ServerVariable v) {
        lines.add(new Line(indent, "default: " + unescape(v.defaultValue())));

        var enums = v.enums();
        if (enums != null && !enums.isEmpty()) {
            lines.add(new Line(indent, "enum:"));

            indent += 2;
            for (var e : enums) {
                lines.add(null);
                lines.add(new Line(indent, unescape(e)));
            }
            indent -= 2;
        }

        if (v.description() != null) {
            lines.add(new Line(indent, "description: " + unescape(v.description())));
        }
    }

    @Override
    public void visit(Tag tag) {
        lines.add(new Line(indent, "name: " + unescape(tag.name())));

        if (tag.description() != null) {
            lines.add(new Line(indent, "description: " + unescape(tag.description())));
        }

        if (tag.externalDocs() != null) {
            lines.add(new Line(indent, "externalDocs:"));
            indent++;
            tag.externalDocs().accept(this);
            indent--;
        }
    }

    @Override
    public void visit(Operation op) {

    }

    @Override
    public void visit(PathItem item) {

        var $ref = item.$ref();
        if ($ref != null) {
            lines.add(new Line(indent, "$ref: " + unescape($ref)));
        }

        var summary = item.summary();
        if (summary != null) {
            lines.add(new Line(indent, "summary: " + unescape(summary)));
        }

        var description = item.description();
        if (description != null) {
            lines.add(new Line(indent, "description: " + unescape(description)));
        }

        var get = item.get();
        if (get != null) {
            lines.add(new Line(indent, "get:"));
            indent++;
            get.accept(this);
            indent--;
        }

        var post = item.post();
        if (post != null) {
            lines.add(new Line(indent, "post:"));
            indent++;
            post.accept(this);
            indent--;
        }

        var put = item.put();
        if (put != null) {
            lines.add(new Line(indent, "put:"));
            indent++;
            put.accept(this);
            indent--;
        }

        var delete = item.delete();
        if (delete != null) {
            lines.add(new Line(indent, "delete:"));
            indent++;
            delete.accept(this);
            indent--;
        }

        var head = item.head();
        if (head != null) {
            lines.add(new Line(indent, "head:"));
            indent++;
            head.accept(this);
            indent--;
        }

        var patch = item.patch();
        if (patch != null) {
            lines.add(new Line(indent, "patch:"));
            indent++;
            patch.accept(this);
            indent--;
        }

        var trace = item.trace();
        if (trace != null) {
            lines.add(new Line(indent, "trace:"));
            indent++;
            trace.accept(this);
            indent--;
        }

        var servers = item.servers();
        if (servers != null && !servers.isEmpty()) {
            lines.add(new Line(indent, "servers:"));
            indent++;
            for (var server : servers) {
                lines.add(null);
                server.accept(this);
            }
            indent--;
        }

        var parameters = item.parameters();
        if (parameters != null && !parameters.isEmpty()) {
            lines.add(new Line(indent, "parameters:"));
            indent++;
            for (var p : parameters) {
                lines.add(null);
                switch (p) {
                    case Parameter it -> it.accept(this);
                    case Reference it -> it.accept(this);
                    default -> throw new IllegalStateException("Unsupported parameter type: " +
                            p.getClass().getCanonicalName());
                }
            }
        }
    }


    @Override
    public void visit(Parameter p) {

    }

    @Override
    public void visit(Reference r) {
        lines.add(new Line(indent, "$ref: '" + r.$ref() + "'"));

    }

    @Override
    public void visit(RequestBody body) {

    }

    @Override
    public void visit(Responses response) {

    }

    @Override
    public void visit(Response it) {
        lines.add(new Line(indent, "description: " + unescape(it.description())));

        var headers = it.headers();
        if (headers != null && !headers.isEmpty()) {
            lines.add(new Line(indent, "headers:"));
            indent++;
            var keySet = new TreeSet<>(headers.keySet());
            for (var key : keySet) {
                lines.add(new Line(indent, key + ":"));
                indent++;
                var header = headers.get(key);
                switch (header) {
                    case Header h -> h.accept(this);
                    case Reference r -> r.accept(this);
                    default -> throw new IllegalStateException("Unsupported header type: " +
                            header.getClass().getCanonicalName());
                }
                indent--;
            }
            indent--;
        }

        var content = it.content();
        if (content != null && !content.isEmpty()) {
            lines.add(new Line(indent, "content:"));
            indent++;
            var keySet = new TreeSet<>(content.keySet());
            for (var key : keySet) {
                lines.add(new Line(indent, key + ":"));
                indent++;
                var mediaType = content.get(key);
                mediaType.accept(this);
                indent--;
            }
            indent--;
        }

        var links = it.links();
        if (links != null && !links.isEmpty()) {
            lines.add(new Line(indent, "links:"));
            indent++;
            var keySet = new TreeSet<>(links.keySet());
            for (var key : keySet) {
                lines.add(new Line(indent, key + ":"));
                indent++;
                var link = links.get(key);
                switch (link) {
                    case Link l -> l.accept(this);
                    case Reference r -> r.accept(this);
                    default -> throw new IllegalStateException("Unsupported link type: " +
                            link.getClass().getCanonicalName());
                }
                indent--;
            }
            indent--;
        }
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
                lines.add(new Line(indent, "$ref: '" + it.$ref() + "'"));
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
        var summary = example.summary();
        if (summary != null) {
            lines.add(new Line(indent, "summary: " + unescape(summary)));
        }

        var description = example.description();
        if (description != null) {
            lines.add(new Line(indent, "description: " + unescape(description)));
        }

        var value = example.value();
        if (value != null) {
            switch (value) {
                case String s -> lines.add(new Line(indent, "value: " + unescape(s)));
                case Map x -> {
                    Map<String, String> m = (Map<String, String>) x;
                    lines.add(new Line(indent, "value:"));
                    indent++;
                    var keySet = new TreeSet<>(m.keySet());
                    for (var key : keySet) {
                        var v = m.get(key);
                        lines.add(new Line(indent, key + ": " + unescape(v)));
                    }
                    indent--;
                }
                default -> throw new IllegalStateException("Unsupported value type: " +
                        value.getClass().getCanonicalName());
            }
        }

        var externalValue = example.externalValue();
        if (externalValue != null) {
            lines.add(new Line(indent, "externalValue: " + unescape(externalValue)));
        }
    }

    @Override
    public void visit(Encoding encoding) {

    }

    @Override
    public void visit(MediaType it) {
        var schema = it.schema();
        if (schema != null) {
            lines.add(new Line(indent, "schema:"));
            indent++;
            schema.accept(this);
            indent--;
        }

        var example = it.example();
        if (example != null) {
            lines.add(new Line(indent, "example: " + unescape(example)));
        }

        var examples = it.examples();
        if (examples != null && !examples.isEmpty()) {
            lines.add(new Line(indent, "examples:"));
            indent++;
            var keySet = new TreeSet<>(examples.keySet());
            for (var key : keySet) {
                lines.add(new Line(indent, key + ":"));
                indent++;
                var exam = examples.get(key);
                switch (exam) {
                    case Example e -> e.accept(this);
                    case Reference r -> r.accept(this);
                    default -> throw new IllegalStateException("Unsupported example type: " +
                            exam.getClass().getCanonicalName());
                }
                indent--;
            }
            indent--;
        }

        var encoding = it.encoding();
        if (encoding != null && !encoding.isEmpty()) {
            lines.add(new Line(indent, "encoding:"));
            indent++;
            var keySet = new TreeSet<>(encoding.keySet());
            for (var key : keySet) {
                lines.add(new Line(indent, key + ":"));
                indent++;
                var enc = encoding.get(key);
                enc.accept(this);
                indent--;
            }
            indent--;
        }
    }

    private record Line(int indent, String text) {
    }
}
