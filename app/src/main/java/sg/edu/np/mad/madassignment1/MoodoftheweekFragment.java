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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moodoftheweek, container, false);
        thisContext = container.getContext();

        // defining DBHandler
        sg.edu.np.mad.madassignment1.DBHandler dbHandler = new sg.edu.np.mad.madassignment1.DBHandler(thisContext, null, null, 1);
        // shared preferences to get userid
        SharedPreferences sharedPreferences = thisContext.getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        // calling the text views and image views to set text / image
        TextView moodStats = view.findViewById(R.id.moodStats);
        ImageView articleImage = view.findViewById(R.id.articleImage);
        TextView article = view.findViewById(R.id.article);
        TextView viewArticle = view.findViewById(R.id.viewArticle);

        // a try catch for parse exception
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //set the date formatter
            Date today = sdf.parse(sdf.format(new Date())); //getting today's date

            // getting the dates of the past 7 days
            Calendar pastWeek = Calendar.getInstance();
            pastWeek.add(Calendar.DAY_OF_MONTH, -7);

            // getting the moods of the user and populating the list
            ArrayList<Mood> moodList = dbHandler.getMoodData(user.getUserID());

            // if the user has entered a mood before
            if(moodList.size() > 0) {
                Date moodDate = sdf.parse(moodList.get(0).getDate()); //getting the date of the mood entered

                ArrayList<String> moodOnlyList = new ArrayList<>();
                for (int i = 0; i <= moodList.size(); i++) {
                    // adding only the mood in string format, without the date
                    moodOnlyList.add(moodList.get(0).getMood());
                }

                // if the mood is entered between a week ago and today
                if (moodDate.compareTo(today) == 0 || (moodDate.after(pastWeek.getTime()) && moodDate.before(today))) {
                    //initialising variables to be used
                    int count = 0, maxCount = 0;
                    String motw = "";

                    // counting the mooods entered and getting the most frequent entered mood
                    for (int i = 0; i < moodOnlyList.size(); i++) {
                        count = 1;
                        for (int j = i + 1; j < moodOnlyList.size(); j++) {
                            if (moodOnlyList.get(i).equals(moodOnlyList.get(j))) {
                                count++;
                            }
                        }
                        if (count > maxCount) {
                            maxCount = count;
                            motw = moodOnlyList.get(i); // set as the mood of the week
                        }
                    }
                    //underlining the "View Article Here" text to make it seem more like a hyperlink text
                    viewArticle.setPaintFlags(viewArticle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    // if the mood of the week is happy
                    if (motw.equals("happy")) {
                        // setting the text to display happy mood of the week
                        moodStats.setText("In the past 7 days, you have been mostly happy. \nKeep the positivity coming!");
                        // removing article related texts and image as there is no article recommended for happy people :)
                        articleImage.setVisibility(View.GONE);
                        article.setVisibility(View.GONE);
                        viewArticle.setVisibility(View.GONE);
                    } else if (motw.equals("neutral")) { // if the mood of the week is neutral
                        // setting the text to display neutral mood of the week
                        moodStats.setText("In the past 7 days, you have been mostly neutral. \nSpend more time on your mental health! \nHere's an article to help :)");
                        // setting article related texts and image to the neutral article information
                        articleImage.setImageResource(R.drawable.neutral);
                        article.setText("Shifting Away From Neutral");
                        // setting the text to bring the user to the neutral article
                        viewArticle.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://albertellis.org/2014/09/shifting-away-neutral/"));
                                startActivity(browserIntent);
                                return false;
                            }
                        });
                    } else if (motw.equals("sad")) { // if the mood of the week is sad
                        // setting the text to display sad mood of the week
                        moodStats.setText("In the past 7 days, you have been mostly sad. \nBe kind to yourself. \nHere are some ways to help.");
                        // setting article related texts and image to the sad article information
                        articleImage.setImageResource(R.drawable.sad);
                        // setting the text to bring the user to the sad article
                        article.setText("Why am I sad all the time?");
                        viewArticle.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://au.reachout.com/articles/why-am-i-sad-all-the-time"));
                                startActivity(browserIntent);
                                return false;
                            }
                        });
                    } else { // if the mood of the week is stressed
                        // setting the text to display stressed mood of the week
                        moodStats.setText("In the past 7 days, you have been mostly stressed. \nTake a breather and de-stress. \nHere are some ways do so.");
                        // setting article related texts and image to the stressed article information
                        articleImage.setImageResource(R.drawable.stressed);
                        article.setText("15 Simple Ways to Relieve Stress");
                        // setting the text to bring the user to the stressed article
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
            // if the user has never entered a mood before
            else{
                moodStats.setText("You have yet to enter a mood in the past week!");
                // removing article related texts and image
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