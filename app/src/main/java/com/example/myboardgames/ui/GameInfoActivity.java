package com.example.myboardgames.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myboardgames.Game;
import com.example.myboardgames.R;
import com.example.myboardgames.ui.games.GamesFragment;

public class GameInfoActivity extends AppCompatActivity {

    private TextView mTextView;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        game = (Game)getIntent().getSerializableExtra("Game");
        mTextView = (TextView) findViewById(R.id.text);
        mTextView.setText(game.getName());

        // Enables Always-on
        //setAmbientEnabled();
    }
}