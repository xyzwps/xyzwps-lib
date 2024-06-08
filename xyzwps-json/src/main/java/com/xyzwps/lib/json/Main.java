package com.xyzwps.lib.json;

import java.util.List;

public class Main {
    public static void main(String[] args) {


        var om = new ObjectMapper();

        {
            var tree = new TreeNode(1, "区域", List.of(
                    new TreeNode(2, "璃月", List.of(
                            new TreeNode(3, "刻晴", List.of()),
                            new TreeNode(4, "香菱", List.of())
                    )),
                    new TreeNode(5, "枫丹", List.of(
                            new TreeNode(6, "芙宁娜", null),
                            new TreeNode(7, "娜维娅", null)
                    ))
            ));

            System.out.println(om.stringify(tree));

            var json = """
                    { "id":1,"title":"区域", "children": [
                        { "id":2,"title":"璃月", "children":[
                            {"children":[],"id":3,"title":"刻晴"},
                            {"children":[],"id":4,"title":"香菱"}
                        ]},
                        { "id":5,"title":"枫丹", "children":[
                            {"children":null,"id":6,"title":"芙宁娜"},
                            {"children":null,"id":7,"title":"娜维娅"}
                        ]}
                    ]}
                    """;
            System.out.println(om.parse(json, TreeNode.class));
        }

    }

    public static class Image {
        private int width;
        private int height;
        private Point[] points;
    }

    public record Point(int x, int y) {
    }

    public static class TreeNode {
        private int id;
        private String title;
        private List<TreeNode> children;

        public TreeNode() {
        }

        public TreeNode(int id, String title, List<TreeNode> children) {
            this.id = id;
            this.title = title;
            this.children = children;
        }

        @Override
        public String toString() {
            return String.format("TreeNode{id=%d, title='%s', children=%s}", id, title, children);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<TreeNode> getChildren() {
            return children;
        }

        public void setChildren(List<TreeNode> children) {
            this.children = children;
        }
    }
}
