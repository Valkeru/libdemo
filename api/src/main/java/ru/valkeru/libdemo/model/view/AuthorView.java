package ru.valkeru.libdemo.model.view;

public interface AuthorView {

    interface AuthorCreateView {}
    interface AuthorUpdateView extends AuthorCreateView {}
    interface AuthorListView {}
    interface AuthorSingleView {}
}
