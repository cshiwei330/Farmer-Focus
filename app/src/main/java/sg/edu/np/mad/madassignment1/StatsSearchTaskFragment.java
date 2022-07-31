/*
SEARCH TASK FRAGMENT
This fragment prompts the user to check out the data recorded for their recurring tasks. When the "CLICK HERE!" button is clicked, the user
go to the recurring task statistics page.
 */

package sg.edu.np.mad.madassignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StatsSearchTaskFragment extends Fragment {
    Context thisContext;
    public String GLOBAL_PREF = "MyPrefs";

    public StatsSearchTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats_search_task, container, false);
        thisContext = container.getContext();

        // button to go to the recurring task statistics page
        Button recurringTask = view.findViewById(R.id.toRecurringTaskPage);
        recurringTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StatsRecurringTaskActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}