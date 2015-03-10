package com.makeagame.tools;

import java.util.List;

import com.makeagame.core.view.component.BaseViewLayout;

public abstract class ObjectAdapter<T> {

    List<T> list;
    BaseViewLayout layout;

    public ObjectAdapter(List<T> list) {
        this();
        this.list = list;
    }

    public ObjectAdapter() {
        this.layout = createView();
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public abstract BaseViewLayout createView();

    public abstract void fillItem(T item);


}
