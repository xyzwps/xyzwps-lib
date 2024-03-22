package com.xyzwps.lib.paimon;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class Utils {

    public static String readFileContent(String fileName) {
        var cl = Utils.class.getClassLoader();
        var path = Objects.requireNonNull(cl.getResource(fileName), String.format("File %s not found", fileName))
                .getPath();
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
