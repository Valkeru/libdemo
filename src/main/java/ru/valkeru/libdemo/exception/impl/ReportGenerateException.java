package ru.valkeru.libdemo.exception.impl;

import ru.valkeru.libdemo.exception.InternalException;

public class ReportGenerateException extends InternalException {

    private static final String REPORT_ERROR = "При формировании отчёта произошла ошибка";

    private ReportGenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ReportGenerateException pdfException(Throwable e) {
        return new ReportGenerateException(REPORT_ERROR, e);
    }
}
