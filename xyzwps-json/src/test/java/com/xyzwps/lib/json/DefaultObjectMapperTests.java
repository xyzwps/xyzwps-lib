package com.xyzwps.lib.json;

import com.xyzwps.lib.bedrock.lang.Equals;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.json.JsonUtils.*;

class DefaultObjectMapperTests {

    static final ObjectMapper OM = new ObjectMapper();

    @Nested
    class StringifyAndParseTests {

        @Test
        void testRecord() {
            var p = new Point(1, 2);
            var json = OM.stringify(p);
            assertEquals("{\"x\":1,\"y\":2}", json);
            var parsed = OM.parse(json, Point.class);
            assertEquals(p, parsed);
        }

        @Test
        void testList() {
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

            var json = OM.stringify(tree);
            assertTrue(jsonEquals(json, """
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
                    """));

            var parsed = OM.parse(json, TreeNode.class);
            assertEquals(tree, parsed);
        }

        @Test
        void testObjectArray() {
            var image = new Image(1366, 768, new Point[]{
                    new Point(1, 2),
                    new Point(100, 200),
            });
            var json = OM.stringify(image);
            assertTrue(jsonEquals(json, """
                    {
                        "width": 1366,
                        "height": 768,
                        "points": [
                            { "x": 1,   "y": 2 },
                            { "x": 100, "y": 200 }
                        ]
                    }
                    """));
            var parsed = OM.parse(json, Image.class);
            assertEquals(image, parsed);
        }

        @Test
        void testPrimitiveArray() {
            var arr = new PrimitiveArrays(
                    new boolean[]{true, false},
                    new short[]{1, 2, 3},
                    new int[]{1, 2, 3},
                    new long[]{1, 2, 3},
                    new float[]{1.1f, 2.2f, 3.3f},
                    new double[]{1.1, 2.2, 3.3},
                    new char[]{'a', 'b', 'c'}
            );
            var json = OM.stringify(arr);
            assertTrue(jsonEquals(json, """
                    {
                        "bools": [true,false],
                        "longs":[1,2,3],
                        "floats":[1.100000023841858,2.200000047683716,3.299999952316284],
                        "ints":[1,2,3],
                        "doubles":[1.1,2.2,3.3],
                        "shorts":[1,2,3],
                        "chars":["a","b","c"]
                    }
                    """));
            var parsed = OM.parse(json, PrimitiveArrays.class);
            assertArrayEquals(arr.bools, parsed.bools);
            assertArrayEquals(arr.shorts, parsed.shorts);
            assertArrayEquals(arr.ints, parsed.ints);
            assertArrayEquals(arr.longs, parsed.longs);
            assertArrayEquals(arr.floats, parsed.floats, 0.0001f);
            assertArrayEquals(arr.doubles, parsed.doubles, 0.0001);
            assertArrayEquals(arr.chars, parsed.chars);
        }
    }

    public static record PrimitiveArrays(
            boolean[] bools,
            short[] shorts,
            int[] ints,
            long[] longs,
            float[] floats,
            double[] doubles,
            char[] chars
    ) {
    }


    public record Point(int x, int y) {
    }

    public static class Image {
        private int width;
        private int height;
        private Point[] points;

        public Image() {
        }

        public Image(int width, int height, Point[] points) {
            this.width = width;
            this.height = height;
            this.points = points;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj == this) return true;
            if (obj instanceof Image that) {
                return this.width == that.width
                       && this.height == that.height
                       && Equals.arrayEquals(this.points, that.points);
            }
            return false;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public Point[] getPoints() {
            return points;
        }

        public void setPoints(Point[] points) {
            this.points = points;
        }
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
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj == this) return true;
            if (obj instanceof TreeNode that) {
                return this.id == that.id
                       && this.title.equals(that.title)
                       && Equals.itemEquals(this.children, that.children);
            }
            return false;
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
