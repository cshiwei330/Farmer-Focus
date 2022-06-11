package sg.edu.np.mad.madassignment1;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimerFragment extends Fragment {
    private String TAG = "Timer Fragment";
    ArrayList<Task> taskList = new ArrayList<>();
    Fragment addTaskFragment = new AddNewTaskFragment();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TimerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimerFragment newInstance(String param1, String param2) {
    TimerFragment fragment = new TimerFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        if (taskList.size() == 0){
//            AddTasksExamples();
            Toast.makeText(getContext(), "No Tasks", Toast.LENGTH_SHORT).show();
        }

        Bundle bundle = getArguments();

        if (bundle != null){
            String newTaskName = bundle.getString("task name");
            String newTaskDesc = bundle.getString("task desc");
            AddTasks(newTaskName, newTaskDesc);
        }

        RecyclerView recyclerView = view.findViewById(R.id.toDoListRecycleView);
        MyAdaptor mAdaptor = new MyAdaptor(taskList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdaptor);

        TextView totalTask = view.findViewById(R.id.totalTasks);
        String totalTaskText = "Total: " + String.valueOf(taskList.size());
        totalTask.setText(totalTaskText);
        Log.v(TAG, "Total No. of Tasks Set");

        FloatingActionButton addNewTask = view.findViewById(R.id.addNewTaskButton);
        addNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //define fragment transaction
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                // set fragment to tasks fragment
                ft.replace(R.id.fragmentLayout, addTaskFragment);
                ft.show(addTaskFragment);
                ft.commit();
            }
        });



        // Inflate the layout for this fragment
        return view;
    }

    private void AddTasks(String newTaskName, String newTaskDesc){
        boolean status = false;
        int id6 = taskList.size() + 1;
        Task taskSix = new Task(id6, status, newTaskName, newTaskDesc);
        taskList.add(taskSix);
    }
}
/*

*/