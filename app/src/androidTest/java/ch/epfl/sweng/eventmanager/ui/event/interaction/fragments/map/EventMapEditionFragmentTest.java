package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.map;

import android.app.Activity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collection;

import javax.inject.Inject;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.test.users.DummyInMemorySession;
import ch.epfl.sweng.eventmanager.ui.CustomViews.CustomAddOptionsDialog;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.users.Session;
import ch.epfl.sweng.eventmanager.test.repository.MockMapEditionRepository;


import static android.os.SystemClock.sleep;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.runner.lifecycle.Stage.RESUMED;

public class EventMapEditionFragmentTest {

    private Activity currentActivity;

    @Rule
    public final EventTestRule<EventShowcaseActivity> mActivityRule =
            new EventTestRule<>(EventShowcaseActivity.class, 1);

    @Inject
    Session session;

    @Before
    public void setup() {

        TestApplication.component.inject(this);
        session.login(DummyInMemorySession.DUMMY_EMAIL, DummyInMemorySession.DUMMY_PASSWORD,
                getActivityInstance(), task -> {});
        pressBack();

        sleep(100);

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        sleep(100);

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_map));

        onView(withId(R.id.menu_showcase_activity_map_edition_edit)).check(matches(isClickable())).perform(click());
    }

    @Test
    public void executeCustomAddOptionsDialog() {
        onView(withId(R.id.menu_showcase_activity_map_edition_help)).check(matches(isClickable()))
                .perform(click());

        onView(withText(R.string.map_edition_info_text)).check(matches(isDisplayed()));

        onView(withText(R.string.ok)).check(matches(isClickable())).perform(click());

        onView(withId(R.id.menu_showcase_activity_map_edition_save)).check(matches(isClickable())).perform(click());

        onView(withText(R.string.button_no)).check(matches(isClickable())).perform(click());

        /*Intents.intended(Matchers.allOf(IntentMatchers.hasComponent(EventMapEditionFragment.class.getName()), IntentMatchers.hasExtraWithKey(EventPickingActivity.SELECTED_EVENT_ID)));*/

        summonAddOptionsDialog();

    }

    private void summonAddOptionsDialog() {
        DialogFragment fragment = new CustomAddOptionsDialog();

        String TAG = "TEST_TAG";

        ((FragmentActivity)getActivityInstance()).getSupportFragmentManager()
                .beginTransaction().add(fragment, TAG).commit();
    }

    private Activity getActivityInstance(){
        getInstrumentation().runOnMainSync(() -> {
            Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
            if (resumedActivities.iterator().hasNext()){
                currentActivity = (Activity) resumedActivities.iterator().next();
            }
        });

        return currentActivity;
    }

}