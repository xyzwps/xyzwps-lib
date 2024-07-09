package extensions.com.xyzwps.lib.express.Filter;

import com.xyzwps.lib.express.Filter;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

@Extension
public class FilterExt {

    public static Filter plus(@This Filter filter, Filter after) {
        return filter.andThen(after);
    }

}
