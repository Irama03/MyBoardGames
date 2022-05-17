package com.example.myboardgames.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myboardgames.R;

public class CategoryDialog extends Dialog {

    /**
     * public constructor with parameters
     * @param context
     */
    public CategoryDialog(@NonNull Context context, int dialogTitle, int categoryHint, int buttonLabel, String name, View.OnClickListener onClickListener) {
        super(context);
        setCancelable(true);
        setContentView(R.layout.dialog_add_edit_category);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView title = findViewById(R.id.dialogTitle);
        title.setText(dialogTitle);

        EditText etCategoryName = findViewById(R.id.categoryName);
        etCategoryName.setHint(categoryHint);
        etCategoryName.setText(name);

        Button cancel = findViewById(R.id.cancelCategory);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryDialog.this.dismiss();
            }
        });

        Button b = findViewById(R.id.doCategory);
        b.setText(buttonLabel);
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
