package com.palight.playerinfo.macro;

public abstract class CommandArgument<T> {

    protected String name;
    protected T value;

    public CommandArgument(String name) {
        this.name = name;
    }

    public abstract String setValue(String value);

    public T getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
