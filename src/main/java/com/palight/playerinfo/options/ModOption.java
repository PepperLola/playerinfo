package com.palight.playerinfo.options;

public abstract class ModOption {

    private String name;
    private String desc;

    private Object value;

    public ModOption (Object value, String name, String desc) {
        this.value = value;
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public abstract Object getValue();

    public abstract void setValue(boolean value);

    public static class BooleanOption extends ModOption {

        private boolean value;

        public BooleanOption(boolean value, String name, String desc) {
            super(value, name, desc);
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public void setValue(boolean value) {
            this.value = value;
        }
    }
}
