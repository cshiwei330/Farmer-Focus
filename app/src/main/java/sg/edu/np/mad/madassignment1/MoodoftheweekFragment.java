package sg.edu.np.mad.madassignment1;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MoodoftheweekFragment extends Fragment {
    Context thisContext;

    public MoodoftheweekFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisContext = container.getContext();
        sg.edu.np.mad.madassignment1.DBHandler dbHandler = new sg.edu.np.mad.madassignment1.DBHandler(thisContext, null, null, 1);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moodoftheweek, container, false);
        TextView moodStats = view.findViewById(R.id.moodStats);
        //find out the max no of mood entered the past week
        //set text
        //link to article

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //set the date formatter
            Date today = sdf.parse(sdf.format(new Date()));

            // getting dates of the past week
            Calendar pastWeek = Calendar.getInstance();
            pastWeek.add(Calendar.DAY_OF_MONTH, -7);

            ArrayList<Mood> moodList = dbHandler.getMoodData();

            if(moodList.size() > 0) {
                Date moodDate = sdf.parse(moodList.get(0).getDate());

                ArrayList<String> moodOnlyList = new ArrayList<>();
                for (int i = 0; i <= moodList.size(); i++) {
                        moodOnlyList.add(moodList.get(0).getMood());
                }

                if (moodDate.compareTo(today) == 0 || (moodDate.after(pastWeek.getTime()) && moodDate.before(today))) {
                    int count = 0, maxCount = 0;
                    String motw = "";
                    for (int i = 0; i < moodOnlyList.size(); i++) {
                        count = 1;
                        for (int j = i + 1; j < moodOnlyList.size(); j++) {
                            if (moodOnlyList.get(i).equals(moodOnlyList.get(j))) {
                                count++;
                            }
                        }
                        if (count > maxCount) {
                            maxCount = count;
                            motw = moodOnlyList.get(i);

                        }
                    }
                    if (motw.equals("happy")) {
                        moodStats.setText("In the past week, you have been mostly happy. Keep the positivity coming!");
                    } else if (motw.equals("neutral")) {
                        moodStats.setText("In the past week, you have been mostly neutral. Spend more time caring for your mental health! Here's an article to help :)");
                    } else if (motw.equals("sad")) {
                        moodStats.setText("In the past week, you have been mostly sad. Be kind to yourself. Here are some ways to help. ");
                    } else if (motw.equals("stressed")) {
                        moodStats.setText("In the past week, you have been mostly stressed. Take a breather and destress. Here are some ways do so.");
                    } else {
                        moodStats.setText(motw);
                    }
                }
            }
            else{
                moodStats.setText("You have yet to enter a mood in the past week!");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }
}