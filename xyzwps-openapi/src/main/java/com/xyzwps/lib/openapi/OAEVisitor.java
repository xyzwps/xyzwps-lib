package com.xyzwps.lib.openapi;

public interface OAEVisitor {

    void visit(Contact contact);

    void visit(Document doc);

    void visit(Info info);

    void visit(License license);

    void visit(ExternalDocumentation externalDocumentation);

    void visit(Paths paths);

    void visit(PathItem item);

    void visit(Operation op);

    void visit(Server server);

    void visit(ServerVariable serverVariable);

    void visit(Tag tag);

    void visit(Parameter p);

    void visit(Reference r);

    void visit(RequestBody body);

    void visit(Responses response);
}
