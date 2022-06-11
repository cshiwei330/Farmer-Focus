package sg.edu.np.mad.madassignment1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewTaskFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddNewTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewTaskFragment newInstance(String param1, String param2) {
        AddNewTaskFragment fragment = new AddNewTaskFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_new_task, container, false);

        EditText newTaskName = view.findViewById(R.id.newTaskName);
        EditText newTaskDesc = view.findViewById(R.id.newTaskDesc);
        Button createNewTaskButton = view.findViewById(R.id.createNewTaskButton);

        createNewTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newTaskNameString = newTaskName.getText().toString();
                String newTaskDescString = newTaskDesc.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("task name", newTaskNameString);
                bundle.putString("task desc", newTaskDescString);

                FragmentTransaction fT = getActivity().getSupportFragmentManager().beginTransaction();

                TimerFragment timerFragment = new TimerFragment();

                timerFragment.setArguments(bundle);

                fT.replace(R.id.fragmentLayout, timerFragment);
                fT.show(timerFragment);
                fT.commit();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}