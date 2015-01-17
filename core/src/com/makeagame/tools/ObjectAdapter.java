package com.makeagame.tools;

import java.util.List;

public abstract class ObjectAdapter<T> {

    List<T> list;
    SimpleLayout layout;

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

    public abstract SimpleLayout createView();

    public abstract void fillItem(T item);


}
