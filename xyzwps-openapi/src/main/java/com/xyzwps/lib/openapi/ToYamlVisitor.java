package com.xyzwps.lib.openapi;

import java.util.ArrayList;
import java.util.List;
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

    }

    @Override
    public void visit(Parameter p) {

    }

    @Override
    public void visit(Reference r) {

    }

    @Override
    public void visit(RequestBody body) {

    }

    @Override
    public void visit(Responses response) {

    }

    private record Line(int indent, String text) {
    }
}
