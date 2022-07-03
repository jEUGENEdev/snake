package com.game.snake.entity;

import android.graphics.Color;

public class Apple {
    private final int x, y;

    public Apple(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Drawable drawable) {
        drawable.draw(x, y, Color.RED);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
interface Drawable {
    void draw(int x, int y, int color);
}
