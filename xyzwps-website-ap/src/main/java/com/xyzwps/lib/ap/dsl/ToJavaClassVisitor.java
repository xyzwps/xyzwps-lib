package com.xyzwps.lib.ap.dsl;

public class ToJavaClassVisitor implements ElementVisitor {

    private final StringBuilder sb = new StringBuilder();

    private int tabs = 0;
    private StringBuilder appendTabs() {
        for (int i = 0; i < tabs; i++) {
            sb.append("    ");
        }
        return sb;
    }

    @Override
    public void visit(ClassElement e) {
        sb.append("package ").append(e.getPackageName()).append(";\n");
        sb.append("\n");

        var imports = new GetImportsVisitor();
        e.visit(imports);
        imports.getImports().forEach(i -> sb.append("import ").append(i).append(";\n"));
        sb.append("\n");

        e.getAnnotations().forEach(this::visit);
        sb.append(e.isPublic() ? "public " : "")
                .append(e.isFinal() ? "final " : "")
                .append("class ").append(e.getClassName()).append(" {\n");

        this.tabs++;
        e.getFields().forEach(this::visit);

        sb.append("}\n");
    }

    @Override
    public void visit(FieldElement e) {
        e.getAnnotations().forEach(this::visit);
        appendTabs()
                .append(e.isPrivate() ? "private " : "")
                .append(e.isStatic() ? "static " : "")
                .append(e.isFinal() ? "final " : "")
                .append(e.getTypeClassName()).append(" ").append(e.getName()).append(";\n");
    }

    @Override
    public void visit(AnnotationElement e) {
        appendTabs()
                .append("@").append(e.getAnnoClassName()).append("(");
        // TODO: values
        sb.append(")\n");
    }

    public String toJavaClass() {
        return sb.toString();
    }
}
