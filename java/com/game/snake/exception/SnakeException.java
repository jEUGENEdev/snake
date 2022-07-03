package com.game.snake.exception;

public class SnakeException extends Throwable {
    @Override
    public String getMessage() {
        return "Змейка вышла за границы карты";
    }
}
