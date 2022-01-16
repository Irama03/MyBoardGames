package com.example.myboardgames.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myboardgames.ButtonsActions;
import com.example.myboardgames.Game;
import com.example.myboardgames.GamesProcessor;
import com.example.myboardgames.R;
import com.example.myboardgames.Utils;
import com.example.myboardgames.ui.addGame.AddGameFragment;
//import com.example.myboardgames.ui.games.AdapterInterface;
import com.github.thunder413.datetimeutils.DateTimeUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;

public class GameInfoActivity extends AppCompatActivity {

    private EditText nameTextI, descriptionTextI, photoPathTextI, rulesTextI, placeTextI;
    private Spinner smallestAgeSpI, biggestAgeSpI, smallestQuantOfPlayersSpI;
    private Spinner biggestQuantOfPlayersSpI, playingTimeSpI;
    private EditText categoriesTextI;
    private TextView quantOfTimesBeingChosenText, dateOfAddingText, dateOfLastChoosingText, btnBack;
    private ImageView imageViewI;
    private ImageButton ibStar1, ibStar2, ibStar3, ibStar4, ibStar5, favoriteButtonI, checkButtonI;
    private ImageButton[] stars = new ImageButton[5];

    //private TextView mTextView;
    private Game game;
    //private int gamePosition;
    //private FrameLayout mContainer;
    //private GameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        //mContainer = findViewById(R.id.nav_host_fragment_container);
        Game gameCopy = (Game)getIntent().getSerializableExtra("Game");
        //gamePosition = (int)getIntent().getSerializableExtra("GamePosition");
        //game = GamesProcessor.getGames().get(gamePosition);
        game = GamesProcessor.getGameByName(gameCopy.getName());
        //adapterInterface = (AdapterInterface) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        //adapter = (RecyclerView.Adapter<GameAdapter.ViewHolder>)getIntent().getParcelableExtra("Adapter");
        //mTextView = (TextView) findViewById(R.id.text);
        //mTextView.setText(game.getName());

        btnBack = (TextView)(findViewById(R.id.btnBack));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nameTextI = (EditText)(findViewById(R.id.nameTextI));
        nameTextI.setText(game.getName());

        descriptionTextI = (EditText)(findViewById(R.id.descriptionTextI));
        descriptionTextI.setText(game.getDescription());

        photoPathTextI = (EditText)(findViewById(R.id.photoPathTextI));
        //photoPathTextI.setText(game.getPhotoPath());

        rulesTextI = (EditText)(findViewById(R.id.rulesTextI));
        rulesTextI.setText(game.getRules());

        placeTextI = (EditText)(findViewById(R.id.placeTextI));
        placeTextI.setText(game.getPlace());

        smallestAgeSpI = (Spinner)(findViewById(R.id.smallestAgeSpI));
        initSpinnerAndSetSelection(R.array.years, smallestAgeSpI, String.valueOf(game.getSmallestAge()));

        biggestAgeSpI = (Spinner)(findViewById(R.id.biggestAgeSpI));
        initSpinnerAndSetSelection(R.array.years, biggestAgeSpI, String.valueOf(game.getBiggestAge()));

        smallestQuantOfPlayersSpI = (Spinner)(findViewById(R.id.smallestQuantOfPlayersSpI));
        initSpinnerAndSetSelection(R.array.quantOfPlayers, smallestQuantOfPlayersSpI, String.valueOf(game.getSmallestQuantOfPlayers()));

        biggestQuantOfPlayersSpI = (Spinner)(findViewById(R.id.biggestQuantOfPlayersSpI));
        initSpinnerAndSetSelection(R.array.quantOfPlayers, biggestQuantOfPlayersSpI, String.valueOf(game.getBiggestQuantOfPlayers()));

        playingTimeSpI = (Spinner)(findViewById(R.id.playingTimeSpI));
        initSpinnerAndSetSelection(R.array.time, playingTimeSpI, game.getPlayingTime());

        categoriesTextI = (EditText)(findViewById(R.id.categoriesTextI));
        categoriesTextI.setText(game.getCategoriesToString());

        quantOfTimesBeingChosenText = (TextView)(findViewById(R.id.quantOfTimesBeingChosenText));
        quantOfTimesBeingChosenText.setText(game.getQuantOfTimesBeingChosen() + "");

        dateOfAddingText = (TextView)(findViewById(R.id.dateOfAddingText));
        if (game.getDateOfAdding() == null)
            dateOfAddingText.setText("No adding date");
        //else dateOfAddingText.setText(DateTimeUtils.formatDate(new Date(), Locale.getDefault()));
        else dateOfAddingText.setText(DateTimeUtils.formatDate(game.getDateOfAdding(), Locale.getDefault()));

        dateOfLastChoosingText = (TextView)(findViewById(R.id.dateOfLastChoosingText));
        if (game.getDateOfLastChoosing() == null)
            dateOfLastChoosingText.setText("No choosing date");
        else dateOfLastChoosingText.setText(DateTimeUtils.formatDate(game.getDateOfLastChoosing()));

        ibStar1 = (ImageButton)findViewById(R.id.ibStar1);
        ibStar2 = (ImageButton)findViewById(R.id.ibStar2);
        ibStar3 = (ImageButton)findViewById(R.id.ibStar3);
        ibStar4 = (ImageButton)findViewById(R.id.ibStar4);
        ibStar5 = (ImageButton)findViewById(R.id.ibStar5);
        stars[0] = ibStar1;
        stars[1] = ibStar2;
        stars[2] = ibStar3;
        stars[3] = ibStar4;
        stars[4] = ibStar5;

        favoriteButtonI = (ImageButton)(findViewById(R.id.favoriteButtonI));
        checkButtonI = (ImageButton)(findViewById(R.id.checkGameI));

        ButtonsActions.favoriteAction(game, favoriteButtonI, this);
        ButtonsActions.chooseAction(game, checkButtonI, this, quantOfTimesBeingChosenText, dateOfLastChoosingText);
        ButtonsActions.pointsAction(game, stars, this);

        findViewById(R.id.updateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateGame();
                GamesProcessor.saveGames(GameInfoActivity.this);
                //Toast.makeText(GameInfoActivity.this, "updating", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.removeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeGame();
                GamesProcessor.saveGames(GameInfoActivity.this);
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

    private void initSpinnerAndSetSelection(@ArrayRes int id, Spinner spinner, String str) {
        String[] arr = getResources().getStringArray(id);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spinner_layout,R.id.spinnerItem,arr);
        spinner.setAdapter(adapter);
        if (str != null)
            spinner.setSelection(Utils.getPositionOfStr(str, arr));
    }

    private void updateGame(){
        game.setName(nameTextI.getText().toString());
        game.setDescription(descriptionTextI.getText().toString());
        game.setPhotoPath(photoPathTextI.getText().toString());
        game.setRules(rulesTextI.getText().toString());
        game.setPlace(placeTextI.getText().toString());
        game.setSmallestAge(Integer.parseInt((String)smallestAgeSpI.getSelectedItem()));
        game.setBiggestAge(Integer.parseInt((String)biggestAgeSpI.getSelectedItem()));
        game.setSmallestQuantOfPlayers(Integer.parseInt((String)smallestQuantOfPlayersSpI.getSelectedItem()));
        game.setBiggestQuantOfPlayers(Integer.parseInt((String)biggestQuantOfPlayersSpI.getSelectedItem()));
        game.setPlayingTime((String)playingTimeSpI.getSelectedItem());
        game.setCategories(Arrays.asList(categoriesTextI.getText().toString().split(", ")));
        //g.setQuantOfPoints(Integer.parseInt(quantOfPointsTextI.getText().toString()));
        //boolean isFavorite = quantOfPoints == 5;
        Toast.makeText(this, "Інформацію про гру було оновлено", Toast.LENGTH_LONG).show();
    }

    public void removeGame(){
        //String res = "There were " + GamesProcessor.getGames().size() + " games. Game "
                //+ game.getName() + " on position " + gamePosition + " is deleting... ";
        GamesProcessor.getGames().remove(game);
        //res = res + "Now there are " + GamesProcessor.getGames().size() + " games.";
        //Toast.makeText(this, res, Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Гру було видалено", Toast.LENGTH_LONG).show();
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        //Toast.makeText(this, "In onActivityResult()", Toast.LENGTH_LONG).show();

        //if (requestCode == Pick_image) {
        //Toast.makeText(this, "requestCode: " + requestCode, Toast.LENGTH_LONG).show();
        if(resultCode == RESULT_OK){
            try {

                //Получаем URI изображения, преобразуем его в Bitmap
                //объект и отображаем в элементе ImageView нашего интерфейса:
                //Toast.makeText(this, "resultCode == RESULT_OK. Before getting uri", Toast.LENGTH_LONG).show();
                final Uri imageUri = imageReturnedIntent.getData();
                //Toast.makeText(this, "Got uri: " + imageUri.toString(), Toast.LENGTH_LONG).show();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ((ImageView)findViewById(R.id.imageViewI)).setImageBitmap(selectedImage);

                InputStream is = getContentResolver().openInputStream(imageUri);
                FileOutputStream fileOutputStream = null;
                String[] fN = imageUri.toString().split("/");
                String fileName = fN[fN.length - 1];

                try {
                    fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, length);
                    }
                    //fileOutputStream.write(selectedImage.getNinePatchChunk());
                    //copyFileUsingStream();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                ((EditText)findViewById(R.id.photoPathTextI)).setText(fileName);
                //Toast.makeText(this, fileName, Toast.LENGTH_LONG).show();
                //Uri uri = Uri.parse(imageUri.toString());

                //Toast.makeText(this, "Uri set!", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                //Toast.makeText(this, "FileNotFoundException: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

        //}
        //else {
        //   Toast.makeText(this, "Error. requestCode == " + requestCode, Toast.LENGTH_LONG).show();
        //}
    }
}