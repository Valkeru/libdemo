package ru.valkeru.libdemo.component.message;

public interface MessageProvider {

    String getBadRequestMessage(Exception e);

    String getInternalErrorMessage(Exception e);

    String getDataIntegrityMessage(Exception e);
}
