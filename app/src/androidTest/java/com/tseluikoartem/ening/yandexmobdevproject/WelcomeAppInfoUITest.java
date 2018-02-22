package com.tseluikoartem.ening.yandexmobdevproject;

import android.content.ComponentName;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import static android.support.test.espresso.intent.Intents.intended;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import launcher.MainLauncherActivity;
import welcomepage.AppWelcomeInfoActivity;
import welcomepage.WelcomeSettingsActivity;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class WelcomeAppInfoUITest {

    @Rule
    public ActivityTestRule<AppWelcomeInfoActivity> mActivityRule = new ActivityTestRule<>(AppWelcomeInfoActivity.class);

    @Before
    public void init(){
        Intents.init();
    }

    @Test
    public void settingsButtonTest() {
        onView(withId(R.id.buttonToSettings)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), WelcomeSettingsActivity.class)));
    }

    @Test
    public void toAppButtonTest() {
        onView(withId(R.id.buttonToApp)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), MainLauncherActivity.class)));
    }

    @After
    public void release(){
        Intents.release();
    }

}
