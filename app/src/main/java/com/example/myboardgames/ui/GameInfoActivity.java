package com.example.myboardgames.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myboardgames.Game;
import com.example.myboardgames.GamesProcessor;
import com.example.myboardgames.JSONHelper;
import com.example.myboardgames.R;
import com.example.myboardgames.ui.addGame.AddGameFragment;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class GameInfoActivity extends AppCompatActivity {

    private EditText nameTextI, descriptionTextI, photoPathTextI, rulesTextI, placeTextI;
    private EditText smallestAgeTextI, biggestAgeTextI, smallestQuantOfPlayersTextI;
    private EditText biggestQuantOfPlayersTextI, categoriesTextI, quantOfPointsTextI;
    private ImageView imageViewI;
    private ImageButton favoriteButtonI;

    //private TextView mTextView;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        game = (Game)getIntent().getSerializableExtra("Game");
        //mTextView = (TextView) findViewById(R.id.text);
        //mTextView.setText(game.getName());

        nameTextI = (EditText)(findViewById(R.id.nameTextI));
        nameTextI.setText(game.getName());

        descriptionTextI = (EditText)(findViewById(R.id.descriptionTextI));
        descriptionTextI.setText(game.getDescription());

        photoPathTextI = (EditText)(findViewById(R.id.photoPathTextI));
        photoPathTextI.setText(game.getPhotoPath());

        rulesTextI = (EditText)(findViewById(R.id.rulesTextI));
        rulesTextI.setText(game.getRules());

        placeTextI = (EditText)(findViewById(R.id.placeTextI));
        placeTextI.setText(game.getPlace());

        smallestAgeTextI = (EditText)(findViewById(R.id.smallestAgeTextI));
        smallestAgeTextI.setText(game.getSmallestAge());

        biggestAgeTextI = (EditText)(findViewById(R.id.biggestAgeTextI));
        biggestAgeTextI.setText(game.getBiggestAge());

        smallestQuantOfPlayersTextI = (EditText)(findViewById(R.id.smallestQuantOfPlayersTextI));
        smallestQuantOfPlayersTextI.setText(game.getSmallestQuantOfPlayers());

        biggestQuantOfPlayersTextI = (EditText)(findViewById(R.id.biggestQuantOfPlayersTextI));
        biggestQuantOfPlayersTextI.setText(game.getBiggestQuantOfPlayers());

        categoriesTextI = (EditText)(findViewById(R.id.categoriesTextI));
        categoriesTextI.setText(game.getCategoriesToString());

        quantOfPointsTextI = (EditText)(findViewById(R.id.quantOfPointsTextI));
        quantOfPointsTextI.setText(game.getQuantOfPoints());

        favoriteButtonI = (ImageButton)(findViewById(R.id.favoriteButtonI));
        if (game.isFavorite())
            favoriteButtonI.setImageResource(R.drawable.ic_baseline_favorite_24);
        else favoriteButtonI.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        favoriteButtonI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.isFavorite()) {
                    favoriteButtonI.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    game.setIsFavorite(false);
                }
                else {
                    favoriteButtonI.setImageResource(R.drawable.ic_baseline_favorite_24);
                    game.setIsFavorite(true);
                }
            }
        });

        findViewById(R.id.updateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateGame();
                save();
            }
        });
        findViewById(R.id.removeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeGame();
                save();
            }
        });

        imageViewI = (ImageView)(findViewById(R.id.imageViewI));
        try {
            final InputStream imageStream = openFileInput(game.getPhotoPath());
            final Bitmap image = BitmapFactory.decodeStream(imageStream);
            imageViewI.setImageBitmap(image);
            ((EditText)findViewById(R.id.photoPathTextI)).setText(game.getPhotoPath());
            //holder.ivGameImage.setImageURI(imageUri);
        } catch (FileNotFoundException e) {
            //Toast.makeText(context, "FileNotFoundException: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (Exception e) {
            //Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        imageViewI.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, AddGameFragment.PICK_IMAGE);
            }
        });
    }

    public void updateGame(){
        game.setName(nameTextI.getText().toString());
        game.setDescription(descriptionTextI.getText().toString());
        game.setPhotoPath(photoPathTextI.getText().toString());
        game.setRules(rulesTextI.getText().toString());
        game.setPlace(placeTextI.getText().toString());
        game.setSmallestAge(Integer.parseInt(smallestAgeTextI.getText().toString()));
        game.setBiggestAge(Integer.parseInt(biggestAgeTextI.getText().toString()));
        game.setSmallestQuantOfPlayers(Integer.parseInt(smallestQuantOfPlayersTextI.getText().toString()));
        game.setBiggestQuantOfPlayers(Integer.parseInt(biggestQuantOfPlayersTextI.getText().toString()));
        game.setCategories(Arrays.asList(categoriesTextI.getText().toString().split(", ")));
        //game.setQuantOfPoints(Integer.parseInt(quantOfPointsTextI.getText().toString()));
        //boolean isFavorite = quantOfPoints == 5;
    }

    public void removeGame(){
        GamesProcessor.getGames().remove(game);
        Toast.makeText(this, "Гру було видалено", Toast.LENGTH_LONG).show();
        this.finish();
    }

    public void save(){

        boolean result = JSONHelper.exportToJSON(this, GamesProcessor.getGames());
        if(result){
            Toast.makeText(this, "Дані збережено", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Не вдалося зберегти дані", Toast.LENGTH_LONG).show();
        }
    }
}