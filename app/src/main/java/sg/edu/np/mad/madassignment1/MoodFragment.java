package sg.edu.np.mad.madassignment1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MoodFragment extends Fragment {

    public MoodFragment() {
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
        View view = inflater.inflate(R.layout.fragment_mood, container, false);

        //Happy Button
        Button happy = view.findViewById(R.id.happyButton);
        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // To know what message to display
                Bundle b = new Bundle();
                b.putString("key", "happy");
                Fragment message = new moodmsgFragment();
                message.setArguments(b);
                // switch fragments
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.moodContainerView, message);
                fragmentTransaction.commit();
            }
        });

        //Neutral Button
        Button neutral = view.findViewById(R.id.neutralButton);
        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // To know what message to display
                Bundle b = new Bundle();
                b.putString("key", "neutral");
                Fragment message = new moodmsgFragment();
                message.setArguments(b);
                // switch fragments
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.moodContainerView, message);
                fragmentTransaction.commit();
            }
        });

        //Sad Button
        Button sad = view.findViewById(R.id.sadButton);
        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // To know what message to display
                Bundle b = new Bundle();
                b.putString("key", "sad");
                Fragment message = new moodmsgFragment();
                message.setArguments(b);
                // switch fragments
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.moodContainerView, message);
                fragmentTransaction.commit();
            }
        });

        //Stressed Button
        Button stressed = view.findViewById(R.id.stressButton);
        stressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // To know what message to display
                Bundle b = new Bundle();
                b.putString("key", "stressed");
                Fragment message = new moodmsgFragment();
                message.setArguments(b);
                // switch fragments
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.moodContainerView, message);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}