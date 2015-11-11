package ru.noticeword_00send.com.sendermessagetiko;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;
import com.rey.material.widget.ProgressView;

import org.json.JSONObject;
import Model.CreateLoginJson;
import loaders.CustomJsonObject;

import loaders.HttpVolley;
import API.API;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;



/**
 *
 * Используется для регистрации нового пользовательского аккаунта.
 * @author Юрий
 * @version  5.1 , 31/07/2015
 *
 *
 */
public class CreateAccaunt extends AppCompatActivity {

    private EditText Login;
    private EditText Lastname;
    private EditText Name;
    private EditText Email;
    private EditText Password;
    private EditText PasswordSame;
    private ProgressView mProgressView;
    private Button Button_Create;
    private CoordinatorLayout content;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private AppBarLayout.LayoutParams toolbarLayoutParams;


    private API url;
    private static CountDownTimer timer;
    private OnOffsetChangedListener LisnerOn = null;


    private Boolean lockA=false;
    private String responseA="";
    private static int getTimeA=7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.init(this, 1, 0, null);
        setContentView(R.layout.create_account);


        Login = (EditText) findViewById(R.id.login);
        Name = (EditText) findViewById(R.id.name);
        Lastname = (EditText) findViewById(R.id.lastname);
        Email = (EditText) findViewById(R.id.email2);
        Password = (EditText) findViewById(R.id.password);
        PasswordSame = (EditText) findViewById(R.id.password2);

        mProgressView = (ProgressView) findViewById(R.id.create_account_progress);
        Button_Create = (Button) findViewById(R.id.create_acc_button);
        appbar = (AppBarLayout) findViewById(R.id.bar);
        content = (CoordinatorLayout) findViewById(R.id.content);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbarLayoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        url = new API();
        initViews(appbar);


        Button_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lockA = false;
                responseA = "";
                getTimeA = 7;
                if (ValidDataCheck()) {
                    Button_Create.setEnabled(false);
                    AddLoader();
                    showProgress(true, false);
                }
            }
        });

    }





    /**
     * Отправляет сформированый запрос на сервер, для создания пользовательского аккаунта.
     * @see CreateAccaunt#RequestCreate()
     */
    private void AddLoader(){
        HttpVolley.getInstance(this).addToRequestQueue(RequestCreate());
    }

    /**
     * Формирует запрос для создания аккаунта, но не отправляет его.
     * @return CustomJsonObject - Возвращает сформированный запрос, и регистрирует слушатели для ответа, onResponse() и  ErrorListener().
     * При ответе на запрос, слушаетли вызывают {@link CreateAccaunt#CreatePause(Boolean lock, String response,int timeA)}
     * @see CreateAccaunt#AddLoader()
     */
    private CustomJsonObject RequestCreate(){
        CustomJsonObject jsObjRequest = new CustomJsonObject(Request.Method.POST, url.getUrlCreateAccount(), JsonToStringCA() , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                CreatePause(true, response.toString(), 7000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CreatePause(false, null, 7000);
            }
        });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return jsObjRequest;
    }


    /**
     * Эмулирует паузу в процессе отправки запроса на сервер.
     * @param lock  если true , будет вызван метод {@link CreateAccaunt#ResponseOK(String response)}
     *              , если false {@link CreateAccaunt#ResponseError()}.
     * @param response   данные ответа от сервера, в формате JSON, могуть быть NULL.
     * @param timeA  продолжительность задержки паузы в миллисекундах.
     *
     * @see CreateAccaunt#ResponseOK(String response)
     * @see CreateAccaunt#ResponseError()
     */
    private void CreatePause(final Boolean lock,final String response,int timeA){
        lockA=lock;
        responseA=response;
        timer = new CountDownTimer(timeA, 1000) {
            public void onTick(long millisUntilFinished) {
                getTimeA--;
            }
            public void onFinish() {
                if(lock==true){
                    ResponseOK(response);
                }else{
                    ResponseError();
                }
                getTimeA=7;
            }
        }.start();
    }

    /**
     * Читает полученные данные от сервера в формате JSON. Если аккаунт был успешно создан,
     * вызывается метод {@link CreateAccaunt#SharedPreferencesWrite(String Login, String Password)}.
     * Если  аккаунт не был создан по запрошенным данным, вызывается {@link CreateAccaunt#SnackBarErrors(String erros)}
     * @param response - данные в формате JSON
     * @see CreateAccaunt#ResponseError()
     */
    private void ResponseOK(String response){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        CreateLoginJson jsonWrite = gson.fromJson(response, CreateLoginJson.class);
        if(jsonWrite.UserID!=234) {
            SharedPreferencesWrite(jsonWrite.Login, jsonWrite.Password, jsonWrite.UserID);
            showProgress(false, false);
        }else{
            SnackBarErrors(getResources().getString(R.string.SnackBareErrorsLogin));
            showProgress(false, true);
        }
        Button_Create.setEnabled(true);
    }


    /**
     * Выводит сообщение об ошибки сети
     * @see CreateAccaunt#ResponseOK(String response)
     */
    private void ResponseError(){
        //SnackBarErrors(getResources().getString(R.string.SnackBareErrorsConnect));
        showProgress(false, true);
        Button_Create.setEnabled(true);
    }


    /**
     * Преобразовывает данные для создания аккаунта, в формат JSON
     * @return json
     *
     *  @see CreateAccaunt#getLogin()
     *  @see CreateAccaunt#getName()
     *  @see CreateAccaunt#getEmail()
     *  @see CreateAccaunt#getPassword()
     *  @see CreateAccaunt#getLastname()
     */
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
                CreatePause(lockB, responseB, getTimeB * 1000);
            }
        }

    }


    /**
     * Проверяет все поля формы ввода создания пользовательского аккаунта, на валидность
     *
     * @return lock валидные данные = true
     */
    private boolean ValidDataCheck(){
    boolean lock=true;
        if(lock){
           if(!CheckValid(Login, getLogin(), R.string.error_login_short, false)) {
            lock = false;
           }
        }
        if(lock) {
            if (!CheckValid(Name, getName(), R.string.error_name_short, false)) {
                lock = false;
            }
        }
        if(lock) {
            if (!CheckLastnane(Lastname, getLastname())) {
                lock = false;
            }
        }
        if(lock) {
            if (!CheckValid(Email, getEmail(), R.string.error_email_short, true)) {
                lock = false;
            }
        }
        if(lock) {
            if (!CheckValid(Password, getPassword(), R.string.error_password_short, false)) {
                lock = false;
            }
        }
        if(lock) {
            if (!CheckValid(PasswordSame, getPasswordSame(), R.string.prompt_password2, false)) {
                lock = false;
            }
        }
        if(lock) {
            if (!getPassword().equals(getPasswordSame())) {
                lock = false;
                PasswordsDonotMatch(Password);
            }
        }
    return lock;
    }


    /**
     * Проверяет поле ввода формы для создания пользовательского аккаунта на валидность.
     * @param object EditText
     * @param str текстовая информация поля ввода
     * @param StrErrors текст об ошибки из resources
     * @param LockB false - только для Email
     *
     * @return lock валидные данные = true
     */
    private boolean CheckValid(EditText object, String str, int StrErrors, boolean LockB){
        boolean lock=true;
        if (str.equals("NONE")){
            object.setError(getString(R.string.error_field_required));
            object.requestFocus();
            lock=false;
        }else{
            if(str.length()<3){
                lock=false;
                object.setError(getString(StrErrors));
                object.requestFocus();
            }else{
                if(LockB){
                    if (!str.contains("@") || !str.contains(".")) {
                        lock = false;
                        object.setError(getString(R.string.error_invalid_email));
                        object.requestFocus();
                    }
                }
            }
        }
        return lock;
    }

    /**
     * Проверяет поле ввода фамилии, на количество введенных символов.
     * @param object EditText
     * @param str текст об ошибки из resources
     *
     * @return lock
     */
    private boolean CheckLastnane(EditText object, String str){
        boolean lock=true;
        if (!str.equals("NONE") && str.length()<3){
            lock=false;
            object.setError(getString(R.string.error_lastname_short));
            object.requestFocus();
        }
        return lock;
    }


    /**
     * Выводит сообщение о несовпадении паролей пользователя, при регистрации пользовательского аккаунта.
     * @param object EditText
     */
    private void PasswordsDonotMatch(EditText object){
        object.setError(getString(R.string.error_nomatch_password));
        object.requestFocus();
    }

    /**
     * Записывает логин и пароль пользователя SharedPreferences
     * @param Login
     * @param Password

     */
    private void SharedPreferencesWrite(String Login, String Password, int UserID){
        SharedPreferences ShaLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ShaLogin.edit();
        editor.putString("Login", Login);
        editor.putString("Password", Password);
        editor.putInt("UserID", UserID);
        editor.apply();
    }

    /**
     * Скрывает, или показывает элементы пользовательского интерфейса
     * формы ввода, для создания нового аккаунта. После чего вызывает прогресс-бар.
     * @param show Если true скрывает элементы и вызвать прогресс-бар.
     * @param errors Выводит элементы, и останавливает прогресс-бар если show=false, errors=true.
     *               Останавливает прогресс-бар, если  show=false, errors=false
     * @see CreateAccaunt#Visibility(boolean show)
     * @see CreateAccaunt#Animate(boolean show)
     * @see CreateAccaunt#show(boolean show)
     */
    private  void showProgress(final boolean show, final boolean errors) {
        final int time = 300;
        if (show){
            Animate(show);
            Login.animate().setDuration(time).alpha(1).setListener(new AnimatorListenerAdapter() {
            @Override
                public void onAnimationEnd(Animator animation) {
                    Visibility(show);
                    mProgressView.setVisibility(View.VISIBLE);
                    toolbarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                    toolbar.setLayoutParams(toolbarLayoutParams);
                    new CountDownTimer(time, 100) {
                        public void onTick(long millisUntilFinished) {}
                        public void onFinish() {
                            appbar.setExpanded(false, true);
                        }
                    }.start();
                    Login.animate().setListener(null);
                }
            });
        }else{
              mProgressView.stop();
              if(errors) {
                  new CountDownTimer(time, 100) {
                  public void onTick(long millisUntilFinished) {}
                      public void onFinish() {
                          appbar.setExpanded(true, true);
                      }
                  }.start();

                  mProgressView.animate().setDuration(900).alpha(1).setListener(new AnimatorListenerAdapter() {
                  @Override
                  public void onAnimationEnd(Animator animation) {
                      mProgressView.setVisibility(View.GONE);
                      Visibility(show);
                      Animate(show);
                          new CountDownTimer(time, 100) {
                          public void onTick(long millisUntilFinished) {}
                              public void onFinish() {
                                  SnackBarErrors(getResources().getString(R.string.SnackBareErrorsConnect));
                                  toolbarLayoutParams.setScrollFlags(0);
                                  toolbar.setLayoutParams(toolbarLayoutParams);
                              }
                          }.start();
                     }
                  });
              }
        }
    }



    private enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    /**
     * регистрирует слушатель состояния для AppBarLayout
     * @param appbar AppBarLayout
     */
    private void initViews(AppBarLayout appbar) {
        if(LisnerOn!=null) {
            appbar.removeOnOffsetChangedListener(LisnerOn);
        }
        appbar.addOnOffsetChangedListener(LisnerOn = new AppBarLayout.OnOffsetChangedListener() {
            private State state;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != State.EXPANDED) {
                    }
                    state = State.EXPANDED;
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != State.COLLAPSED) {
                        mProgressView.start();
                    }
                    state = State.COLLAPSED;
                } else {
                    if (state != State.IDLE) {
                    }
                    state = State.IDLE;
                }
            }
        });
    }


    /**
     * Скрывает или возобновляет элементы пользовательского интерфейса GONE or VISIBLE
     * @param show
     */
    private void Visibility(final boolean show){
        Login.setVisibility(show ? View.GONE : View.VISIBLE);
        Name.setVisibility(show ? View.GONE : View.VISIBLE);
        Lastname.setVisibility(show ? View.GONE : View.VISIBLE);
        Email.setVisibility(show ? View.GONE : View.VISIBLE);
        Password.setVisibility(show ? View.GONE : View.VISIBLE);
        PasswordSame.setVisibility(show ? View.GONE : View.VISIBLE);
        Button_Create.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /**
     * Плавно делает прозрачными элементы пользовательского интерфейса.
     * @param show
     */
    private void Animate(final boolean show){
        final int time = 300;
        if(!show) {
            Login.animate().setDuration(time).alpha(show ? 0 : 1);
        }
        Name.animate().setDuration(time).alpha(show ? 0 : 1);
        Lastname.animate().setDuration(time).alpha(show ? 0 : 1);
        Email.animate().setDuration(time).alpha(show ? 0 : 1);
        Password.animate().setDuration(time).alpha(show ? 0 : 1);
        PasswordSame.animate().setDuration(time).alpha(show ? 0 : 1);
        Button_Create.animate().setDuration(time).alpha(show ? 0 : 1);
    }

    /**
     * Резко делает прозрачными элементы пользовательского интерфейса.
     * @param show
     */
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


    /**
     * Вызывает всплывающие сообщение Snackbar
     * @param erros
     *
     */
    private void SnackBarErrors(String erros){
        Snackbar.make(content, erros, Snackbar.LENGTH_LONG).show();
    }


    /**
     * Возвращает логин пользователя, который он ввел в форму  ввода создания нового пользовательского аккаунта.
     * @return login
     *
     */
    public String getLogin(){
        String login = "NONE";
        if(!Login.getText().toString().isEmpty()){
            login = Login.getText().toString().trim();
        }
        return login;
    }
    /**
     * Возвращает имя пользователя, который он ввел в форму  ввода создания нового пользовательского аккаунта.
     * @return name
     *
     */
    public String getName(){
        String name = "NONE";
        if(!Name.getText().toString().isEmpty()){
            name = Name.getText().toString().trim();
        }
        return name;
    }
    /**
     * Возвращает Email пользователя, который он ввел в форму  ввода создания нового пользовательского аккаунта.
     * @return email
     *
     */
    public String getEmail(){
        String email = "NONE";
        if(!Email.getText().toString().isEmpty()){
            email = Email.getText().toString().trim();
        }
        return email;
    }
    /**
     * Возвращает пароль пользователя, который он ввел в форму  ввода создания нового пользовательского аккаунта.
     * @return password
     *
     */
    public String getPassword(){
        String password = "NONE";
        if(!Password.getText().toString().isEmpty()){
            password = Password.getText().toString().trim();
        }
        return password;
    }
    /**
     * Возвращает проверочный пароль пользователя, который он ввел в форму  ввода  создания нового пользовательского аккаунта.
     * @return passwordSame
     *
     */
    public String getPasswordSame(){
        String passwordSame = "NONE";
        if(!PasswordSame.getText().toString().isEmpty()){
            passwordSame = PasswordSame.getText().toString().trim();
        }
        return passwordSame;
    }
    /**
     * Возвращает фамилию пользователя, которую он ввел в форму ввода создания нового пользовательского аккаунта.
     * @return lastname
     *
     */
    public String getLastname(){
        String lastname = "NONE";
        if(!Lastname.getText().toString().isEmpty()){
            lastname = Lastname.getText().toString().trim();
        }
        return lastname;
    }


    /**
     * Возвращает объект mProgressView
     * @return ProgressView
     */
    public ProgressView getProgress(){
        return mProgressView;
    }


    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    public void onBackPressed() {
        Dialog.Builder builder = null;
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog){
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                finish();
                super.onPositiveActionClicked(fragment);
            }
            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {

                super.onNegativeActionClicked(fragment);
            }
        };
        ((SimpleDialog.Builder)builder).message("Exit from draft?")
                .positiveAction("DISCARD")
                .negativeAction("CANCEL");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }





}
