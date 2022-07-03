package com.game.snake.entity;

import android.graphics.Color;
import android.widget.LinearLayout;

import com.game.snake.MainActivity;
import com.game.snake.exception.SnakeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Snake {
    private MainActivity mainActivity;
    private final LinearLayout[][] playingField;
    private final int snakeColor = Color.GREEN;
    private final int transparent = Color.TRANSPARENT;
    private int maxX;
    private int maxY;
    private final List<PartSnake> partsSnake = new ArrayList<>();
    private Apple apple;
    private int score;

    public Snake(LinearLayout[][] playingField, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.playingField = playingField;
        generateSnake();
        generateApple();
    }

    private void generateSnake() {
        maxY = playingField.length;
        maxX = playingField[0].length;
        Random random = new Random();
        int x = random.nextInt(maxX-2)+1;
        int y = random.nextInt(maxY-2)+1;
        draw(x, y, snakeColor);
        partsSnake.add(new PartSnake(x, y));
    }

    private void generateApple() {
        List<PartSnake> freePlaces = new ArrayList<>();
        for(int y=1; y<maxY-1; y++)
            for(int x=1; x<maxX-1; x++) {
                PartSnake partSnake = new PartSnake(x, y);
                if(!partsSnake.contains(partSnake))
                    freePlaces.add(partSnake);
            }
        PartSnake partSnake = freePlaces.get(new Random().nextInt(freePlaces.size()));
        apple = new Apple(partSnake.x, partSnake.y);
        apple.draw(this::draw);
    }

    private int[] getStartPositionSnake() {
        PartSnake partSnake = partsSnake.get(0);
        return new int[] {partSnake.x, partSnake.y};
    }

    private void addStartPositionSnake(int... xy) {
        PartSnake partSnake = new PartSnake(xy[0], xy[1]);
        partsSnake.add(0, partSnake);
    }

    private int[] getEndPositionSnake() {
        PartSnake partSnake = partsSnake.get(partsSnake.size()-1);
        return new int[] {partSnake.x, partSnake.y};
    }

    private void addEndPositionSnake(boolean remove, int... xy) {
        if(remove) {
            partsSnake.remove(partsSnake.size()-1);
            return;
        }
        PartSnake partSnake = new PartSnake(xy[0], xy[1]);
        partsSnake.add(partSnake);
    }

    private void draw(int x, int y, int color) {
        playingField[y][x].setBackgroundColor(color);
    }

    private void controller(int... xy) throws SnakeException {
        boolean kill = false;
        for(PartSnake partSnake : partsSnake)
            if(new PartSnake(xy[0], xy[1]).equals(partSnake))
                kill = true;
        if(xy[0] == 0 || xy[0] == maxX - 1 || xy[1] == 0 || xy[1] == maxY - 1 || kill)
            throw new SnakeException();
    }

    private void move(int offsetX, int offsetY) throws SnakeException {
        int[] startPos = getStartPositionSnake();
        int[] endPos = getEndPositionSnake();
        controller(startPos[0]+offsetX, startPos[1]+offsetY);
        draw(endPos[0], endPos[1], transparent);
        addEndPositionSnake(true);
        draw(startPos[0]+offsetX, startPos[1]+offsetY, snakeColor);
        addStartPositionSnake(startPos[0]+offsetX, startPos[1]+offsetY);
        PartSnake partSnake = new PartSnake(startPos[0]+offsetX, startPos[1]+offsetY);
        if(new PartSnake(apple.getX(), apple.getY()).equals(partSnake)) {
            score++;
            mainActivity.getScore().setText(score+"");
            addEndPositionSnake(false, endPos[0], endPos[1]);
            generateApple();
        }
    }

    public void up() throws SnakeException {
        move(0, -1);
    }

    public void down() throws SnakeException {
        move(0, 1);
    }

    public void left() throws SnakeException {
        move(-1, 0);
    }

    public void right() throws SnakeException {
        move(1, 0);
    }

    static class PartSnake {
        public PartSnake(int x, int y) {
            this.x = x;
            this.y = y;
        }
        private final int x, y;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PartSnake partSnake = (PartSnake) o;

            if (x != partSnake.x) return false;
            return y == partSnake.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }
}