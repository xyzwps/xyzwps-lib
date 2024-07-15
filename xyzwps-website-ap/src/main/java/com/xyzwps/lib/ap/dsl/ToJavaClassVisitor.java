package com.xyzwps.lib.ap.dsl;

public class ToJavaClassVisitor implements ElementVisitor {

    private final StringBuilder sb = new StringBuilder();

    private int tabs = 0;

    private StringBuilder appendTabs() {
        sb.append("    ".repeat(Math.max(0, tabs)));
        return sb;
    }

    @Override
    public void visit($Class e) {
        sb.append("package ").append(e.getType().packageName()).append(";\n");
        sb.append("\n");

        var className = e.getType().className();

        var imports = new GetImportsVisitor();
        e.visit(imports);
        imports.getImports().forEach(i -> sb.append("import ").append(i).append(";\n"));
        sb.append("\n");

        e.getAnnotations().forEach(a -> {
            a.visit(this);
            sb.append("\n");
        });
        sb.append(e.getAccessLevel().toSourceCode())
                .append(e.isFinal() ? "final " : "")
                .append("class ").append(className);

        if (!e.getImplementedInterfaces().isEmpty()) {
            sb.append(" implements ");
            for (int j = 0; j < e.getImplementedInterfaces().size(); j++) {
                var itf = e.getImplementedInterfaces().get(j);
                if (j > 0) sb.append(", ");
                sb.append(itf.className());
            }
        }

        sb.append(" {\n");

        this.tabs++;
        var fields = e.getFields();
        fields.forEach(value -> value.visit(this));

        sb.append("\n");

        appendTabs().append("public ").append(className).append("(");
        for (int i = 0; i < fields.size(); i++) {
            var f = fields.get(i);
            if (i > 0) sb.append(", ");

            sb.append(f.getType().className()).append(" ").append(f.getName());
        }
        sb.append(") {\n");
        this.tabs++;
        for (var f : fields) {
            appendTabs().append("this.").append(f.getName()).append(" = ").append(f.getName()).append(";\n");
        }
        this.tabs--;
        appendTabs().append("}\n\n");

        e.getMethods().forEach(this::visit);

        sb.append("}\n");
    }

    @Override
    public void visit($Field e) {
        e.getAnnotations().forEach(this::visit);
        var type = e.getType();
        appendTabs()
                .append(e.getAccessLevel().toSourceCode())
                .append(e.isStatic() ? "static " : "")
                .append(e.isFinal() ? "final " : "")
                .append(type.className()).append(" ").append(e.getName()).append(";\n");
    }

    @Override
    public void visit($Annotation e) {
        sb.append("@").append(e.getType().className()).append("(");
        // TODO: values
        sb.append(") ");
    }

    @Override
    public void visit($Method e) {
        var returnType = e.getReturnType() == null ? "void" : e.getReturnType().className();
        e.getAnnotations().forEach(this::visit);
        appendTabs()
                .append(e.getAccessLevel().toSourceCode())
                .append(e.isStatic() ? "static " : "")
                .append(returnType).append(" ").append(e.getName()).append("(");
        for (int i = 0; i < e.getArguments().size(); i++) {
            var arg = e.getArguments().get(i);
            if (i > 0) sb.append(", ");

            arg.getAnnotations().forEach(this::visit);
            sb.append(arg.getType().className()).append(" ").append(arg.getName());
        }
        sb.append(") {\n");
        this.tabs++;
        e.getLines().forEach(l -> appendTabs().append(l).append("\n"));
        this.tabs--;
        appendTabs().append("}\n");
    }

    @Override
    public void visit($Type fullTypeName) {

    }

    public void visit($Arg $Arg) {
    }

    public String toJavaClass() {
        return sb.toString();
    }
}
