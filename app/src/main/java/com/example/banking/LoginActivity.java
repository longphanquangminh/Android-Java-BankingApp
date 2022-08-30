package com.example.banking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    CardView enterButton ;
    EditText cardNumberEt ;
    TextView createAccountBtn ;
    LocalDataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findAllViews();
        db = new LocalDataBaseHelper(this);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call login method
                String cardNumber = cardNumberEt.getText().toString().trim();
                String result = db.login(cardNumber);
                if(!result.equals("")){
                    Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                    i.putExtra("cardNumber",result);
                    startActivity(i);
                }

            }
        });


        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start activity to create account
                Intent intent = new Intent(LoginActivity.this,CreateAccountActivity.class);
                startActivity(intent);
            }
        });


    }

    private void findAllViews() {
        enterButton = findViewById(R.id.loginBtn);
        cardNumberEt = findViewById(R.id.cardnumber_et);
        createAccountBtn = findViewById(R.id.createAccount_btn);
    }

    // error dialog
    public void showAlertDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Wrong Card Number!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alertDialog.show();
    }
}