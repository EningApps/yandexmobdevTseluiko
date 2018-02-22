package com.tseluikoartem.ening.yandexmobdevproject;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import launcher.MainLauncherActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.view.Gravity;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecycleViewsUITest {


    @Rule
    public ActivityTestRule<MainLauncherActivity> mActivityRule = new ActivityTestRule<>(MainLauncherActivity.class);

    @Test
    public void chooseAppRecyclerViewTest() {
        onView(withId(R.id.fab_launcher)).perform(click());
        onView(withId(R.id.app_choose_recycler_view)).perform(RecyclerViewActions.scrollTo(withChild(withText("Tseluiko"))));
    }

    @Test
    public void mainGridRRecyclerViewTest() {
        onView(withId(R.id.fragment_grid_recycler_view)).perform(RecyclerViewActions.scrollTo(withChild(withText("Tseluiko"))));
    }

    @Test
    public void mainLinearRecyclerViewTest() {
        onView(withId(R.id.drawer_layout_launcher_activity))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_list));
        onView(withId(R.id.fragment_list_recycler_view)).perform(RecyclerViewActions.scrollTo(withChild(withText("Tseluiko"))));

    }

}
