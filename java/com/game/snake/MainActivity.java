package com.game.snake;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.game.snake.entity.Snake;
import com.game.snake.thread.GameplayWorker;
import com.game.snake.types.TypeAction;

public class MainActivity extends AppCompatActivity {
    private LinearLayout gameSpace;
    private TextView score;
    private int maxX;
    private int maxY;
    private LinearLayout[][] playingField;
    private Snake snake;
    private volatile TypeAction typeAction;
    private boolean endDialogGameShow = false;
    private int speedSnake = 400;
    private int currentSpeedSnake = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int[] _s = {};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        score = findViewById(R.id.score);
        initGameSpace();
        snake = new Snake(playingField, this);
        initGameplay();
        initSettingsButton();
    }

    public void initSettingsButton() {
        ImageButton settings = findViewById(R.id.settings);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.settings_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        settings.setOnClickListener(view -> {
            if(!GameplayWorker.isStatus())
                dialog.show();
            else
                Toast.makeText(this, R.string.ops1, Toast.LENGTH_SHORT).show();
        });
        Button[] buttons = {dialog.findViewById(R.id.easy), dialog.findViewById(R.id.middle), dialog.findViewById(R.id.hard)};
        for(int i=0; i<buttons.length; i++) {
            if(i == 0)
                buttons[i].setOnClickListener(view -> {
                    currentSpeedSnake = 400;
                    Toast.makeText(this, R.string.ok, Toast.LENGTH_SHORT).show();
                });
            else if(i == 1)
                buttons[i].setOnClickListener(view -> {
                    currentSpeedSnake = 200;
                    Toast.makeText(this, R.string.ok, Toast.LENGTH_SHORT).show();
                });
            else
                buttons[i].setOnClickListener(view -> {
                    currentSpeedSnake = 100;
                    Toast.makeText(this, R.string.ok, Toast.LENGTH_SHORT).show();
                });
        }
        dialog.findViewById(R.id.apply).setOnClickListener(view -> {
            speedSnake = currentSpeedSnake;
            currentSpeedSnake = 400;
            dialog.dismiss();
        });
    }

    public void initGameplay() {
        Button up = findViewById(R.id.button_up),
        down = findViewById(R.id.button_down),
        left = findViewById(R.id.button_left),
        right = findViewById(R.id.button_right);
        up.setOnClickListener(view -> {
            if(!GameplayWorker.isStatus() && !endDialogGameShow)
                new GameplayWorker(this, speedSnake).execute();
            typeAction = TypeAction.UP;
        });
        down.setOnClickListener(view -> {
            if(!GameplayWorker.isStatus() && !endDialogGameShow)
                new GameplayWorker(this, speedSnake).execute();
            typeAction = TypeAction.DOWN;
        });
        left.setOnClickListener(view -> {
            if(!GameplayWorker.isStatus() && !endDialogGameShow)
                new GameplayWorker(this, speedSnake).execute();
            typeAction = TypeAction.LEFT;
        });
        right.setOnClickListener(view -> {
            if(!GameplayWorker.isStatus() && !endDialogGameShow)
                new GameplayWorker(this, speedSnake).execute();
            typeAction = TypeAction.RIGHT;
        });
    }

    public void gameOverDialog() {
        GameplayWorker.setStatus(false);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.game_over_dialog);
        dialog.setCancelable(false);
        Button start = dialog.findViewById(R.id.start);
        start.setOnClickListener(view -> {
            typeAction = null;
            gameSpace.removeAllViews();
            initGameSpace();
            snake = new Snake(playingField, this);
            endDialogGameShow = false;
            dialog.dismiss();
        });
        endDialogGameShow = true;
        dialog.show();
    }

    public void initSizesGameSpace() {
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        maxY = (int) ((point.y*0.6-30)/50)-1;
        maxX = (point.x-30)/50-2;
    }

    public void generateMargin() {
        for(int i=0; i<maxX; i++) {
            playingField[0][i].setBackgroundColor(Color.BLACK);
            playingField[maxY-1][i].setBackgroundColor(Color.BLACK);
        }
        for(int i=1; i<maxY-1; i++) {
            playingField[i][maxX-1].setBackgroundColor(Color.BLACK);
            playingField[i][0].setBackgroundColor(Color.BLACK);
        }
    }

    public void initGameSpace() {
        initSizesGameSpace();
        playingField = new LinearLayout[maxY][maxX];
        gameSpace = findViewById(R.id.game_space);
        for(int i=0; i<maxY; i++) {
            LinearLayout row = new LinearLayout(this);
            for(int k=0; k<maxX; k++) {
                LinearLayout columns = new LinearLayout(this);
                row.addView(columns, new LinearLayout.LayoutParams(50, 50));
                playingField[i][k] = columns;
            }
            gameSpace.addView(row);
        }
        generateMargin();
    }

    public TypeAction getTypeAction() {
        return typeAction;
    }

    public Snake getSnake() {
        return snake;
    }

    public TextView getScore() {
        return score;
    }
}