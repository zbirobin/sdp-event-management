package ch.epfl.sweng.eventmanager.ui.schedule;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;

import java.util.List;

public abstract class AbstractScheduleActivity extends Fragment {

    private static String TAG = "AbstractScheduleActivity";
    protected ScheduleViewModel model;
    private RecyclerView recyclerView;
    private TimeLineAdapter timeLineAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_schedule, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = ViewModelProviders.of(getActivity()).get(ScheduleViewModel.class);

        this.getScheduledItems().observe(this, concerts -> {
            timeLineAdapter = new TimeLineAdapter(concerts, model);
            recyclerView.setAdapter(timeLineAdapter);

            // TODO: we should display a message when concerts == null to let the user know that this event has no schedule
        });
    }

    protected abstract LiveData<List<ScheduledItem>> getScheduledItems();


}

