package com.xyzwps.lib.ap.dsl;

public class ToJavaClassVisitor implements ElementVisitor {

    private final StringBuilder sb = new StringBuilder();

    private int tabs = 0;

    private StringBuilder appendTabs() {
        sb.append("    ".repeat(Math.max(0, tabs)));
        return sb;
    }

    @Override
    public void visit(ClassElement e) {
        sb.append("package ").append(e.getType().getPackageName()).append(";\n");
        sb.append("\n");

        var className = e.getType().getClassName();

        var imports = new GetImportsVisitor();
        e.visit(imports);
        imports.getImports().forEach(i -> sb.append("import ").append(i).append(";\n"));
        sb.append("\n");

        e.getAnnotations().forEach(this::visit);
        sb.append(e.isPublic() ? "public " : "")
                .append(e.isFinal() ? "final " : "")
                .append("class ").append(className).append(" {\n");

        this.tabs++;
        e.getFields().forEach(this::visit);

        sb.append("\n");

        appendTabs().append("public ").append(className).append("(");
        for (int i = 0; i < e.getFields().size(); i++) {
            var f = e.getFields().get(i);
            if (i > 0) sb.append(", ");

            sb.append(f.getType().getClassName()).append(" ").append(f.getName());
        }
        sb.append(") {\n");
        this.tabs++;
        for (var f : e.getFields()) {
            appendTabs().append("this.").append(f.getName()).append(" = ").append(f.getName()).append(";\n");
        }
        this.tabs--;
        appendTabs().append("}\n");

        sb.append("}\n");
    }

    @Override
    public void visit(FieldElement e) {
        e.getAnnotations().forEach(this::visit);
        var type = e.getType();
        appendTabs()
                .append(e.isPrivate() ? "private " : "")
                .append(e.isStatic() ? "static " : "")
                .append(e.isFinal() ? "final " : "")
                .append(type.getClassName()).append(" ").append(e.getName()).append(";\n");
    }

    @Override
    public void visit(AnnotationElement e) {
        appendTabs()
                .append("@").append(e.getType().getClassName()).append("(");
        // TODO: values
        sb.append(")\n");
    }

    @Override
    public void visit(MethodElement methodElement) {

    }

    @Override
    public void visit(FullTypeNameElement fullTypeName) {

    }

    public String toJavaClass() {
        return sb.toString();
    }
}
