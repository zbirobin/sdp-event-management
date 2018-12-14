package ch.epfl.sweng.eventmanager.ui.event.interaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.SendNewsFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.user.EventUserManagementFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.EventInteractionModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.NewsViewModel;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.AndroidInjection;

import javax.inject.Inject;

public class EventAdministrationActivity extends MultiFragmentActivity {
    private static final String TAG = "EventAdministration";

    @Inject
    ViewModelFactory factory;

    private EventInteractionModel model;
    private NewsViewModel newsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_administration);
        initializeSharedUI();

        // Fetch event from passed ID
        Intent intent = getIntent();
        eventID = intent.getIntExtra(EventPickingActivity.SELECTED_EVENT_ID, -1);
        if (eventID <= 0) { // Suppose that negative or null event ID are invalids
            Log.e(TAG, "Got invalid event ID#" + eventID + ".");
        } else {
            this.model = ViewModelProviders.of(this, factory).get(EventInteractionModel.class);
            this.model.init(eventID);

            // Only display admin button if the user is at least staff
            model.getEvent().observe(this, ev -> {
                if (ev == null) {
                    Log.e(TAG, "Got null model from parent activity");
                    return;
                }

                // Set window title
                setTitle("Admin: " + ev.getName());
            });

            // Set default administration fragment
            changeFragment(new EventUserManagementFragment(), false);
        }

        // Initialize News model
        this.newsModel = ViewModelProviders.of(this, factory).get(NewsViewModel.class);
        this.newsModel.init(eventID);

        // Handle drawer events
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // set item as selected to persist highlight
        menuItem.setChecked(true);

        // close drawer when item is tapped
        mDrawerLayout.closeDrawers();

        switch(menuItem.getItemId()) {
            case R.id.nav_showcase :
                Intent showcaseIntent = new Intent(this, EventShowcaseActivity.class);
                showcaseIntent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, eventID);
                showcaseIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(showcaseIntent);
                break;

            case R.id.nav_user_management :
                changeFragment(new EventUserManagementFragment(), true);
                break;

            case R.id.nav_send_news :
                changeFragment(new SendNewsFragment(), true);
                break;

            case R.id.nav_edit_event :
                Intent editIntent = new Intent(this, EventCreateActivity.class);
                editIntent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, eventID);
                startActivity(editIntent);
                break;
        }

        return true;
    }
}
