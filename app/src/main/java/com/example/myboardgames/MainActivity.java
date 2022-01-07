package com.example.myboardgames;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    //public List<Game> games;
    //public ArrayAdapter<Game> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_games, R.id.navigation_add_game)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //open();

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*public void open(ListView listView){
        games = JSONHelper.importFromJSON(this);
        if(games != null){
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, games);
            listView.setAdapter(adapter);
            Toast.makeText(this, "Дані відновлено", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Не вдалося відновити дані", Toast.LENGTH_LONG).show();
        }
    }*/

    /*private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }*/

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
                        ((EditText)findViewById(R.id.photoPathText)).setText(fileName);
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