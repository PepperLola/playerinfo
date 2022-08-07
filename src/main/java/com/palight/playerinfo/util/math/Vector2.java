package com.palight.playerinfo.util.math;

public class Vector2<T> {
    public T x;
    public T y;

    public Vector2(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public static class Vector2i extends Vector2<Integer> {
        public Vector2i(Integer x, Integer y) {
            super(x, y);
        }

        public Vector2i add(Vector2i other) {
            return new Vector2i(this.x + other.x, this.y + other.y);
        }

        public Vector2i subtract(Vector2i other) {
            return new Vector2i(this.x - other.x, this.y - other.y);
        }
    }
}
