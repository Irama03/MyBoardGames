package com.example.myboardgames.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.myboardgames.R;

public class ShareGameDialog extends Dialog {

    public ShareGameDialog(@NonNull Context context, View.OnClickListener onClickListener) {
        super(context);
        setCancelable(true);
        setContentView(R.layout.dialog_share_game);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button cancel = findViewById(R.id.cancelShare);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareGameDialog.this.dismiss();
            }
        });

        Button b = findViewById(R.id.shareGame);
        b.setOnClickListener(onClickListener);

        findViewById(R.id.friendName).requestFocus();
    }

    public String getFriendName(){
        EditText name = findViewById(R.id.friendName);
        return name.getText().toString().trim();
    }
}
