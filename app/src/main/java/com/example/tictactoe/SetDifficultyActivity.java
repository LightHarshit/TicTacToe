package com.example.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SetDifficultyActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_easy,button_medium,button_hard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_difficulty);

        button_easy = findViewById(R.id.easy);
        button_medium = findViewById(R.id.medium);
        button_hard = findViewById(R.id.hard);

        button_easy.setOnClickListener(this);
        button_medium.setOnClickListener(this);
        button_hard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int difficulty = 0;
        switch(v.getId()){
            case R.id.easy:
                difficulty = 1;
                break;
            case R.id.medium:
                difficulty = 2;
                break;
            case R.id.hard:
                difficulty = 3;
                break;
        }

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("difficulty",difficulty);
        startActivity(intent);
    }
}
