package com.example.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class SharedPreferenceConfig {

    private SharedPreferences sharedPreferences;
    private Context context;

    SharedPreferenceConfig(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("minWinTime",Context.MODE_PRIVATE);
    }

    public void writeTime(long time){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("minTime",time);
        editor.commit();
    }

    public long readTime(){
        return sharedPreferences.getLong("minTime",300000);
    }
}
