package ru.valkeru.libdemo.model.view;

public interface SeriesView {

    interface SeriesCreateView {}
    interface SeriesUpdateView extends SeriesCreateView {}
    interface SeriesListView {}
    interface SeriesSingleView extends SeriesListView {}
}
