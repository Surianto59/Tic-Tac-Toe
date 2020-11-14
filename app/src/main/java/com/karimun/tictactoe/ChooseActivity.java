package com.karimun.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.karimun.tictactoe.databinding.ActivityChooseBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChooseActivity extends AppCompatActivity {
    ActivityChooseBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void actionCross(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(MainActivity.PLAY_MODE, 0);
        intent.putExtra(MainActivity.PLAYER_SIGN, true);
        startActivity(intent);
        finish();
    }

    public void actionNought(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(MainActivity.PLAY_MODE, 0);
        intent.putExtra(MainActivity.PLAYER_SIGN, false);
        startActivity(intent);
        finish();
    }
}
