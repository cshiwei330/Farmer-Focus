package sg.edu.np.mad.madassignment1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class moodmsgFragment extends Fragment {


    public moodmsgFragment() {
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
        View view = inflater.inflate(R.layout.fragment_moodmsg, container, false);

        // get button pressed
        String emotion = this.getArguments().getString("key");

        TextView msg = view.findViewById(R.id.moodMsg);

        //displays msg based on mood
        //*will add in more messages and randomize the messages shown so user can see different msgs
        if (emotion.equals("happy")){
            msg.setText("Glad that you are feeling happy. Hope this positivity continues!");
        }
        else if (emotion.equals("neutral")){
            msg.setText("It will come and get you. It's not that you shouldn't be neutral. It's that you won't be able to stay neutral");
        }
        else if (emotion.equals("sad")){
            msg.setText("It's ok to be sad sometimes. It'll get easier with time, trust me :)");
        }
        else{
            msg.setText("You're not alone! It's ok to take a break.");
        }


        {
            return view;
        }
    }
}