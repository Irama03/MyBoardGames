package com.example.myboardgames;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class AddCategoryDialog extends Dialog {

    /**
     * public constructor with parameters
     * @param context
     */
    public AddCategoryDialog(@NonNull Context context, View.OnClickListener onClickListener) {
        super(context);
        setCancelable(true);
        setContentView(R.layout.dialog_add_category);

        //getWindow().setBackgroundDrawableResource(R.drawable.dialog_back);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText etCategoryName = findViewById(R.id.categoryName);
        Button b = findViewById(R.id.addCategory);

        b.setOnClickListener(onClickListener);

        etCategoryName.requestFocus();
    }

    /**
     * method is used to get category name from the dialog
     * @return
     */
    public String getCategoryName(){
        EditText name = findViewById(R.id.categoryName);
        return name.getText().toString().trim();
    }
}
