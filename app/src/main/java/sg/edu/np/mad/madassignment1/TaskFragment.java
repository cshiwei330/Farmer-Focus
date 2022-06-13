package sg.edu.np.mad.madassignment1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {
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

    public TaskFragment() {
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
    public static TaskFragment newInstance(String param1, String param2) {
    TaskFragment fragment = new TaskFragment();
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

        View view = inflater.inflate(R.layout.fragment_task, container, false);
        DBHandler dbHandler = new DBHandler(getActivity(), null, null,6);

        taskList = dbHandler.getTaskData();

        TextView totalTask = view.findViewById(R.id.totalTasks);
        final String[] totalTaskText = {"Total: " + String.valueOf(taskList.size())};
        totalTask.setText(totalTaskText[0]);

        Log.v(TAG, "First TAG Size: " + String.valueOf(taskList.size()));

        Bundle bundle = getArguments();

        if (bundle != null){
            String newTaskName = bundle.getString("task name");
            String newTaskDesc = bundle.getString("task desc");

            // 0 means false = not completed, 1 means true = completed
            int status = 0;
            int id = taskList.size() + 1;

            Task newTaskDB = new Task();
            newTaskDB.setId(id);
            newTaskDB.setStatus(status);
            newTaskDB.setTaskName(newTaskName);
            newTaskDB.setTaskDesc(newTaskDesc);
            dbHandler.addTask(newTaskDB);
            Log.v(TAG, "Added to DB");
            taskList = dbHandler.getTaskData();
            totalTaskText[0] = "Total: " + String.valueOf(taskList.size());
            totalTask.setText(totalTaskText[0]);
        }

        if (taskList.size() == 0){
            Toast.makeText(getActivity(), "No Tasks", Toast.LENGTH_LONG).show();
        }

        RecyclerView recyclerView = view.findViewById(R.id.toDoListRecycleView);
        MyAdaptor mAdaptor = new MyAdaptor(taskList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdaptor);

        Button clearAllTaskButton = view.findViewById(R.id.clearAllTaskButton);
        clearAllTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler.deleteAllTask();
                taskList = dbHandler.getTaskData();
                mAdaptor.clear();
                // make it a one element array
                // If not, problem: Cannot assign a value to final variable 'totalTaskText'
                totalTaskText[0] = "Total: " + String.valueOf(taskList.size());
                totalTask.setText(totalTaskText[0]);
                Toast.makeText(getActivity(), "Tasks Cleared", Toast.LENGTH_LONG).show();
            }
        });


        FloatingActionButton addNewTask = view.findViewById(R.id.addNewTaskButton);
        addNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //define fragment transaction
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                // set fragment to tasks fragment
                ft.replace(R.id.nav_host_fragment_content_main, addTaskFragment);
                Log.v(TAG, "Replaced fragment");
                ft.show(addTaskFragment);
                Log.v(TAG, "Fragment shown");
                ft.commit();
                Log.v(TAG, "Fragment commited");
        }
        });



        // Inflate the layout for this fragment
        return view;
    }

}
