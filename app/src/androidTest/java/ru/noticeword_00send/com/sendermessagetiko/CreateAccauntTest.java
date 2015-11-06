package ru.noticeword_00send.com.sendermessagetiko;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import static android.support.test.espresso.Espresso.getIdlingResources;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateAccauntTest {

    @Rule
    public ActivityTestRule<CreateAccaunt> mActivityRule = new ActivityTestRule(CreateAccaunt.class);

    @Test
    public void count_letter_login() {
        onView(withId(R.id.login)).perform(typeText("ri"));
        onView(withId(R.id.name)).perform(typeText("riko"));
        onView(withId(R.id.lastname)).perform(typeText("vironi"));
        onView(withId(R.id.email2)).perform(typeText("wind@mail.ru"));
        onView(withId(R.id.password)).perform(typeText("9999999"));
        onView(withId(R.id.password2)).perform(typeText("9999999"), closeSoftKeyboard());
        onView(withId(R.id.email_create_acc_button2)).perform(click());
        onView(withId(R.id.login)).check(matches(hasFocus()));
    }


    @Test
    public void count_letter_name() {
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        SystemClock.sleep(500);

        onView(withId(R.id.login)).perform(typeText("Sririko"));
        onView(withId(R.id.name)).perform(typeText("ri"));
        onView(withId(R.id.lastname)).perform(typeText("vironi"));
        onView(withId(R.id.email2)).perform(typeText("wind@mail.ru"));
        onView(withId(R.id.password)).perform(typeText("9999999"));
        onView(withId(R.id.password2)).perform(typeText("999999"), closeSoftKeyboard());
        onView(withId(R.id.email_create_acc_button2)).perform(click());
        onView(withId(R.id.name)).check(matches(hasFocus()));
    }

    @Test
    public void count_letter_lastname() {
        onView(withId(R.id.login)).perform(typeText("Sririko"));
        onView(withId(R.id.name)).perform(typeText("riko"));
        onView(withId(R.id.lastname)).perform(typeText("vi"));
        onView(withId(R.id.email2)).perform(typeText("wind@mail.ru"));
        onView(withId(R.id.password)).perform(typeText("9999999"));
        onView(withId(R.id.password2)).perform(typeText("999999"), closeSoftKeyboard());
        onView(withId(R.id.email_create_acc_button2)).perform(click());
        onView(withId(R.id.lastname)).check(matches(hasFocus()));

        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SystemClock.sleep(500);
    }

    @Test
    public void count_letter_email() {
        onView(withId(R.id.login)).perform(typeText("Sririko"));
        onView(withId(R.id.name)).perform(typeText("riko"));
        onView(withId(R.id.lastname)).perform(typeText("vironi"));
        onView(withId(R.id.email2)).perform(typeText("wi"));
        onView(withId(R.id.password)).perform(typeText("9999999"));
        onView(withId(R.id.password2)).perform(typeText("999999"), closeSoftKeyboard());
        onView(withId(R.id.email_create_acc_button2)).perform(click());
        onView(withId(R.id.email2)).check(matches(hasFocus()));

    }


    @Test
    public void count_letter_spassword() {
        onView(withId(R.id.login)).perform(typeText("Sririko"));
        onView(withId(R.id.name)).perform(typeText("riko"));
        onView(withId(R.id.lastname)).perform(typeText("vironi"));
        onView(withId(R.id.email2)).perform(typeText("wind@mail.ru"));
        onView(withId(R.id.password)).perform(typeText("99"));
        onView(withId(R.id.password2)).perform(typeText("999999"), closeSoftKeyboard());
        onView(withId(R.id.email_create_acc_button2)).perform(click());
        onView(withId(R.id.password)).check(matches(hasFocus()));

    }

    @Test
    public void count_letter_password2() {
        onView(withId(R.id.login)).perform(typeText("Sririko"));
        onView(withId(R.id.name)).perform(typeText("riko"));
        onView(withId(R.id.lastname)).perform(typeText("vironi"));
        onView(withId(R.id.email2)).perform(typeText("wind@mail.ru"));
        onView(withId(R.id.password)).perform(typeText("9999999"));
        onView(withId(R.id.password2)).perform(typeText("99"), closeSoftKeyboard());
        onView(withId(R.id.email_create_acc_button2)).perform(click());
        onView(withId(R.id.password2)).check(matches(hasFocus()));
    }



    //E-Mail
    //=================================================================================
    @Test
    public void check_valid_email_1() {
        onView(withId(R.id.login)).perform(typeText("Sririko"));
        onView(withId(R.id.name)).perform(typeText("riko"));
        onView(withId(R.id.lastname)).perform(typeText("vironi"));
        onView(withId(R.id.email2)).perform(typeText("windmail.ru"));
        onView(withId(R.id.password)).perform(typeText("9999999"));
        onView(withId(R.id.password2)).perform(typeText("999999"), closeSoftKeyboard());
        onView(withId(R.id.email_create_acc_button2)).perform(click());
        onView(withId(R.id.email2)).check(matches(hasFocus()));

    }

    @Test
    public void check_valid_email_2() {
        onView(withId(R.id.login)).perform(typeText("Sririko"));
        onView(withId(R.id.name)).perform(typeText("riko"));
        onView(withId(R.id.lastname)).perform(typeText("vironi"));
        onView(withId(R.id.email2)).perform(typeText("wind@mailru"));
        onView(withId(R.id.password)).perform(typeText("9999999"));
        onView(withId(R.id.password2)).perform(typeText("999999"), closeSoftKeyboard());
        onView(withId(R.id.email_create_acc_button2)).perform(click());
        onView(withId(R.id.email2)).check(matches(hasFocus()));

    }

    //password
    //=================================================================================
    @Test
    public void check_valid_password_1() {
        onView(withId(R.id.login)).perform(typeText("Sririko"));
        onView(withId(R.id.name)).perform(typeText("riko"));
        onView(withId(R.id.lastname)).perform(typeText("vironi"));
        onView(withId(R.id.email2)).perform(typeText("wind@mail.ru"));
        onView(withId(R.id.password)).perform(typeText("999988"));
        onView(withId(R.id.password2)).perform(typeText("999999"), closeSoftKeyboard());
        onView(withId(R.id.email_create_acc_button2)).perform(click());
        onView(withId(R.id.password)).check(matches(hasFocus()));

    }

    @Test
    public void check_valid_password_2() {
        onView(withId(R.id.login)).perform(typeText("Sririko"));
        onView(withId(R.id.name)).perform(typeText("riko"));
        onView(withId(R.id.lastname)).perform(typeText("vironi"));
        onView(withId(R.id.email2)).perform(typeText("wind@mail.ru"));
        onView(withId(R.id.password)).perform(typeText("999999"));
        onView(withId(R.id.password2)).perform(typeText("999456"), closeSoftKeyboard());
        onView(withId(R.id.email_create_acc_button2)).perform(click());
        onView(withId(R.id.password)).check(matches(hasFocus()));

    }



    //Empty check
    //=================================================================================
    @Test
    public void Empty_login() {
        onView(withId(R.id.email_create_acc_button2)).perform(click(), closeSoftKeyboard());
        onView(withId(R.id.login)).check(matches(hasFocus()));
    }


    @Test
    public void Empty_name() {
        onView(withId(R.id.login)).perform(typeText("riko"), closeSoftKeyboard());
        onView(withId(R.id.email_create_acc_button2)).perform(click());
        onView(withId(R.id.name)).check(matches(hasFocus()));

    }

    @Test
    public void Empty_email() {
        onView(withId(R.id.login)).perform(typeText("riko"));
        onView(withId(R.id.name)).perform(typeText("riko"), closeSoftKeyboard());
        onView(withId(R.id.email_create_acc_button2)).perform(click());
        onView(withId(R.id.email2)).check(matches(hasFocus()));

    }

    @Test
    public void Empty_email2() {
        onView(withId(R.id.login)).perform(typeText("riko"));
        onView(withId(R.id.name)).perform(typeText("riko"), closeSoftKeyboard());
        onView(withId(R.id.lastname)).perform(typeText("vironi"));
        onView(withId(R.id.email_create_acc_button2)).perform(click());
        onView(withId(R.id.email2)).check(matches(hasFocus()));

    }


    @Test
    public void Empty_spassword() {
        onView(withId(R.id.login)).perform(typeText("riko"));
        onView(withId(R.id.name)).perform(typeText("riko"));
        onView(withId(R.id.lastname)).perform(typeText("vironi"));
        onView(withId(R.id.email2)).perform(typeText("wind@mail.ru"), closeSoftKeyboard());
        onView(withId(R.id.email_create_acc_button2)).perform(click());
        onView(withId(R.id.password)).check(matches(hasFocus()));

    }

    @Test
    public void Empty_password2() {

        onView(withId(R.id.login)).perform(typeText("riko"));
        onView(withId(R.id.name)).perform(typeText("riko"));
        onView(withId(R.id.lastname)).perform(typeText("vironi"));
        onView(withId(R.id.email2)).perform(typeText("wind@mail.ru"));
        onView(withId(R.id.password)).perform(typeText("999999"), closeSoftKeyboard());
        onView(withId(R.id.email_create_acc_button2)).perform(click());
        onView(withId(R.id.password2)).check(matches(hasFocus()));

    }




    //=================================================================================
    //Test on errors connection
    //=================================================================================

    @Test
    public void Check_connection() {
        WifiManager wifiManager = (WifiManager) mActivityRule.getActivity().getSystemService(Context.WIFI_SERVICE);
             wifiManager.setWifiEnabled(false);
             SystemClock.sleep(1000);

             onView(withId(R.id.login)).perform(typeText("RikoLogin"));
             onView(withId(R.id.name)).perform(typeText("riko"));
             onView(withId(R.id.lastname)).perform(typeText("vironi"));
             onView(withId(R.id.email2)).perform(typeText("wind@mail.ru"));
             onView(withId(R.id.password)).perform(typeText("999999wW"));
             onView(withId(R.id.password2)).perform(typeText("999999wW"), closeSoftKeyboard());
             onView(withId(R.id.email_create_acc_button2)).perform(click());

             onView(withId(R.id.login)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
             onView(withId(R.id.name)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
             onView(withId(R.id.lastname)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
             onView(withId(R.id.email2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
             onView(withId(R.id.password)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
             onView(withId(R.id.password2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
             onView(withId(R.id.email_create_acc_button2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

             SystemClock.sleep(10000);
             wifiManager.setWifiEnabled(true);

             onView(withId(R.id.login)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
             onView(withId(R.id.name)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
             onView(withId(R.id.lastname)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
             onView(withId(R.id.email2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
             onView(withId(R.id.password)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
             onView(withId(R.id.password2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
             onView(withId(R.id.email_create_acc_button2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

             onView(withId(R.id.login)).check(matches(isDisplayed()));


    }




    @Test
    public void Check_connection_orientation() {
        WifiManager wifiManager = (WifiManager) mActivityRule.getActivity().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
        SystemClock.sleep(1000);

        onView(withId(R.id.login)).perform(typeText("RikoLogin"));
        onView(withId(R.id.name)).perform(typeText("riko"));
        onView(withId(R.id.lastname)).perform(typeText("vironi"));
        onView(withId(R.id.email2)).perform(typeText("wind@mail.ru"));
        onView(withId(R.id.password)).perform(typeText("999999wW"));
        onView(withId(R.id.password2)).perform(typeText("999999wW"), closeSoftKeyboard());
        onView(withId(R.id.email_create_acc_button2)).perform(click());

        onView(withId(R.id.login)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.name)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.lastname)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.email2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.password)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.password2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.email_create_acc_button2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        SystemClock.sleep(1000);
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SystemClock.sleep(1000);
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        SystemClock.sleep(1000);
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SystemClock.sleep(1000);

        SystemClock.sleep(5000);
        wifiManager.setWifiEnabled(true);

        onView(withId(R.id.login)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.name)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.lastname)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.email2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.password)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.password2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.email_create_acc_button2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));


        onView(withId(R.id.login)).check(matches(isDisplayed()));
    }










}