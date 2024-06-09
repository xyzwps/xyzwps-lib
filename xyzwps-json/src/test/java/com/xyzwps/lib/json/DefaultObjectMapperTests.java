package com.xyzwps.lib.json;

import com.xyzwps.lib.bedrock.lang.Equals;
import lombok.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.json.JsonUtils.*;

class DefaultObjectMapperTests {

    static final ObjectMapper OM = new ObjectMapper();

    @Nested
    class StringifyAndParseTests {

        @Test
        void test() {
            var obj = StringifyAndParseIntros.builder()
                    .forPojo(Pojo.builder()
                            .sp((short) 3)
                            .ip(3)
                            .lp(3L)
                            .fp(3.14f)
                            .dp(3.14)
                            .cp('c')
                            .bp(true)
                            .string("刻晴")
                            .bigInteger(BigInteger.valueOf(100))
                            .bigDecimal(BigDecimal.valueOf(100.1))
                            .build())
                    .forEnum(Weekday.SUNDAY)
                    .forRecord(new Point(1, 2))
                    .forList(new TreeNode(1, "区域", List.of(
                            new TreeNode(2, "璃月", List.of(
                                    new TreeNode(3, "刻晴", List.of()),
                                    new TreeNode(4, "香菱", List.of())
                            )),
                            new TreeNode(5, "枫丹", List.of(
                                    new TreeNode(6, "芙宁娜", null),
                                    new TreeNode(7, "娜维娅", null)
                            ))
                    )))
                    .forObjectArray(new Point[]{new Point(1, 2), new Point(100, 200)})
                    .vectors(new List[]{
                            List.of(new Point(1, 2), new Point(100, 200)),
                            List.of(new Point(3, 4), new Point(300, 400))
                    })
                    .matrix(new int[][]{{1, 2}, {3, 4}})
                    .bools(new boolean[]{true, false})
                    .shorts(new short[]{1, 2, 3})
                    .ints(new int[]{1, 2, 3})
                    .longs(new long[]{1, 2, 3})
                    .floats(new float[]{1.1f, 2.2f, 3.3f})
                    .doubles(new double[]{1.1, 2.2, 3.3})
                    .chars(new char[]{'a', 'b', 'c'})
                    .build();

            var json = OM.stringify(obj);
            assertTrue(jsonEquals(json, """
                    {
                        "forPojo": {
                            "sp": 3,
                            "ip": 3,
                            "lp": 3,
                            "fp": 3.140000104904175,
                            "dp": 3.14,
                            "cp": "c",
                            "bp": true,
                            "string": "刻晴",
                            "bigInteger": 100,
                            "bigDecimal": 100.1
                        },
                        "forEnum": "SUNDAY",
                        "forRecord": { "x": 1, "y": 2 },
                        "forList": { "id": 1, "title": "区域", "children": [
                            { "id": 2, "title": "璃月", "children": [
                                { "id": 3, "title": "刻晴", "children": [] },
                                { "id": 4, "title": "香菱", "children": [] }
                            ]},
                            { "id": 5, "title": "枫丹", "children": [
                                { "id": 6, "title": "芙宁娜", "children": null },
                                { "id": 7, "title": "娜维娅", "children": null }
                            ]}
                        ]},
                        "forObjectArray": [
                            { "x": 1, "y": 2 },
                            { "x": 100, "y": 200 }
                        ],
                        "vectors": [
                            [ { "x": 1, "y": 2 }, { "x": 100, "y": 200 } ],
                            [ { "x": 3, "y": 4 }, { "x": 300, "y": 400 } ]
                        ],
                        "matrix": [[1, 2], [3, 4]],
                        "bools": [true, false],
                        "shorts": [1, 2, 3],
                        "ints": [1, 2, 3],
                        "longs": [1, 2, 3],
                        "floats": [1.100000023841858, 2.200000047683716, 3.299999952316284],
                        "doubles": [1.1, 2.2, 3.3],
                        "chars": ["a", "b", "c"]
                    }
                    """));

            var parsed = OM.parse(json, StringifyAndParseIntros.class);
            assertEquals(obj.getForPojo(), parsed.getForPojo());
            assertEquals(obj.getForEnum(), Weekday.SUNDAY);
            assertEquals(obj.getForRecord(), parsed.getForRecord());
            assertEquals(obj.getForList(), parsed.getForList());
            assertTrue(Equals.arrayEquals(obj.getForObjectArray(), parsed.getForObjectArray()));
            assertArrayEquals(obj.getBools(), parsed.getBools());
            assertArrayEquals(obj.getShorts(), parsed.getShorts());
            assertArrayEquals(obj.getInts(), parsed.getInts());
            assertArrayEquals(obj.getLongs(), parsed.getLongs());
            assertArrayEquals(obj.getFloats(), parsed.getFloats(), 0.0001f);
            assertArrayEquals(obj.getDoubles(), parsed.getDoubles(), 0.0001);
            assertArrayEquals(obj.getChars(), parsed.getChars());

            var vectors = parsed.vectors;
            assertEquals(2, vectors.length);
            assertIterableEquals(List.of(new Point(1, 2), new Point(100, 200)), vectors[0]);
            assertIterableEquals(List.of(new Point(3, 4), new Point(300, 400)), vectors[1]);

            var matrix = parsed.matrix;
            assertEquals(2, matrix.length);
            assertArrayEquals(new int[]{1, 2}, matrix[0]);
            assertArrayEquals(new int[]{3, 4}, matrix[1]);
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class StringifyAndParseIntros {
            private Pojo forPojo;
            private Weekday forEnum;
            private Point forRecord;
            private TreeNode forList;
            private Point[] forObjectArray;
            private List<Point>[] vectors; // generic array
            private int[][] matrix; // multi-dimensions array
            // TODO: generic list
            // TODO: map
            // TODO: generic map
            private boolean[] bools;
            private short[] shorts;
            private int[] ints;
            private long[] longs;
            private float[] floats;
            private double[] doubles;
            private char[] chars;
        }


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

    public record PrimitiveArrays(
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

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Image {
        private int width;
        private int height;
        private Point[] points;

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
    }

    public enum Weekday {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pojo {
        private short sp;
        private int ip;
        private long lp;
        private float fp;
        private double dp;
        private char cp;
        private boolean bp;

        private String string;
        private BigInteger bigInteger;
        private BigDecimal bigDecimal;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class TreeNode {
        private int id;
        private String title;
        private List<TreeNode> children;

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
                       && Equals.listItemEquals(this.children, that.children);
            }
            return false;
        }

        @Override
        public String toString() {
            return String.format("TreeNode{id=%d, title='%s', children=%s}", id, title, children);
        }
    }
}
