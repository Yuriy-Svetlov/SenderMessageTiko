package ru.noticeword_00send.com.sendermessagetiko;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rey.material.app.ThemeManager;
import com.rey.material.widget.ProgressView;

import org.json.JSONObject;
import JsonClasses.CreateLoginJson;
import loaders.CustomJsonObject;

import loaders.HttpVolley;
import API.API;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;



/**
 * Created by Юрий on 17.10.2015.
 */
public class CreateAccaunt extends AppCompatActivity{

    private AutoCompleteTextView Login;
    private AutoCompleteTextView Lastname;
    private AutoCompleteTextView Name;
    private AutoCompleteTextView Email;
    private EditText Password;
    private EditText PasswordSame;
    private ProgressView mProgressView;
    private Button Button_Create;

    private API url;
    private static CountDownTimer timer;

    private Boolean lockA=false;
    private String responseA="";
    private static int getTimeA=7;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.init(this, 1, 0, null);
        setContentView(R.layout.create_account);

        //находим элементы
        Login = (AutoCompleteTextView) findViewById(R.id.login);
        Name = (AutoCompleteTextView) findViewById(R.id.name);
        Lastname = (AutoCompleteTextView) findViewById(R.id.lastname);
        Email = (AutoCompleteTextView) findViewById(R.id.email2);
        Password = (EditText) findViewById(R.id.password);
        PasswordSame = (EditText) findViewById(R.id.password2);
        mProgressView = (ProgressView) findViewById(R.id.login_progress2);
        Button_Create = (Button) findViewById(R.id.email_create_acc_button2);


        //Скрываем клавиатуру
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        url = new API();



        Button_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lockA=false;
                String responseA="";
                getTimeA=7;
                //проверка валидность
                if (ValidDataCheck() == true) {
                    Button_Create.setEnabled(false);
                    AddLoader();
                    showProgress(true, false);
                }
            }
        });




        if (savedInstanceState != null) {
            if (Login.getVisibility() != View.GONE) {

            }
        }

    }







    private void AddLoader(){
        HttpVolley.getInstance(this).addToRequestQueue(RequestCreate());
    }


    private CustomJsonObject RequestCreate(){
        CustomJsonObject jsObjRequest = new CustomJsonObject(Request.Method.POST, url.getUrlCreateAccount(), JsonToStringCA() , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                CreatePause(true, response.toString(), null, 7000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CreatePause(false, null, "error32", 7000);
            }
        });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return jsObjRequest;
    }





    private void CreatePause(final Boolean lock, final String response, final String error, int timeA){
        lockA=lock;
        responseA=response;
        timer = new CountDownTimer(timeA, 1000) {
            public void onTick(long millisUntilFinished) {
                getTimeA--;
                Log.i("111","getTime2 "+getTimeA);
            }
            public void onFinish() {

                if(lock==true){
                    ResponseOK(response);
                }else{
                    ResponseError(error);
                }
                getTimeA=7;
                Log.i("111","getTime "+getTimeA);
            }
        }.start();
    }

    private void ResponseOK(String response){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        CreateLoginJson jsonWrite = gson.fromJson(response, CreateLoginJson.class);
        if(jsonWrite.UserID!=234) {
            //записываем пароль и логин
            SharedPreferencesWrite(jsonWrite.Login, jsonWrite.Password);
            showProgress(false, false);
        }else{
            //ошибка - дубликат
            Log.i("111", "некорректный пароль");
            showProgress(false, true);
        }
        Button_Create.setEnabled(true);
    }


    private void ResponseError(String  error){
        //ошибка коннекта
        Log.i("111","ResponseError ");
        showProgress(false, true);
        Button_Create.setEnabled(true);
    }

    @NonNull
    private String JsonToStringCA() {
        CreateLoginJson jsonTo = new CreateLoginJson();
        jsonTo.Login = getLogin();
        jsonTo.Name =  getName();
        jsonTo.Email =  getEmail();
        jsonTo.Password = getPassword();
        if (!getLastname().equals("NONE")) {
            jsonTo.Lastname = getLastname();
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(jsonTo).toString();
    }



















    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("Login", Login.getVisibility());

        savedInstanceState.putBoolean("lockB", lockA);
        savedInstanceState.putString("responseB", responseA);
        savedInstanceState.putInt("getTimeB", getTimeA);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int Login_State = 0;
        boolean lockB = false;
        String responseB = "";
        int getTimeB = 0;
        Login_State = savedInstanceState.getInt("Login");
        lockB = savedInstanceState.getBoolean("lockB");
        responseB = savedInstanceState.getString("responseB");
        getTimeB = savedInstanceState.getInt("getTimeB");

        if(Login_State==View.GONE){
            show(true);
            if(timer!=null){
                timer.cancel();
            }
            if(getTimeB!=0){
                if(getTimeB>7){
                    getTimeB=7;
                }
                CreatePause(lockB, responseB, responseB ,getTimeB*1000 );
            }
        }
    }









    private boolean ValidDataCheck(){
    boolean lock=false;
        if (!Login.getText().toString().isEmpty() &&
            !Name.getText().toString().isEmpty() &&
            !Email.getText().toString().isEmpty() &&
            !Password.getText().toString().isEmpty() &&
            !PasswordSame.getText().toString().isEmpty()) {

            if(PasswordSame.getText().toString().equals(Password.getText().toString())){

                if(Password.getText().toString().length()>=6 && Password.getText().toString().length()<=20){

                    if(Email.getText().toString().contains("@") && Email.getText().toString().contains(".")){
                        lock=true;
                    }else{
                          Email.setError(getString(R.string.error_invalid_email));
                          Email.requestFocus();
                    }
                }else{
                     Password.setError(getString(R.string.action_sign_in_short));
                     Password.requestFocus();
                     }
            }else{
                  PasswordSame.setError(getString(R.string.error_incorrect_password));
                  PasswordSame.requestFocus();
                  }

        }else if (Login.getText().toString().isEmpty()){
                  Login.setError(getString(R.string.error_field_required));
                  Login.requestFocus();
                  }else if (Name.getText().toString().isEmpty()){
                            Name.setError(getString(R.string.error_field_required));
                            Name.requestFocus();
                            }else if (Email.getText().toString().isEmpty()){
                                      Email.setError(getString(R.string.error_field_required));
                                      Email.requestFocus();
                                      }else if (Password.getText().toString().isEmpty()){
                                                Password.setError(getString(R.string.error_field_required));
                                                Password.requestFocus();
                                                }else if (PasswordSame.getText().toString().isEmpty()){
                                                          PasswordSame.setError(getString(R.string.error_field_required));
                                                          PasswordSame.requestFocus();
                                                          }
    return lock;
    }






























    private void SharedPreferencesWrite(String Login, String Password){
        SharedPreferences ShaLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ShaLogin.edit();
        editor.putString("Login", Login);
        editor.putString("Password", Password);
        editor.apply();
    }








    private void showProgress(final boolean show, final boolean errors) {
        final int time = 300;
        if(show==true){
            //форма
            Animate(show);
            Login.animate().setDuration(time).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
                public void onAnimationEnd(Animator animation) {
                         //форма
                         Visibility(show);
                         //Прогресс-бар
                         mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                         mProgressView.start();
                         Login.animate().setListener(null);
                 }
            });
        }else{
              mProgressView.stop();

              if(errors==true) {
                  mProgressView.animate().setDuration(1000).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                     @Override
                     public void onAnimationEnd(Animator animation) {
                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                         Visibility(show);
                         Animate(show);
                     }
                  });
              }
        }
    }




    private void Visibility(final boolean show){
        Login.setVisibility(show ? View.GONE : View.VISIBLE);
        Name.setVisibility(show ? View.GONE : View.VISIBLE);
        Lastname.setVisibility(show ? View.GONE : View.VISIBLE);
        Email.setVisibility(show ? View.GONE : View.VISIBLE);
        Password.setVisibility(show ? View.GONE : View.VISIBLE);
        PasswordSame.setVisibility(show ? View.GONE : View.VISIBLE);
        Button_Create.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void Animate(final boolean show){
        final int time = 300;
        if(show==false) {
            Login.animate().setDuration(time).alpha(show ? 0 : 1);
        }
        Name.animate().setDuration(time).alpha(show ? 0 : 1);
        Lastname.animate().setDuration(time).alpha(show ? 0 : 1);
        Email.animate().setDuration(time).alpha(show ? 0 : 1);
        Password.animate().setDuration(time).alpha(show ? 0 : 1);
        PasswordSame.animate().setDuration(time).alpha(show ? 0 : 1);
        Button_Create.animate().setDuration(time).alpha(show ? 0 : 1);
    }

    private void show(boolean show){
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        Visibility(show);
        Login.animate().alpha(show ? 0 : 1);
        Name.animate().alpha(show ? 0 : 1);
        Lastname.animate().alpha(show ? 0 : 1);
        Email.animate().alpha(show ? 0 : 1);
        Password.animate().alpha(show ? 0 : 1);
        PasswordSame.animate().alpha(show ? 0 : 1);
        Button_Create.animate().alpha(show ? 0 : 1);
        mProgressView.start();
    }



















    public String getLogin(){
        String login = "NONE";
        if(!Login.getText().toString().isEmpty()){
            login = Login.getText().toString().trim();
        }
        return login;
    }

    public String getName(){
        String name = "NONE";
        if(!Name.getText().toString().isEmpty()){
            name = Name.getText().toString().trim();
        }
        return name;
    }

    public String getEmail(){
        String email = "NONE";
        if(!Email.getText().toString().isEmpty()){
            email = Email.getText().toString().trim();
        }
        return email;
    }

    public String getPassword(){
        String password = "NONE";
        if(!Password.getText().toString().isEmpty()){
            password = Password.getText().toString().trim();
        }
        return password;
    }

    public String getLastname(){
        String lastname = "NONE";
        if(!Lastname.getText().toString().isEmpty()){
            lastname = Lastname.getText().toString().trim();
        }
        return lastname;
    }






    @Override
    protected void onStop() {
        super.onStop();
        if(timer!=null){
            timer.cancel();
            if (Login.getVisibility() == View.GONE) {
                showProgress(false, true);
                Button_Create.setEnabled(true);
            }
        }
    }





}
