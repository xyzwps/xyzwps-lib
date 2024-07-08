package com.xyzwps.lib.http;

import java.util.List;

/**
 * @param essence      not null
 * @param source       may be null
 * @param charset      may be null
 * @param compressible
 * @param extensions   not null
 */
public record MimeInfo(String essence, MimeSource source, String charset,
                       boolean compressible, List<String> extensions) {
}
