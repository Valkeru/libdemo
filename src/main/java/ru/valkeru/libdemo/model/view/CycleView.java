package ru.valkeru.libdemo.model.view;

public interface CycleView {

    interface CycleCreateView {}
    interface CycleUpdateView extends CycleCreateView {}
    interface CycleSingleView extends CycleUpdateView {}
    interface CycleListView extends CycleSingleView {}
}
