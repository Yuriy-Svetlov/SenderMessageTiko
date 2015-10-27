package ru.noticeword_00send.com.sendermessagetiko;



import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.rey.material.app.ThemeManager;
import com.rey.material.widget.Button;


public class LoginActivity extends AppCompatActivity {



    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button mEmailCreateButton;
    private Context con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.init(this, 1, 0, null);
        setContentView(R.layout.activity_login);


        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailCreateButton = (Button) findViewById(R.id.create_account_button);


        //Скрываем клавиатуру
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        con=this;


        //войти
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });




        mEmailCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent create_ac = new Intent(LoginActivity.this, CreateAccaunt.class);
                mEmailCreateButton.setEnabled(false);
                mEmailCreateButton.setTextColor(Color.parseColor("#0B0B3B"));
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mEmailCreateButton.setEnabled(true);
                        startActivity(create_ac);
                        overridePendingTransition(R.anim.slide, R.anim.anim2);
                    }
                }, 250);

            }
        });










    }

































}
