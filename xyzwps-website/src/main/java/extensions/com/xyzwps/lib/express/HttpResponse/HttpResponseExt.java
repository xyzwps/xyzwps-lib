package extensions.com.xyzwps.lib.express.HttpResponse;

import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.website.common.JSON;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

@Extension
public class HttpResponseExt {

    public static void sendJson(@This HttpResponse res, Object obj) {
        res.ok();
        res.headers().set("Content-Type", "application/json");
        res.send(JSON.stringify(obj).getBytes());
    }
}
