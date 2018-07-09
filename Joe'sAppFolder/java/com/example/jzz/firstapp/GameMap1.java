package com.example.jzz.firstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

public class GameMap1 extends AppCompatActivity {
    public Button home;

    public void backhome(){
        home = findViewById(R.id.but_menu);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(GameMap1.this, MainActivity.class);
                finish();
                System.exit(0);
                startActivity(intent2);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_map1);
        backhome();
    }
}
