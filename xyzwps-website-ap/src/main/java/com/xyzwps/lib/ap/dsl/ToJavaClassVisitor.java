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

        e.getAnnotations().forEach(a -> this.visit(a, false));
        sb.append(e.isPublic() ? "public " : "")
                .append(e.isFinal() ? "final " : "")
                .append("class ").append(className);

        if (!e.getImplementedInterfaces().isEmpty()) {
            sb.append(" implements ");
            for (int j = 0; j < e.getImplementedInterfaces().size(); j++) {
                var itf = e.getImplementedInterfaces().get(j);
                if (j > 0) sb.append(", ");
                sb.append(itf.getClassName());
            }
        }

        sb.append(" {\n");

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

        e.getMethods().forEach(this::visit);

        sb.append("}\n");
    }

    @Override
    public void visit(FieldElement e) {
        e.getAnnotations().forEach(a -> this.visit(a, false));
        var type = e.getType();
        appendTabs()
                .append(switch (e.getAccessLevel()) {
                    case PUBLIC -> "public ";
                    case PRIVATE -> "private ";
                    case PROTECTED -> "protected ";
                    case PACKAGE -> "";
                })
                .append(e.isStatic() ? "static " : "")
                .append(e.isFinal() ? "final " : "")
                .append(type.getClassName()).append(" ").append(e.getName()).append(";\n");
    }

    @Override
    public void visit(AnnotationElement e, boolean inline) {
        if (inline) {
            sb.append("@").append(e.getType().getClassName()).append("(");
            // TODO: values
            sb.append(") ");
        } else {

            appendTabs()
                    .append("@").append(e.getType().getClassName()).append("(");
            // TODO: values
            sb.append(")\n");
        }
    }

    @Override
    public void visit(MethodElement e) {
        var returnType = e.getReturnType() == null ? "void" : e.getReturnType().getClassName();
        e.getAnnotations().forEach(a -> this.visit(a, false));
        appendTabs()
                .append(switch (e.getAccessLevel()) {
                    case PACKAGE -> " ";
                    case PUBLIC -> "public ";
                    case PRIVATE -> "private ";
                    case PROTECTED -> "protected ";
                })
                .append(e.isStatic() ? "static " : "")
                .append(returnType).append(" ").append(e.getName()).append("(");
        for (int i = 0; i < e.getArguments().size(); i++) {
            var arg = e.getArguments().get(i);
            if (i > 0) sb.append(", ");

            arg.getAnnotations().forEach(a -> this.visit(a, true));
            sb.append(arg.getType().getClassName()).append(" ").append(arg.getName());
        }
        sb.append(") {\n");
        this.tabs++;
        e.getLines().forEach(l -> appendTabs().append(l).append("\n"));
        this.tabs--;
        appendTabs().append("}\n");
    }

    @Override
    public void visit(FullTypeNameElement fullTypeName) {

    }

    public void visit(ArgumentElement argumentElement) {
    }

    public String toJavaClass() {
        return sb.toString();
    }
}
