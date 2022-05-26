package com.example.myboardgames;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.example.myboardgames.database.AppDatabase;
import com.example.myboardgames.models.User;
import com.example.myboardgames.activities.CategoriesActivity;
import com.example.myboardgames.activities.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs = null;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDatabase = new AppDatabase();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_from_friends, R.id.navigation_games, R.id.navigation_add_game)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        prefs = getSharedPreferences("com.example.myboardgames", MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handles app bar item clicks.
     *
     * @param item Item clicked.
     * @return True if one of the defined items was clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_categories:
                Intent intent = new Intent(MainActivity.this,
                        CategoriesActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this,
                        ProfileActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                // Do nothing
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getString("userId", "").equals("")) {
            String uuid = UUID.randomUUID().toString();
            System.out.println("UUID: " + uuid);
            prefs.edit().putString("userId", uuid).apply();
            User user = new User(uuid, "");
            appDatabase.addUserToDatabase(user);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ((ImageView)findViewById(R.id.imageView)).setImageBitmap(selectedImage);

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
                        ((EditText)findViewById(R.id.photoPathText)).setText(fileName);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
    }
}