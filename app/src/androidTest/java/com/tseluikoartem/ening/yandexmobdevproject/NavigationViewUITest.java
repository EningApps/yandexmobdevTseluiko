package com.tseluikoartem.ening.yandexmobdevproject;


import android.content.ComponentName;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import launcher.MainLauncherActivity;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.view.Gravity;

import com.tseluikoartem.ening.yandexmobdevproject.activities.DevProfileActivity;
import com.tseluikoartem.ening.yandexmobdevproject.activities.SettingActivity;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class NavigationViewUITest {

    @Rule
    public ActivityTestRule<MainLauncherActivity> mActivityRule = new ActivityTestRule<>(MainLauncherActivity.class);

    @Before
    public void init(){
        Intents.init();
    }

    @Test
    public void toSettingsTest() {
        onView(withId(R.id.drawer_layout_launcher_activity))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings));
        intended(hasComponent(new ComponentName(getTargetContext(), SettingActivity.class)));
    }

    @Test
    public void toGridFragmentTest() {
        onView(withId(R.id.drawer_layout_launcher_activity))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_launcher));
        onView(withId(R.id.fragment_grid_recycler_view)).perform(RecyclerViewActions.scrollTo(withChild(withText("Tseluiko"))));
    }

    @Test
    public void toLinearFragmentTest() {
        onView(withId(R.id.drawer_layout_launcher_activity))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_list));
        onView(withId(R.id.fragment_list_recycler_view)).perform(RecyclerViewActions.scrollTo(withChild(withText("Tseluiko"))));
    }

    @Test
    public void toDesktopFragmentTest() {
        onView(withId(R.id.drawer_layout_launcher_activity))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_desktop));
        onView(withId(R.id.desktop_recycler));
    }

    @Test
    public void toProfileActivityTest() {
        onView(withId(R.id.drawer_layout_launcher_activity))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.imageViewHeader)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), DevProfileActivity.class)));

    }

    @After
    public void release(){
        Intents.release();
    }

}
