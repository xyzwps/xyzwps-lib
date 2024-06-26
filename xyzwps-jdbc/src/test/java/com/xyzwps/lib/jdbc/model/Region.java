package com.xyzwps.lib.jdbc.model;

import com.xyzwps.lib.jdbc.ColumnPropertyMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public enum Region {
    MONDSTADT("蒙德"),
    LIYUE("璃月"),
    INAZUMA("稻妻"),
    SUMERU("须弥"),
    FONTAINE("枫丹"),
    NATLAN("纳塔"),
    SNEZHNEAYA("至冬"),
    KHAENRIAH("坎瑞亚"),
    UNKNOWN("未知");

    public final String zhName;

    Region(String zhName) {
        this.zhName = zhName;
    }

    public static Region fromZhName(String zhName) {
        return switch (zhName) {
            case null -> null;
            case "蒙德" -> MONDSTADT;
            case "璃月" -> LIYUE;
            case "稻妻" -> INAZUMA;
            case "须弥" -> SUMERU;
            case "枫丹" -> FONTAINE;
            case "纳塔" -> NATLAN;
            case "至冬" -> SNEZHNEAYA;
            case "坎瑞亚" -> KHAENRIAH;
            default -> UNKNOWN;
        };
    }

    public static class RegionMapper implements ColumnPropertyMapper<Region> {

        @Override
        public Region fromColumn(ResultSet rs, String column) throws SQLException {
            return Region.fromZhName(rs.getString(column));
        }

        @Override
        public void fromProperty(PreparedStatement ps, int index, Region property) throws SQLException {
            ps.setString(index, property.zhName);
        }
    }
}
