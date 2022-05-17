package com.example.myboardgames.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myboardgames.database.AppDatabase;
import com.example.myboardgames.helpers.ButtonsActions;
import com.example.myboardgames.models.SharedGame;
import com.example.myboardgames.models.User;
import com.example.myboardgames.ui.dialogs.CategoryDialog;
import com.example.myboardgames.models.Game;
import com.example.myboardgames.helpers.GamesProcessor;
import com.example.myboardgames.R;
import com.example.myboardgames.helpers.Utils;
import com.example.myboardgames.ui.addGame.AddGameFragment;
import com.example.myboardgames.ui.dialogs.ShareGameDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameInfoActivity extends AppCompatActivity {

    private EditText nameTextI, descriptionTextI, photoPathTextI, rulesTextI, placeTextI;
    private Spinner smallestAgeSpI, biggestAgeSpI, smallestQuantOfPlayersSpI;
    private Spinner biggestQuantOfPlayersSpI, playingTimeSpI;
    private TextView categoriesTextI;
    private boolean[] selectedCategories;
    private ArrayList<Integer> categoriesList;
    private String[] categories;
    private TextView quantOfTimesBeingChosen, dateOfAdding, dateOfLastChoosing, btnBack;
    private ImageView imageViewI;
    private ImageButton ibStar1, ibStar2, ibStar3, ibStar4, ibStar5;
    private ImageButton favoriteButtonI, checkButtonI, addCategoryButtonI;
    private ImageButton[] stars = new ImageButton[5];
    private ImageButton shareButton;
    private CategoryDialog categoryDialog;
    private ShareGameDialog shareGameDialog;

    SharedPreferences prefs = null;
    private AppDatabase appDatabase;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);

        prefs = getSharedPreferences("com.example.myboardgames", MODE_PRIVATE);
        appDatabase = new AppDatabase();
        Game gameCopy = (Game)getIntent().getSerializableExtra("Game");
        game = GamesProcessor.getGameByName(gameCopy.getName());

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

        categoriesTextI = (TextView)(findViewById(R.id.categoriesTextI));
        categoriesTextI.setText(game.getCategoriesToString());
        initMultiSpinner();
        ButtonsActions.setCategoriesListener(categoriesTextI, this, categoriesList, categories, selectedCategories);

        quantOfTimesBeingChosen = (TextView)(findViewById(R.id.quantOfTimesBeingChosen));
        quantOfTimesBeingChosen.setText(game.getQuantOfTimesBeingChosen() + "");

        dateOfAdding = (TextView)(findViewById(R.id.dateOfAdding));
        if (game.getDateOfAdding() == null)
            dateOfAdding.setText("невідомо");
        else dateOfAdding.setText(Utils.convertDateToLocalString(game.getDateOfAdding()));

        dateOfLastChoosing = (TextView)(findViewById(R.id.dateOfLastChoosing));
        if (game.getDateOfLastChoosing() == null)
            dateOfLastChoosing.setText("немає");
        else dateOfLastChoosing.setText(Utils.convertDateToLocalString(game.getDateOfLastChoosing()));

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

        addCategoryButtonI = (ImageButton)(findViewById(R.id.addCategoryButtonI));
        addCategoryButtonI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryDialog = new CategoryDialog(GameInfoActivity.this, R.string.dialog_title_add,
                        R.string.dialog_category_name_hint_add, R.string.dialog_button_add, "",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String categoryName = categoryDialog.getCategoryName();
                                if(!Utils.validateName(categoryName))
                                    Toast.makeText(GameInfoActivity.this, "Введіть непорожню назву категорії!", Toast.LENGTH_LONG).show();
                                else if (GamesProcessor.categoryNameAlreadyExists(categoryName))
                                    Toast.makeText(GameInfoActivity.this, "Така категорія вже існує!", Toast.LENGTH_LONG).show();
                                else{
                                    GamesProcessor.addCategory(categoryName);
                                    GamesProcessor.saveCategories(GameInfoActivity.this);
                                    int size = GamesProcessor.getCategories().size();
                                    String[] helpCategories = new String[size];
                                    System.arraycopy(categories, 0, helpCategories, 0, size - 1);
                                    helpCategories[size-1] = categoryName;
                                    categories = helpCategories;
                                    boolean[] helpSelectedCategories = new boolean[size];
                                    System.arraycopy(selectedCategories, 0, helpSelectedCategories, 0, size - 1);
                                    selectedCategories = helpSelectedCategories;
                                    ButtonsActions.setCategoriesListener(categoriesTextI, GameInfoActivity.this, categoriesList, categories, selectedCategories);
                                    categoryDialog.dismiss();
                                }
                            }
                        });
                categoryDialog.show();
            }
        });

        favoriteButtonI = (ImageButton)(findViewById(R.id.favoriteButtonI));
        checkButtonI = (ImageButton)(findViewById(R.id.checkGameI));

        ButtonsActions.favoriteAction(game, favoriteButtonI, this);
        ButtonsActions.chooseAction(game, checkButtonI, this, quantOfTimesBeingChosen, dateOfLastChoosing);
        ButtonsActions.pointsAction(game, stars, this);

        shareButton = (ImageButton)(findViewById(R.id.shareGame));
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareGameDialog = new ShareGameDialog(GameInfoActivity.this, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String friendName = shareGameDialog.getFriendName();
                                if(!Utils.validateName(friendName))
                                    Toast.makeText(GameInfoActivity.this, "Введіть непорожню інформацію про друга!", Toast.LENGTH_LONG).show();
                                else if (friendName.equals(prefs.getString("userId", "")) || friendName.equals(prefs.getString("username", ""))) {
                                    Toast.makeText(GameInfoActivity.this, "Ігри не можна рекомендувати собі)", Toast.LENGTH_LONG).show();
                                }
                                else if (!Utils.isNetworkAvailable(GameInfoActivity.this)) {
                                    Toast.makeText(GameInfoActivity.this, "Неможливо порекомендувати гру. Мережа Інтернет відсутня", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    appDatabase.usernameExists(friendName).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()) {

                                                DatabaseReference databaseReference = appDatabase.getReferenceToGroup(AppDatabase.USERS_GROUP_KEY);
                                                ValueEventListener valueEventListener = new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            User u = dataSnapshot.getValue(User.class);
                                                            assert u != null;
                                                            if (u.getUsername().equals(friendName)) {
                                                                recommendGame(u.getId());
                                                                break;
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                };
                                                databaseReference.addValueEventListener(valueEventListener);
                                            }
                                            else {
                                                appDatabase.userIdExists(friendName).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.exists()) {

                                                            DatabaseReference databaseReference = appDatabase.getReferenceToGroup(AppDatabase.USERS_GROUP_KEY);
                                                            ValueEventListener valueEventListener = new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                        User u = dataSnapshot.getValue(User.class);
                                                                        assert u != null;
                                                                        if (u.getId().equals(friendName)) {
                                                                            recommendGame(u.getId());
                                                                            break;
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            };
                                                            databaseReference.addValueEventListener(valueEventListener);
                                                        }
                                                        else {
                                                            Toast.makeText(GameInfoActivity.this, "Такого користувача не існує", Toast.LENGTH_LONG).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                                    });
                                }
                            }
                        });
                shareGameDialog.show();
            }
        });

        findViewById(R.id.updateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateGame())
                    GamesProcessor.saveGames(GameInfoActivity.this);
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
        if (!game.getPhotoPath().equals("")) {
            try {
                final InputStream imageStream = openFileInput(game.getPhotoPath());
                final Bitmap image = BitmapFactory.decodeStream(imageStream);
                imageViewI.setImageBitmap(image);
                ((EditText)findViewById(R.id.photoPathTextI)).setText(game.getPhotoPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    private void recommendGame(String friendId) {
        DatabaseReference databaseReference = appDatabase.getReferenceToGroup(AppDatabase.SHARED_GAMES_GROUP_KEY);
        final boolean[] recoFound = {false};
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!recoFound[0]) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        SharedGame sharedGame = dataSnapshot.getValue(SharedGame.class);
                        assert sharedGame != null;
                        if (sharedGame.getName().equals(game.getName()) && sharedGame.getReceiverId().equals(friendId)) {
                            Toast.makeText(GameInfoActivity.this, "Ця гра вже була порекомендована цьому користувачу", Toast.LENGTH_LONG).show();
                            recoFound[0] = true;
                            break;
                        }
                    }
                }
                if (!recoFound[0]) {
                    SharedGame sharedGameNew = new SharedGame(game, prefs.getString("userId", ""), friendId);
                    appDatabase.addSharedGameToDatabase(sharedGameNew);
                    Toast.makeText(GameInfoActivity.this, "Гру успішно порекомендовано", Toast.LENGTH_LONG).show();
                    shareGameDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }

    private void initSpinnerAndSetSelection(@ArrayRes int id, Spinner spinner, String str) {
        String[] arr = getResources().getStringArray(id);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spinner_layout,R.id.spinnerItem,arr);
        spinner.setAdapter(adapter);
        if (str != null)
            spinner.setSelection(Utils.getPositionOfStr(str, arr));
    }

    /**
     * method is used to initialise multispinner
     */
    private void initMultiSpinner() {
        List<String> listCategories = GamesProcessor.getCategories();
        categories = listCategories.toArray(new String[0]);
        categoriesList = new ArrayList<>();
        selectedCategories = new boolean[categories.length];
        for (String category: game.getCategories()) {
            int position = Utils.getPositionOfStr(category, categories);
            categoriesList.add(position);
            selectedCategories[position] = true;
            Collections.sort(categoriesList);
        }
    }

    private boolean updateGame(){
        String name = nameTextI.getText().toString().trim();
        if (name.length() == 0) {
            Toast.makeText(this, "Введіть назву гри!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (!name.equalsIgnoreCase(game.getName()) && GamesProcessor.gameAlreadyExists(name)) {
            Toast.makeText(this, "Гра з такою назвою вже існує", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            game.setName(name);
            game.setDescription(descriptionTextI.getText().toString());
            game.setPhotoPath(photoPathTextI.getText().toString());
            game.setRules(rulesTextI.getText().toString());
            game.setPlace(placeTextI.getText().toString());
            game.setSmallestAge(Integer.parseInt((String)smallestAgeSpI.getSelectedItem()));
            game.setBiggestAge(Integer.parseInt((String)biggestAgeSpI.getSelectedItem()));
            game.setSmallestQuantOfPlayers(Integer.parseInt((String)smallestQuantOfPlayersSpI.getSelectedItem()));
            game.setBiggestQuantOfPlayers(Integer.parseInt((String)biggestQuantOfPlayersSpI.getSelectedItem()));
            game.setPlayingTime((String)playingTimeSpI.getSelectedItem());
            List<String> categories = Arrays.asList(categoriesTextI.getText().toString().split("\\s*,\\s*"));
            if (categories.get(0).equals("")) {
                categories = new ArrayList<>();
                categories.add("загальна категорія");
            }
            game.setCategories(categories);
            Toast.makeText(this, "Інформацію про гру було оновлено", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    public void removeGame(){
        GamesProcessor.deleteGame(game);
        Toast.makeText(this, "Гру було видалено", Toast.LENGTH_LONG).show();
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK){
            try {
                final Uri imageUri = imageReturnedIntent.getData();
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}