package com.example.myboardgames;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class AddCategoryDialog extends Dialog {

    /**
     * public constructor with parameters
     * @param context
     */
    public AddCategoryDialog(@NonNull Context context) {
        super(context);
        setCancelable(true);
        setContentView(R.layout.dialog_add_category);

        /*getWindow().setBackgroundDrawableResource(R.drawable.dialog_back);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView title = findViewById(R.id.dialogTitle);
        title.setText(stringTitle);

        TextView message = findViewById(R.id.dialogMessage);
        message.setText(stringMessage);

        EditText mail = findViewById(R.id.resetEmail);
        mail.setHint(emailHint);

        Button b = findViewById(R.id.btnSend);
        b.setText(buttonLabel);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Utils.isNetworkAvailable(LoginActivity.this)){
                    Utils.showToast(LoginActivity.this,R.string.noInternetConnection);
                    return;
                }
                String email = resetDialog.getResetEmail();
                if(!Utils.validateEmail(email)){
                    Utils.showToast(LoginActivity.this,R.string.invalidMail);
                }
                else{
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                resetDialog.dismiss();
                                Utils.showToast(LoginActivity.this,R.string.resetSuccessful);
                            }else {
                                resetDialog.dismiss();
                                Utils.showToast(LoginActivity.this,R.string.resetNotSuccessful);
                            }
                        }
                    });
                }
            }
        });

        mail.requestFocus();*/
    }

    /**
     * method is used to get email from the dialog
     * @return
     */
    public String getCategoryName(){
        EditText name = findViewById(R.id.resetEmail);
        return name.getText().toString().trim();
    }
}
