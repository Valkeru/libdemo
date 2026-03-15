package ru.valkeru.libdemo.util;

import lombok.experimental.UtilityClass;
import org.slf4j.MDC;

import java.util.UUID;

@UtilityClass
public class RequestIdUtil {

    private final String MDC_REQUEST_ID_PROP = "requestId";

    public UUID setMDCRequestId() {
        UUID requestId = UUID.randomUUID();
        MDC.put(MDC_REQUEST_ID_PROP, requestId.toString());

        return requestId;
    }

    public UUID getMDCRequestId() {
        return UUID.fromString(MDC.get(MDC_REQUEST_ID_PROP));
    }

    public void clearMDCRequestId() {
        MDC.remove(MDC_REQUEST_ID_PROP);
    }
}
