package com.example.myboardgames.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myboardgames.helpers.GamesProcessor;
import com.example.myboardgames.R;
import com.example.myboardgames.adapters.CategoryAdapter;

import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    private CategoryAdapter adapter;
    private RecyclerView recyclerView;
    private List<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        //setupActionBar();
        recyclerView = findViewById(R.id.recyclerViewCategories);
        categories = GamesProcessor.getCategories();
        //filterCategories = GamesProcessor.getCopyOfGames();
        adapter = new CategoryAdapter(this, categories, new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClicked(String category, int position) {
                //TODO: maybe delete listener
                Toast.makeText(CategoriesActivity.this, category + " on position " + position + " clicked", Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
