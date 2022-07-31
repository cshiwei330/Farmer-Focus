package sg.edu.np.mad.madassignment1;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/*----------------------------------------------------------------------------------------------------*/

                                /* HOME ACTIVITY - MoodFragment */
/* This Fragment shows a mood selector whereby users can input their mood for the day. Their mood will be
 * recorded in the database for statistics activity. When the user input a different mood on the same day, an
 * alert dialogue will appear to ask the user if they want to update their mood and the change will be
 * reflected in the database. When a mood is clicked, this fragment will switch to MoodMsgFragment with a
 * bundle that consists the mood the user selected. */

/*----------------------------------------------------------------------------------------------------*/

public class MoodFragment extends Fragment {

    public String GLOBAL_PREF = "MyPrefs";

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

        //define dbHandler
        DBHandler dbHandler = new DBHandler(getActivity(), null, null,6);

        // shared preferences to get username
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        //fill moodList with db data
        ArrayList<Mood> moodList = new ArrayList<>();
        moodList = dbHandler.getMoodData(user.getUserID());

        // set date string to today's date
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        //make list final
        ArrayList<Mood> finalMoodList = moodList;
        //Happy Button
        ImageView happy = view.findViewById(R.id.happyButton);
        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mood = "happy";

                // to find out if entry of mood already exist for today
                Mood existingEntry = moodToBeChanged(finalMoodList, date, user.getUserID());
                // if not yet, create the entry and add to database
                if (existingEntry == null){
                    addEntryToDB(date, mood, dbHandler, user.getUserID());
                    switchingFragmentWithBundle(mood);
                }
                //if already have entry, prompt user abt it
                else{

                    //if mood selected is the same; just show message
                    if (existingEntry.getMood().equals(mood)){
                        switchingFragmentWithBundle(mood);
                    }
                    //otherwise, show alert to ask user to confirm the mood change and update database
                    else{
                        promptChangeMood(existingEntry,dbHandler,mood);
                    }
                }
            }
        });

        //Neutral Button
        ImageView neutral = view.findViewById(R.id.neutralButton);
        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mood = "neutral";

                // to find out if entry of mood already exist for today
                Mood existingEntry = moodToBeChanged(finalMoodList, date, user.getUserID());
                // if not yet, create the entry and add to database
                if (existingEntry == null){
                    addEntryToDB(date, mood, dbHandler, user.getUserID());
                    switchingFragmentWithBundle(mood);
                }
                //if already have entry, prompt user abt it
                else{

                    //if mood selected is the same; just show message
                    if (existingEntry.getMood().equals(mood)){
                        switchingFragmentWithBundle(mood);
                    }
                    //otherwise, show alert to ask user to confirm the mood change and update database
                    else{
                        promptChangeMood(existingEntry,dbHandler,mood);
                    }
                }
            }

        });

        //Sad Button
        ImageView sad = view.findViewById(R.id.sadButton);
        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mood = "sad";

                /// to find out if entry of mood already exist for today
                Mood existingEntry = moodToBeChanged(finalMoodList, date, user.getUserID());
                // if not yet, create the entry and add to database
                if (existingEntry == null){
                    addEntryToDB(date, mood, dbHandler, user.getUserID());
                    switchingFragmentWithBundle(mood);
                }
                //if already have entry, prompt user abt it
                else{

                    //if mood selected is the same; just show message
                    if (existingEntry.getMood().equals(mood)){
                        switchingFragmentWithBundle(mood);
                    }
                    //otherwise, show alert to ask user to confirm the mood change and update database
                    else{
                        promptChangeMood(existingEntry,dbHandler,mood);
                    }
                }
            }
        });

        //Stressed Button
        ImageView stressed = view.findViewById(R.id.stressedButton);
        stressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mood = "stressed";

                // to find out if entry of mood already exist for today
                Mood existingEntry = moodToBeChanged(finalMoodList, date, user.getUserID());
                // if not yet, create the entry and add to database
                if (existingEntry == null){
                    addEntryToDB(date, mood, dbHandler, user.getUserID());
                    switchingFragmentWithBundle(mood);
                }
                //if already have entry, prompt user abt it
                else{

                    //if mood selected is the same; just show message
                    if (existingEntry.getMood().equals(mood)){
                        switchingFragmentWithBundle(mood);
                    }
                    //otherwise, show alert to ask user to confirm the mood change and update database
                    else{
                        promptChangeMood(existingEntry,dbHandler,mood);
                    }
                }
            }
        });

        return view;
    }

    public void addEntryToDB (String date, String mood, DBHandler dbHandler, Integer userID){

        //create Mood object
        Mood newEntry = new Mood(date, mood, userID);

        //add new task to db
        dbHandler.addMood(newEntry);
    }

    public void switchingFragmentWithBundle (String message){
        // To know what message to display
        Bundle b = new Bundle();
        b.putString("key", message);
        Fragment messageFrag = new MoodMsgFragment();
        messageFrag.setArguments(b);
        // switch fragments
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.moodContainerView, messageFrag);
        fragmentTransaction.commit();
    }

    public Mood moodToBeChanged (ArrayList<Mood> moodList, String today, Integer userID){

        Mood changeMood = null; //set mood object to be null

        for (int i = 0; i < moodList.size(); i++){  //loop thru mood list to find existing mood entry for today
            Mood mood = moodList.get(i);

            String entryDate = mood.getDate();

            if (entryDate.equals(today) && (mood.getMoodUserID()==userID)){       //if there's already a entry, return the mood Object
                changeMood = mood;
                break;
            }
        }

        return changeMood;
    }

    // if a different mood is selected on the same day, prompt user
    public void promptChangeMood (Mood mood, DBHandler dbHandler, String newMood){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Feeling different now?");
        builder.setMessage("It seems like you have submitted your mood previously but " +
                "it's normal to have a change of mood throughout the day." +
                "\n\nWould you like to update your mood?");
        builder.setCancelable(true);
        builder.setPositiveButton("Update Mood", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        mood.changeMood(newMood);
                        dbHandler.changeMood(mood);     //update database
                        Toast.makeText(getActivity(), "Updated your mood!", Toast.LENGTH_SHORT).show();

                        //switch fragment to show message based on new mood
                        switchingFragmentWithBundle(newMood);
                    }
                });
        builder.setNegativeButton("Cancel", new
                DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });

        AlertDialog promptChangeMood = builder.create();
        promptChangeMood.show();
    }


}