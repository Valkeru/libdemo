package ru.valkeru.libdemo.model.view;

public interface BookView {

    interface BookCreateView {}
    interface BookUpdateView extends BookCreateView {}
    interface BookListView {}
    interface BookSingleView extends BookUpdateView {}
}
