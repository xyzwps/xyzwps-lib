package lib.jshttp.mimedb;

import java.util.List;
import java.util.Set;

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
