package ru.noticeword_00send.com.sendermessagetiko;




import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import com.rey.material.app.Dialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.app.ThemeManager;
import com.rey.material.widget.ProgressView;

import org.json.JSONObject;

import API.API;
import Model.CreateLoginJson;
import loaders.CustomJsonObject;
import loaders.HttpVolley;



public class LoginActivity extends AppCompatActivity {


    private CoordinatorLayout content;
    private EditText login;
    private EditText password;
    private Button login_button;
    private TextView createAccount_textView;
    private TextView label_textView;
    private ProgressView mProgressView;
    private Toolbar toolbar;
    private AppBarLayout.LayoutParams toolbarLayoutParams;
    private AppBarLayout appbar;
    private ImageView imageView;


    private Boolean lockA=false;
    private String responseA="";
    private static int getTimeA=7;
    private static CountDownTimer timer;
    private AppBarLayout.OnOffsetChangedListener LisnerOn = null;
    private API url;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.init(this, 1, 0, null);
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.loginx);
        password = (EditText) findViewById(R.id.passwordx);
        login_button = (Button) findViewById(R.id.login_button);
        createAccount_textView = (TextView) findViewById(R.id.createAccount_textView);
        label_textView = (TextView) findViewById(R.id.label_textView);
        mProgressView = (ProgressView) findViewById(R.id.login_progress);
        content = (CoordinatorLayout) findViewById(R.id.contentLogin);
        toolbar = (Toolbar) findViewById(R.id.toolbarlogin);
        appbar = (AppBarLayout) findViewById(R.id.barlogin);
        imageView = (ImageView) findViewById(R.id.imageView);





        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        createAccount_textView.setTypeface(Typeface.createFromAsset(getAssets(), "font/Niconne-Regular.ttf"));
        label_textView.setTypeface(Typeface.createFromAsset(getAssets(), "font/Bellico.ttf"));
        toolbarLayoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        setlistnerAppBarLayout(appbar);
        url = new API();




        createAccount_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent create_ac = new Intent(LoginActivity.this, CreateAccaunt.class);
                createAccount_textView.setEnabled(false);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createAccount_textView.setEnabled(true);
                        startActivity(create_ac);
                        overridePendingTransition(R.anim.slide, R.anim.anim2);
                        finish();
                    }
                }, 250);
            }
        });


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockA = false;
                responseA = "";
                getTimeA = 7;
                if (ValidDataCheck()) {
                    login_button.setEnabled(false);
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
        CustomJsonObject jsObjRequest = new CustomJsonObject(Request.Method.POST, url.getUrlSingIn(), JsonToStringCA() , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                CreatePause(true, response.toString(), 7000);
                Log.i("111","ttttt - "+response.toString());
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
            //Точка входа в основной модуль

        }else{
            SnackBarErrors(getResources().getString(R.string.ErrorIncorrectPassAndLogin));
            showProgress(false, true);
        }
        login_button.setEnabled(true);
    }


    /**
     * Выводит сообщение об ошибки сети
     * @see CreateAccaunt#ResponseOK(String response)
     */
    private void ResponseError(){
        SnackBarErrors(getResources().getString(R.string.SnackBareErrorsConnect));
        showProgress(false, true);
        login_button.setEnabled(true);
    }


    /**
     * Преобразовывает данные для создания аккаунта, в формат JSON
     * @return json
     *
     *  @see CreateAccaunt#getLogin()
     *  @see CreateAccaunt#getPassword()
     */
    @NonNull
    private String JsonToStringCA() {
        CreateLoginJson jsonTo = new CreateLoginJson();
        jsonTo.Login = getLogin();
        jsonTo.Password = getPassword();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(jsonTo).toString();
    }




    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("Login", login.getVisibility());

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
   * Проверяет все поля формы ввода создания пользовательского аккаунта, на валидность
   *
   * @return lock валидные данные = true
   */
    private boolean ValidDataCheck(){
        boolean lock=true;
        if(lock){
            if(!CheckValid(login, getLogin(), R.string.error_login_short)) {
                lock = false;
            }
        }
        if(lock) {
            if (!CheckValid(password, getPassword(), R.string.error_password_short)) {
                lock = false;
            }
        }
        return lock;
    }


    /**
     * Проверяет поле ввода формы для создания пользовательского аккаунта на валидность.
     * @param object EditText
     * @param str текстовая информация поля ввода
     * @param StrErrors текст об ошибки из resources
     *
     * @return lock валидные данные = true
     */
    private boolean CheckValid(EditText object, String str, int StrErrors){
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
            }
        }
        return lock;
    }



    /**
     * Возвращает логин пользователя, который он ввел в форму  ввода создания нового пользовательского аккаунта.
     * @return login
     *
     */
    public String getLogin(){
        String loginm = "NONE";
        if(!login.getText().toString().isEmpty()){
            loginm = login.getText().toString().trim();
        }
        return loginm;
    }

    /**
     * Возвращает пароль пользователя, который он ввел в форму  ввода создания нового пользовательского аккаунта.
     * @return password
     *
     */
    public String getPassword(){
        String passwordm = "NONE";
        if(!password.getText().toString().isEmpty()){
            passwordm = password.getText().toString().trim();
        }
        return passwordm;
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
            login.animate().setDuration(time).alpha(1).setListener(new AnimatorListenerAdapter() {
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
                    login.animate().setListener(null);
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
                                //SnackBarErrors(getResources().getString(R.string.SnackBareErrorsConnect));
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
    private void setlistnerAppBarLayout(AppBarLayout appbar) {
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
        login.setVisibility(show ? View.GONE : View.VISIBLE);
        password.setVisibility(show ? View.GONE : View.VISIBLE);
        createAccount_textView.setVisibility(show ? View.GONE : View.VISIBLE);
        label_textView.setVisibility(show ? View.GONE : View.VISIBLE);
        imageView.setVisibility(show ? View.GONE : View.VISIBLE);
        login_button.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /**
     * Плавно делает прозрачными элементы пользовательского интерфейса.
     * @param show
     */
    private void Animate(final boolean show){
        final int time = 300;
        if(!show) {
            login.animate().setDuration(time).alpha(show ? 0 : 1);
        }
        createAccount_textView.animate().setDuration(time).alpha(show ? 0 : 1);
        label_textView.animate().setDuration(time).alpha(show ? 0 : 1);
        password.animate().setDuration(time).alpha(show ? 0 : 1);
        imageView.animate().setDuration(time).alpha(show ? 0 : 1);
        login_button.animate().setDuration(time).alpha(show ? 0 : 1);
    }

    /**
     * Резко делает прозрачными элементы пользовательского интерфейса.
     * @param show
     */
    private void show(boolean show){
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        Visibility(show);
        login.animate().alpha(show ? 0 : 1);
        password.animate().alpha(show ? 0 : 1);
        createAccount_textView.animate().alpha(show ? 0 : 1);
        label_textView.animate().alpha(show ? 0 : 1);
        imageView.animate().alpha(show ? 0 : 1);
        login_button.animate().alpha(show ? 0 : 1);
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













    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    public void onBackPressed() {
    Builder builder = null;
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

