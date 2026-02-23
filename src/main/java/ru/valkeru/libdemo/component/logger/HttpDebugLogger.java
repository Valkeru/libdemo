package ru.valkeru.libdemo.component.logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HttpDebugLogger {

    /**
     * Для логирования параметров запросов
     */
    void logRequestParameters(HttpServletRequest request);

    /**
     * Для логирования тела запросов
     *
     * @param body Тело запроса
     * @param request Запрос
     *
     */
    void logRequestBody(Object body, HttpServletRequest request);

    /**
     * Логирование тела ответа
     *
     * @param body Тело ответа
     * @param request Запрос
     * @param response Ответ
     */
    void logResponseBody(Object body, HttpServletRequest request, HttpServletResponse response);
}
