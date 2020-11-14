package com.karimun.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.karimun.tictactoe.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static final String PLAY_MODE = "play_mode";
    public static final String PLAYER_SIGN = "player_sign";

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void action1Player(View view) {
        Intent intent = new Intent(this, ChooseActivity.class);
        startActivity(intent);
    }

    public void action2Players(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(PLAY_MODE, 1);
        startActivity(intent);
    }
}