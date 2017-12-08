package cn.chenzd.mvpdemo.http.retrofit;

import java.util.HashMap;
import java.util.Map;

/**
 * 构建Api请求参数
 *
 * @author chenzaidong
 * @date 2017/12/7.
 */

public class HttpRequest {
    private static final String KEY = "key";
    private static final String VALUE = "7a856ebb8cf0a19c1c3aee729aa9e74b";

    public static final Map<String, Object> getRequest() {
        Map<String, Object> map = new HashMap<>(16);
        map.put(KEY, VALUE);
        return map;
    }
}
