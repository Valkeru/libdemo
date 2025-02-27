package ru.valkeru.libdemo.util;

import lombok.experimental.UtilityClass;
import org.slf4j.MDC;

import java.util.UUID;

@UtilityClass
public class RequestIdUtil {

    private final String MDC_REQUEST_ID_PROP = "requestId";

    public void installMDCRequestId() {
        MDC.put(MDC_REQUEST_ID_PROP, UUID.randomUUID().toString());
    }

    public UUID getMDCRequestId() {
        return UUID.fromString(MDC.get(MDC_REQUEST_ID_PROP));
    }

    public void clearMDCRequestId() {
        MDC.remove(MDC_REQUEST_ID_PROP);
    }
}
