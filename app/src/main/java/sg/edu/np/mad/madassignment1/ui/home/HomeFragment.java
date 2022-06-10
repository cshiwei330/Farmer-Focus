package sg.edu.np.mad.madassignment1.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import sg.edu.np.mad.madassignment1.HomePage;
import sg.edu.np.mad.madassignment1.LoginPage;
import sg.edu.np.mad.madassignment1.R;
import sg.edu.np.mad.madassignment1.TimerFragment;
import sg.edu.np.mad.madassignment1.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container,false);

        Button test = view.findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent testlogin = new Intent(getActivity(), LoginPage.class);
                startActivity(testlogin);
            }
        });

        ImageView tasksIcon = view.findViewById(R.id.tasksIcon);
        TextView tasksTexView = view.findViewById(R.id.tasksTextView);
        Fragment taskFragment = new TimerFragment();

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

        tasksTexView.setOnClickListener(new View.OnClickListener() {
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