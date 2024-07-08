package com.xyzwps.lib.http;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;

/**
 * Based on [`mime-db`](https://github.com/jshttp/mime-db) 1.52.0
 *
 * `db.csv` generating script:
 *
 * ```js
 * const db = require('mime-db/db.json');
 * const _ = require('lodash');
 *
 * for (const name in db) {
 *   const meta = db[name];
 *
 *   const source = _.isEmpty(meta.source) ? '' : `${meta.source.toUpperCase()}`;
 *   const charset = _.isEmpty(meta.charset) ? '' : `${meta.charset}`;
 *   const compressible = _.isNil(meta.compressible) ? false : meta.compressible;
 *   const extensions = _(meta.extensions).join(';');
 *
 *   console.log(`${name},${source},${charset},${compressible},${extensions}`);
 * }
 * ```
 */
public final class MimeDb {

    private static final Map<String, MimeInfo> essenceToMime;

    private static final Map<String, List<MimeInfo>> extToMimes;

    public static Optional<MimeInfo> findFirstByExtension(String ext) {
        if (ext == null || ext.isEmpty()) return Optional.empty();

        var li = extToMimes.get(ext.toLowerCase());
        if (li == null || li.isEmpty()) return Optional.empty();

        return Optional.of(li.getFirst());
    }

    private static Map<String, MimeInfo> init() {
        Map<String, MimeInfo> result = new HashMap<>();

        try (var inputStream = MimeDb.class.getClassLoader().getResourceAsStream("db.csv")) {
            assert inputStream != null;
            try (var scanner = new Scanner(inputStream)) {
                while (scanner.hasNextLine()) {
                    String[] segments = scanner.nextLine().split(",", 5);
                    String name = segments[0], source = segments[1], charset = segments[2];
                    String compressible = segments[3], extensions = segments[4];
                    var mime = new MimeInfo(
                            name,
                            MimeSource.from(source),
                            charset == null || charset.isEmpty() ? null : charset,
                            Boolean.parseBoolean(compressible),
                            toExtensions(extensions)
                    );
                    result.put(mime.essence(), mime);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return result;
    }

    private static List<String> toExtensions(String str) {
        if (str == null || str.isEmpty()) return List.of();
        return List.of(str.split(";"));
    }

    static {
        essenceToMime = Map.copyOf(init());

        {
            var etm = new HashMap<String, List<MimeInfo>>();
            essenceToMime.forEach((essence, mime) -> {
                for (var ext : mime.extensions()) {
                    etm.computeIfAbsent(ext, (x) -> new LinkedList<>()).add(mime);
                }
            });

            Map<String, List<MimeInfo>> toImmutableList = new HashMap<>();
            for (var ext : etm.keySet()) {
                toImmutableList.put(ext, List.copyOf(etm.get(ext)));
            }
            extToMimes = Map.copyOf(toImmutableList);
        }
    }

}
