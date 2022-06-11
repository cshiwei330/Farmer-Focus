package sg.edu.np.mad.madassignment1.ui.calendar;

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

public class CalendarFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calender,container,false);

        return view;
    }
}
