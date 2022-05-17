package com.example.myboardgames.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myboardgames.R;
import com.example.myboardgames.database.AppDatabase;
import com.example.myboardgames.models.SharedGame;
import com.example.myboardgames.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SharedGameInfoActivity extends AppCompatActivity {
    private TextView nameTextI, descriptionTextI, rulesTextI;
    private TextView ageT, quantOfPlayersT, playingTime;
    private TextView categoriesTextI, recommenderText, btnBack;

    SharedPreferences prefs = null;
    private AppDatabase appDatabase;
    private SharedGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_game_info);

        prefs = getSharedPreferences("com.example.myboardgames", MODE_PRIVATE);
        appDatabase = new AppDatabase();

        game = (SharedGame)getIntent().getSerializableExtra("SharedGame");

        btnBack = (TextView)(findViewById(R.id.btnBack));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nameTextI = (TextView)(findViewById(R.id.nameTextI));
        nameTextI.setText(game.getName());

        descriptionTextI = (TextView)(findViewById(R.id.descriptionTextI));
        descriptionTextI.setText(game.getDescription());

        rulesTextI = (TextView)(findViewById(R.id.rulesTextI));
        rulesTextI.setText(game.getRules());

        ageT = (TextView)(findViewById(R.id.ageT));
        String age = ageT.getText().toString() + " " + game.getSmallestAge() + " до " + game.getBiggestAge();
        ageT.setText(age);

        quantOfPlayersT = (TextView)(findViewById(R.id.playersT));
        String quantOfPlayers = quantOfPlayersT.getText().toString() + " " + game.getSmallestQuantOfPlayers() + " до " + game.getBiggestQuantOfPlayers();
        quantOfPlayersT.setText(quantOfPlayers);

        playingTime = (TextView)(findViewById(R.id.playingTime));
        playingTime.setText(game.getPlayingTime());

        categoriesTextI = (TextView)(findViewById(R.id.categoriesTextI));
        categoriesTextI.setText(game.getCategories());

        recommenderText = (TextView)(findViewById(R.id.recommenderText));
        recommenderText.setText("Від " +  game.getRecommenderId());
        appDatabase.getReferenceToGroup(AppDatabase.USERS_GROUP_KEY).child(game.getRecommenderId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                if (user.getUsername() != null && !user.getUsername().equals(""))
                    recommenderText.setText("Від " + user.getUsername());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
