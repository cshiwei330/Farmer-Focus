package sg.edu.np.mad.madassignment1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MoodoftheweekFragment extends Fragment {
    Context thisContext;
    public String GLOBAL_PREF = "MyPrefs";

    //define dbHandler
    DBHandler dbHandler = new DBHandler(getActivity(), null, null,6);

    // shared preferences to get username
    SharedPreferences sharedPreferences = getContext().getSharedPreferences(GLOBAL_PREF, 0);
    String username = sharedPreferences.getString("username", "");
    User user = dbHandler.findUser(username);

    public MoodoftheweekFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisContext = container.getContext();
        sg.edu.np.mad.madassignment1.DBHandler dbHandler = new sg.edu.np.mad.madassignment1.DBHandler(thisContext, null, null, 1);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moodoftheweek, container, false);
        TextView moodStats = view.findViewById(R.id.moodStats);
        ImageView articleImage = view.findViewById(R.id.articleImage);
        TextView article = view.findViewById(R.id.article);
        TextView viewArticle = view.findViewById(R.id.viewArticle);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //set the date formatter
            Date today = sdf.parse(sdf.format(new Date()));

            // getting dates of the past week
            Calendar pastWeek = Calendar.getInstance();
            pastWeek.add(Calendar.DAY_OF_MONTH, -7);

            ArrayList<Mood> moodList = dbHandler.getMoodData(user.getUserID());

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
                    viewArticle.setPaintFlags(viewArticle.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                    if (motw.equals("happy")) {
                        moodStats.setText("In the past 7 days, you have been mostly happy. \nKeep the positivity coming!");
                        articleImage.setVisibility(View.GONE);
                        article.setVisibility(View.GONE);
                        viewArticle.setVisibility(View.GONE);
                    } else if (motw.equals("neutral")) {
                        moodStats.setText("In the past 7 days, you have been mostly neutral. \nSpend more time on your mental health! \nHere's an article to help :)");
                        articleImage.setImageResource(R.drawable.neutral);
                        article.setText("Shifting Away From Neutral \nBrooke Guttenberg");
                        viewArticle.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://albertellis.org/2014/09/shifting-away-neutral/"));
                                startActivity(browserIntent);
                                return false;
                            }
                        });
                    } else if (motw.equals("sad")) {
                        moodStats.setText("In the past 7 days, you have been mostly sad. \nBe kind to yourself. \nHere are some ways to help.");
                        articleImage.setImageResource(R.drawable.sad);
                        article.setText("Why am I sad all the time?");
                        viewArticle.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://au.reachout.com/articles/why-am-i-sad-all-the-time"));
                                startActivity(browserIntent);
                                return false;
                            }
                        });
                    } else {
                        moodStats.setText("In the past 7 days, you have been mostly stressed. \nTake a breather and de-stress. \nHere are some ways do so.");
                        articleImage.setImageResource(R.drawable.stressed);
                        article.setText("15 Simple Ways to Relieve Stress");
                        viewArticle.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.healthline.com/nutrition/16-ways-relieve-stress-anxiety"));
                                startActivity(browserIntent);
                                return false;
                            }
                        });
                    }
                }
            }
            else{
                moodStats.setText("You have yet to enter a mood in the past week!");
                articleImage.setVisibility(View.GONE);
                article.setVisibility(View.GONE);
                viewArticle.setVisibility(View.GONE);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }
}