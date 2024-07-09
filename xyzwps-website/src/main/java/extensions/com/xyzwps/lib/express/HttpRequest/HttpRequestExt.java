package extensions.com.xyzwps.lib.express.HttpRequest;

import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.json.JsonException;
import com.xyzwps.website.common.AppException;
import com.xyzwps.website.common.JSON;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Extension
public class HttpRequestExt {

    public static <T> T json(@This HttpRequest req, Class<T> clazz) {
        var contentType = req.contentType();
        if (contentType == null) {
            throw AppException.badRequest("Content-Type is not json");
        }
        if (!contentType.isApplicationJson()) {
            throw AppException.badRequest("Content-Type is not json");
        }
        if (!(req.body() instanceof InputStream is)) {
            throw AppException.internalServerError("Request body has been parsed");
        }

        try {
            var charset = contentType.parameters.get("charset").map(Charset::forName).orElse(StandardCharsets.UTF_8);
            var reader = new InputStreamReader(is, charset);
            return JSON.JM.parse(reader, clazz);
        } catch (JsonException e) {
            throw AppException.badRequest("Invalid json format");
        }
    }
}
