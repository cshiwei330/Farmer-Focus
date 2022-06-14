package sg.edu.np.mad.madassignment1.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import sg.edu.np.mad.madassignment1.R;
import sg.edu.np.mad.madassignment1.TaskFragment;
import sg.edu.np.mad.madassignment1.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container,false);

        ImageView tasksIcon = view.findViewById(R.id.tasksIcon);
        TextView tasksTextView = view.findViewById(R.id.tasksTextView);
        Fragment taskFragment = new TaskFragment();

        tasksIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //define fragment transaction
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                // set fragment to tasks fragment
                ft.add(R.id.fragmentLayout, taskFragment);
                ft.show(taskFragment);
                ft.commit();
            }
        });

        tasksTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //define fragment transaction
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                // set fragment to tasks fragment
                ft.add(R.id.fragmentLayout, taskFragment);
                ft.show(taskFragment);
                ft.commit();
            }
        });

        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}