package com.example.myboardgames.ui.activities;

import android.os.Bundle;

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
        recyclerView = findViewById(R.id.recyclerViewCategories);
        categories = GamesProcessor.getCategories();
        adapter = new CategoryAdapter(this, categories, new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClicked(String category, int position) { }
        });
        recyclerView.setAdapter(adapter);
    }
}
