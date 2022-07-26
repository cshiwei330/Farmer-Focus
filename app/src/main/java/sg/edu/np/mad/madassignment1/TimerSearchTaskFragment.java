package sg.edu.np.mad.madassignment1;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TimerSearchTaskFragment extends Fragment {

    Context thisContext;
    public String GLOBAL_PREF = "MyPrefs";

    public TimerSearchTaskFragment() {
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
        View view = inflater.inflate(R.layout.activity_timer_search_task_fragment, container, false);
        thisContext = container.getContext();
        Button selectTask = view.findViewById(R.id.toSelectTaskPage);
        selectTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TimerTaskListActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}