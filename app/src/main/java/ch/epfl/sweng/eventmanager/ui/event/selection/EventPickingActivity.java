package ch.epfl.sweng.eventmanager.ui.event.selection;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.user.DisplayAccountActivity;
import ch.epfl.sweng.eventmanager.ui.user.LoginActivity;
import ch.epfl.sweng.eventmanager.users.Session;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import dagger.android.AndroidInjection;

import javax.inject.Inject;
import java.util.Collections;

public class EventPickingActivity extends AppCompatActivity {
    public static final String SELECTED_EVENT_ID = "ch.epfl.sweng.SELECTED_EVENT_ID";
    @Inject
    ViewModelFactory factory;
    @BindView(R.id.joined_help_text)
    TextView joinedHelpText;
    @BindView(R.id.not_joined_help_text)
    TextView notJoinedHelpText;
    @BindView(R.id.help_text)
    TextView helpText;
    @BindView(R.id.joined_events_list)
    RecyclerView joinedEvents;
    @BindView(R.id.not_joined_event_list)
    RecyclerView eventList;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.event_linear_layout)
    LinearLayout content;
    private Boolean doubleBackToExitPressedOnce = false;
    private EventPickingModel model;

    private void setupRecyclerView(RecyclerView view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        view.setLayoutManager(layoutManager);
        // Set an empty list adapter
        view.setAdapter(new EventListAdapter(Collections.emptyList()));
    }

    private void setUIVisibility(int joinedEvent, int eventList, int joinedHelpText,
                               int notJoinedHelpText) {
        this.joinedEvents.setVisibility(joinedEvent);
        this.eventList.setVisibility(eventList);
        this.joinedHelpText.setVisibility(joinedHelpText);
        this.notJoinedHelpText.setVisibility(notJoinedHelpText);
    }

    private void setupObservers() {
        this.model.getEventsPair().observe(this, list -> {
            if (list == null) {
                return;
            }
            eventList.setAdapter(new EventListAdapter(list.getOtherEvents()));
            joinedEvents.setAdapter(new EventListAdapter(list.getJoinedEvents()));

            //once data is loaded
            helpText.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);

            if (!list.getJoinedEvents().isEmpty()) {
                if (list.getOtherEvents().isEmpty()) {
                    setUIVisibility(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE);
                } else {
                    setUIVisibility(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE);
                }
            } else {
                setUIVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_event_picking);
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        this.model = ViewModelProviders.of(this, factory).get(EventPickingModel.class);
        this.model.init();
        ButterKnife.bind(this);
        content.setVisibility(View.GONE);
        Toolbar toolbar = findViewById(R.id.event_picking_toolbar);
        setSupportActionBar(toolbar);
        setupObservers();

        // Event list
        eventList.setHasFixedSize(true);
        LinearLayoutManager eventListLayoutManager = new LinearLayoutManager(this);
        eventList.setLayoutManager(eventListLayoutManager);
        notJoinedHelpText.setVisibility(View.GONE);

        // Event lists
        setupRecyclerView(eventList);
        setupRecyclerView(joinedEvents);
    }

    private void openLoginOrAccountActivity() {
        Class nextActivity;
        if (Session.isLoggedIn()) {
            nextActivity = DisplayAccountActivity.class;
        } else {
            nextActivity = LoginActivity.class;
        }
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_picking, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.login_button:
                openLoginOrAccountActivity();
                break;

            case R.id.logout_button:
                Session.logout();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (Session.isLoggedIn()) {
            menu.findItem(R.id.login_button).setTitle(R.string.account_button);
            menu.findItem(R.id.logout_button).setVisible(true);
        } else {
            menu.findItem(R.id.login_button).setTitle(R.string.login_button);
            menu.findItem(R.id.logout_button).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast toast = Toast.makeText(this, R.string.double_back_press_to_exit, Toast.LENGTH_SHORT);
        ((TextView) (toast.getView().findViewById(android.R.id.message))).setTextColor(ContextCompat.getColor(this,
                R.color.colorSecondary));
        toast.getView().getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                PorterDuff.Mode.SRC_IN);
        toast.show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}