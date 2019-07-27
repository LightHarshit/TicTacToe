package com.example.tictactoe;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button00,button01,button02;
    Button button10,button11,button12;
    Button button20,button21,button22;
    TextView textView;
    private static final long START_TIME_IN_MILLIS = 300000;
    CountDownTimer countDownTimer;
    long timeleftinmillis = START_TIME_IN_MILLIS,timeTaken;
    SharedPreferenceConfig preferenceConfig;

     int[][] buttons = new int[][]{{R.id.button00,R.id.button01,R.id.button02},
        {R.id.button10,R.id.button11,R.id.button12},
        {R.id.button20,R.id.button21,R.id.button22}};

    int matrix[][] = new int[3][3];
    int comp_x = -1,comp_y = -1,winner,difficulty;
    boolean gameOver = false,validMove = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                matrix[i][j] = 0;

        textView = findViewById(R.id.min_time);
        button00 = findViewById(R.id.button00);
        button01 = findViewById(R.id.button01);
        button02 = findViewById(R.id.button02);
        button10 = findViewById(R.id.button10);
        button11 = findViewById(R.id.button11);
        button12 = findViewById(R.id.button12);
        button20 = findViewById(R.id.button20);
        button21 = findViewById(R.id.button21);
        button22 = findViewById(R.id.button22);

        Intent intent = getIntent();
        difficulty = intent.getIntExtra("difficulty",1);

        button00.setOnClickListener(this);
        button01.setOnClickListener(this);
        button02.setOnClickListener(this);
        button10.setOnClickListener(this);
        button11.setOnClickListener(this);
        button12.setOnClickListener(this);
        button20.setOnClickListener(this);
        button21.setOnClickListener(this);
        button22.setOnClickListener(this);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        textView.setText("Minimum time : " + ((double)preferenceConfig.readTime())/1000 + "seconds.");
        startTimer();
    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(timeleftinmillis,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleftinmillis = millisUntilFinished;
                if(gameOver){
                    cancel();
                    timeTaken = START_TIME_IN_MILLIS - timeleftinmillis;
                    Log.e("message", "game finished in " + timeTaken);
                    if(preferenceConfig.readTime() > timeTaken && winner ==2) {
                        preferenceConfig.writeTime(timeTaken);
                        Toast.makeText(getApplicationContext(),"new least time",Toast.LENGTH_SHORT).show();
                        textView.setText("Minimum time : " + ((double)preferenceConfig.readTime())/1000 + "seconds.");
                        ContactDbHelper contactDbHelper = new ContactDbHelper(getApplicationContext());
                        SQLiteDatabase database = contactDbHelper.getWritableDatabase();
                        contactDbHelper.addContent((double)timeTaken,database);
                        contactDbHelper.close();
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button00 :
                if(matrix[0][0] == 0) {
                    button00.setText("O");
                    matrix[0][0] = 2;
                    validMove = true;
                }
                break;

            case R.id.button01 :
                if(matrix[0][1] == 0) {
                    button01.setText("O");
                    matrix[0][1] = 2;
                    validMove = true;
                }
                break;


            case R.id.button02 :
                if(matrix[0][2] == 0) {
                    button02.setText("O");
                    matrix[0][2] = 2;
                    validMove = true;
                }
                break;

            case R.id.button10 :
                if(matrix[1][0] == 0) {
                    button10.setText("O");
                    matrix[1][0] = 2;
                    validMove = true;
                }
                break;

            case R.id.button11 :
                if(matrix[1][1] == 0) {
                    button11.setText("O");
                    matrix[1][1] = 2;
                    validMove = true;
                }
                break;

            case R.id.button12 :
                if(matrix[1][2] == 0) {
                    button12.setText("O");
                    matrix[1][2] = 2;
                    validMove = true;
                }
                break;

            case R.id.button20 :
                if(matrix[2][0] == 0) {
                    button20.setText("O");
                    matrix[2][0] = 2;
                    validMove = true;
                }
                break;

            case R.id.button21 :
                if(matrix[2][1] == 0) {
                    button21.setText("O");
                    matrix[2][1] = 2;
                    validMove = true;
                }
                break;

            case R.id.button22 :
                if(matrix[2][2] == 0) {
                    button22.setText("O");
                    matrix[2][2] = 2;
                    validMove = true;
                }
                break;
        }

        if(gameResult(2) == 2) {
            gameOver = true;
            winner = 2;
            Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
            intent.putExtra("winner","YOU WIN");
            startActivity(intent);
        }

        if(!gameOver && validMove) {
            if (movesLeft()) {
                putComputersMove();
                validMove = false;
            }
            else {
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                intent.putExtra("winner", "MATCH DRAW");
                startActivity(intent);
            }
        }
        if(gameResult(1) == 1) {
            gameOver = true;
            winner = 1;
            Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
            intent.putExtra("winner","COMPUTER WINS");
            startActivity(intent);
        }
        String s = " \n";
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                s = s + matrix[i][j];
            }
            s = s + '\n';
        }
        Log.e("message",s);
    }

    private boolean movesLeft() {
        for(int i=0;i<=2;i++)
            for(int j=0;j<3;j++)
                if(matrix[i][j] == 0)
                    return true;
        return false;
    }

    private void putComputersMove() {
        if(difficulty == 1) {
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(matrix[i][j] == 0){
                        comp_x = i;
                        comp_y = j;
                    }
                }
            }
            Button comp_button = findViewById(buttons[comp_x][comp_y]);
            comp_button.setText("X");
            matrix[comp_x][comp_y] = 1;
        }

        else if(difficulty == 2){
            getMediumMove();
            Button comp_button = findViewById(buttons[comp_x][comp_y]);
            comp_button.setText("X");
            matrix[comp_x][comp_y] = 1;
        }
        else if(difficulty == 3) {
            minimax(3, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (comp_x != -1) {
                Button comp_button = findViewById(buttons[comp_x][comp_y]);
                comp_button.setText("X");
                matrix[comp_x][comp_y] = 1;
            }
        }
        Log.e("message","comp move is : " + comp_x + "  " + comp_y);
    }

    private void getMediumMove(){
        int maxScore = -100,currentScore;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(matrix[i][j] == 0){
                    matrix[i][j] = 1;
                    currentScore = score();
                    if(currentScore>maxScore) {
                        maxScore = currentScore;
                        comp_x = i;
                        comp_y = j;
                    }
                    matrix[i][j] = 0;
                }
            }
        }
    }

    public int minimax(int depth, int turn, int alpha, int beta){
        if(beta<=alpha){if(turn == 1) return Integer.MAX_VALUE; else return Integer.MIN_VALUE; }
        int gameResult = checkGame(1);

        if(gameResult == 1)return Integer.MAX_VALUE/2;
        else if(checkGame(2) == 2)return Integer.MIN_VALUE/2;
        else if(gameResult==0)return 0;

        if(depth==1)return score();
        int breaker = 0;
        int maxScore=Integer.MIN_VALUE, minScore = Integer.MAX_VALUE;
        for(int i=0;i<3;i++) {
            for(int j=0;j<3;j++) {
                int currentScore = 0;

                if(!movesLeft()) continue;

                if(turn==1){
                    if(matrix[i][j] == 0)
                        matrix[i][j] = 1;
                    else
                        continue;
                    currentScore = minimax(depth-1, 2, alpha, beta);

                    if(depth==3){
                        Log.e("message","current score of row :" + i + "col : "+ j + "is : "+ currentScore);
                        if(currentScore > maxScore){comp_x = i;comp_y = j;}
                        if(currentScore == Integer.MAX_VALUE/2){matrix[i][j] = 0;breaker = 1;break;}
                    }

                    maxScore = Math.max(currentScore, maxScore);

                    alpha = Math.max(currentScore, alpha);
                }
                else if(turn==2){
                    if(matrix[i][j] == 0)
                        matrix[i][j] = 2;
                    else
                        continue;
                    currentScore = minimax(depth-1, 1, alpha, beta);
                    minScore = Math.min(currentScore, minScore);

                    beta = Math.min(currentScore, beta);
                }
                matrix[i][j] = 0;
                if(currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE) {breaker = 1;break;}
            }
            if(breaker == 1){
                breaker = 0;
                break;
            }
        }
        return turn==1?maxScore:minScore;
    }

    private int score() {
        int score = 0;
        //Count Horizontal
        for(int i=0;i<3;i++) {
            int temp[] = new int[]{matrix[i][0], matrix[i][1], matrix[i][2]};
            score = countScore(temp,score);
        }

        //Count Vertical
        for(int i=0;i<3;i++) {
            int temp[] = new int[]{matrix[0][i],matrix[1][i],matrix[2][i]};
            score = countScore(temp,score);
        }

        //Count Diognals
        int temp[] = new int[]{matrix[0][0],matrix[1][1],matrix[2][2]};
        score = countScore(temp,score);
        temp = new int[]{matrix[0][2],matrix[1][1],matrix[2][0]};
        score = countScore(temp,score);

        return score;
    }

    private int checkGame(int value) {
        if(matrix[0][0] == value && matrix[0][1] == value && matrix[0][2] == value)
            return value;
        if(matrix[1][0] == value && matrix[1][1] == value && matrix[1][2] == value)
            return value;
        if(matrix[2][0] == value && matrix[2][1] == value && matrix[2][2] == value)
            return value;
        if(matrix[0][0] == value && matrix[1][0] == value && matrix[2][0] == value)
            return value;
        if(matrix[0][1] == value && matrix[1][1] == value && matrix[2][1] == value)
            return value;
        if(matrix[0][2] == value && matrix[1][2] == value && matrix[2][2] == value)
            return value;
        if(matrix[0][0] == value && matrix[1][1] == value && matrix[2][2] == value)
            return value;
        if(matrix[0][2] == value && matrix[1][1] == value && matrix[2][0] == value)
            return value;

        int count = 0;
        for(int i=0;i<3;i++)
            for(int j = 0;j<3;j++)
                if(matrix[i][j] == 0)
                    count++;
        //Game Draw
        if(count == 0)
            return 0;
        return -1;
    }

    private int gameResult(int value){
        int color;
        if(value == 2)
            color = Color.BLUE;
        else
            color = Color.GREEN;

        if(matrix[0][0] == value && matrix[0][1] == value && matrix[0][2] == value) {
            button00.setBackgroundColor(color);
            button01.setBackgroundColor(color);
            button02.setBackgroundColor(color);
            return value;
        }
        if(matrix[1][0] == value && matrix[1][1] == value && matrix[1][2] == value) {
            button10.setBackgroundColor(color);
            button11.setBackgroundColor(color);
            button12.setBackgroundColor(color);
            return value;
        }
        if(matrix[2][0] == value && matrix[2][1] == value && matrix[2][2] == value) {
            button20.setBackgroundColor(color);
            button21.setBackgroundColor(color);
            button22.setBackgroundColor(color);
            return value;
        }
        if(matrix[0][0] == value && matrix[1][0] == value && matrix[2][0] == value) {
            button00.setBackgroundColor(color);
            button10.setBackgroundColor(color);
            button20.setBackgroundColor(color);
            return value;
        }
        if(matrix[0][1] == value && matrix[1][1] == value && matrix[2][1] == value) {
            button01.setBackgroundColor(color);
            button11.setBackgroundColor(color);
            button21.setBackgroundColor(color);
            return value;
        }
        if(matrix[0][2] == value && matrix[1][2] == value && matrix[2][2] == value) {
            button02.setBackgroundColor(color);
            button12.setBackgroundColor(color);
            button22.setBackgroundColor(color);
            return value;
        }
        if(matrix[0][0] == value && matrix[1][1] == value && matrix[2][2] == value) {
            button00.setBackgroundColor(color);
            button11.setBackgroundColor(color);
            button22.setBackgroundColor(color);
            return value;
        }
        if(matrix[0][2] == value && matrix[1][1] == value && matrix[2][0] == value) {
            button02.setBackgroundColor(color);
            button11.setBackgroundColor(color);
            button20.setBackgroundColor(color);
            return value;
        }
        int count = 0;
        for(int i=0;i<3;i++)
            for(int j = 0;j<3;j++)
                if(matrix[i][j] == 0)
                    count++;
        //Game Draw
        if(count == 0)
            return 0;
        return -1;
    }

    private int count(int[] temp,int a){
        int count = 0;
        for(int i=0;i<3;i++)
            if(temp[i] == a)
                count++;
        return count;
    }

    private int countScore(int[] temp,int score){
        if(count(temp,1) == 3)
            score+=100;
        if(count(temp,1) == 2 && count(temp,0) == 1)
            score+=20;
        if(count(temp,1) == 1 && count(temp,0) == 2)
            score+=10;
        if(count(temp,2) == 2 && count(temp,0) == 1 && difficulty == 3)
            score-=40;
        return score;
    }
}
