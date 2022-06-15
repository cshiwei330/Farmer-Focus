package sg.edu.np.mad.madassignment1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewTaskFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
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

        //inflate fragment
        View view = inflater.inflate(R.layout.fragment_add_new_task, container, false);

        //define elements in fragment
        EditText newTaskName = view.findViewById(R.id.newTaskName);
        EditText newTaskDesc = view.findViewById(R.id.newTaskDesc);
        Button createNewTaskButton = view.findViewById(R.id.createNewTaskButton);
        Button dateButton = view.findViewById(R.id.datePickerButton);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
//                //define fragment transaction
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                // set fragment to tasks fragment
//                ft.replace(R.id.nav_host_fragment_content_main, datePicker);
//                ft.show(datePicker);
//                ft.commit();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                datePicker.show(ft, "date picker");

            }
        });

        createNewTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Task Created", Toast.LENGTH_SHORT).show();

                String newTaskNameString = newTaskName.getText().toString();
                String newTaskDescString = newTaskDesc.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("task name", newTaskNameString);
                bundle.putString("task desc", newTaskDescString);

//                FragmentTransaction fT = getActivity().getSupportFragmentManager().beginTransaction();

                //define fragment transaction
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                TaskFragment taskFragment = new TaskFragment();

                taskFragment.setArguments(bundle);

                ft.replace(R.id.nav_host_fragment_content_main, taskFragment);
                ft.show(taskFragment);
                ft.commit();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView textView = view.findViewById(R.id.textView2);
        textView.setText(currentDateString);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getContext(), (DatePickerDialog.OnDateSetListener) getContext(), year, month, day);
    }
}