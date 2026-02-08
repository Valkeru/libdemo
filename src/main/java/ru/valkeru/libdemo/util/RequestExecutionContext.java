package ru.valkeru.libdemo.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.util.ContentCachingRequestWrapper;
import ru.valkeru.libdemo.exception.impl.NoCachedRequestException;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class RequestExecutionContext {

    public static final String REQUEST_WRAPPER_KEY = "requestWrapper";

    private static final ThreadLocal<Map<String, Object>> contextStorage = ThreadLocal.withInitial(HashMap::new);

    public static void store(String key, Object object) {
        Map<String, Object> storage = contextStorage.get();

        storage.put(key, object);
    }

    public static <T> T getObject(String key, Class<? extends T> targetClass) {
        Map<String, Object> storage = contextStorage.get();
        if (storage == null) {
            return null;
        }

        Object storedObject = storage.getOrDefault(key, null);

        if (!targetClass.isInstance(storedObject)) {
            return null;
        }

        return targetClass.cast(storedObject);
    }

    public static String readRequestBody() throws NoCachedRequestException {
        ContentCachingRequestWrapper requestWrapper =
                getObject(REQUEST_WRAPPER_KEY, ContentCachingRequestWrapper.class);

        if (requestWrapper == null) {
            throw NoCachedRequestException.noCachedRequest();
        }

        return requestWrapper.getContentAsString();
    }

    public static void clear() {
        contextStorage.remove();
    }
}
