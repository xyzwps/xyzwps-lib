package com.xyzwps.lib.openapi;

public interface OAEVisitor {

    void visit(Contact contact);

    void visit(OpenApiDocument doc);

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

    void visit(Response response);

    void visit(Header header);

    void visit(Schema schema);

    void visit(Encoding encoding);

    void visit(MediaType mediaType);

    void visit(Example example);

    void visit(Style style);

    void visit(Link link);
}
