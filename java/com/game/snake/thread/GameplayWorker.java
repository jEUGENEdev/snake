package com.game.snake.thread;

import android.os.AsyncTask;

import com.game.snake.MainActivity;
import com.game.snake.exception.SnakeException;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameplayWorker extends AsyncTask<Void, GameplayWorker.Movable, Void> {
    private final MainActivity mainActivity;
    private static boolean status = false;
    private int speed;

    public GameplayWorker(MainActivity mainActivity, int speed) {
        this.mainActivity = mainActivity;
        this.speed = speed;
    }

    @Override
    protected void onProgressUpdate(Movable... values) {
        super.onProgressUpdate(values);
        values[0].move();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        status = true;
        AtomicBoolean stop = new AtomicBoolean(true);
        while(stop.get()) {
            switch (mainActivity.getTypeAction()) {
                case UP: {
                    publishProgress(() -> {
                        try {
                            mainActivity.getSnake().up();
                        } catch (SnakeException e) {
                            mainActivity.gameOverDialog();
                            stop.set(false);
                        }
                    });
                    break;
                }
                case DOWN: {
                    publishProgress(() -> {
                        try {
                            mainActivity.getSnake().down();
                        } catch (SnakeException e) {
                            mainActivity.gameOverDialog();
                            stop.set(false);
                        }
                    });
                    break;
                }
                case LEFT: {
                    publishProgress(() -> {
                        try {
                            mainActivity.getSnake().left();
                        } catch (SnakeException e) {
                            mainActivity.gameOverDialog();
                            stop.set(false);
                        }
                    });
                    break;
                }
                case RIGHT: {
                    publishProgress(() -> {
                        try {
                            mainActivity.getSnake().right();
                        } catch (SnakeException e) {
                            mainActivity.gameOverDialog();
                            stop.set(false);
                        }
                    });
                    break;
                }
            }
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static void setStatus(boolean status) {
        GameplayWorker.status = status;
    }

    public static boolean isStatus() {
        return status;
    }

    interface Movable {
        void move();
    }
}
