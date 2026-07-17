package ru.valkeru.libdemo.component.message;

public interface MessageProvider {

    String getInternalErrorMessage(Exception e);

    String getDataIntegrityMessage(Exception e);
}
