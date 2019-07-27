package com.example.tictactoe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView textView_winner;
    TextView textView_winTime;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textView_winner = findViewById(R.id.winner);
        textView_winTime = findViewById(R.id.winTime);
        button = findViewById(R.id.play_again);

        Intent intent = getIntent();
        String winner = intent.getStringExtra("winner");
        textView_winner.setText(winner);

        ContactDbHelper contactDbHelper = new ContactDbHelper(getApplicationContext());
        SQLiteDatabase database;
        database = contactDbHelper.getReadableDatabase();
        Cursor cursor = contactDbHelper.readContent(database);
        String message = "Victory Timing : \n";

        while(cursor.moveToNext()){
            double time = cursor.getDouble(cursor.getColumnIndex("winTime"));
            message = message + (double)(time/1000) + " seconds\n";
        }
        contactDbHelper.close();

        textView_winTime.setText(message);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SetDifficultyActivity.class));
            }
        });
    }
}
