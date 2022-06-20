package sg.edu.np.mad.madassignment1;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        //define dbHandler
        DBHandler dbHandler = new DBHandler(getActivity(), null, null,6);

        //fill moodList with db data
        ArrayList<Mood> moodList = new ArrayList<>();
        moodList = dbHandler.getMoodData();

        // set date string to today's date
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        //make list final
        ArrayList<Mood> finalMoodList = moodList;
        //Happy Button
        Button happy = view.findViewById(R.id.happyButton);
        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mood = "happy";

                // to find out if entry of mood already exist for today
                Mood existingEntry = moodToBeChanged(finalMoodList, date);
                // if not yet, create the entry and add to database
                if (existingEntry == null){
                    addEntryToDB(date, mood, dbHandler);
                    switchingFragmentWithBundle(mood);
                }
                //if already have entry, prompt user abt it
                else{

                    //if mood selected is the same; just show message
                    if (existingEntry.getMood().equals(mood)){
                        switchingFragmentWithBundle(mood);
                    }
                    else{
                        promptChangeMood(existingEntry,dbHandler,mood);
                    }
                }
            }
        });

        //Neutral Button
        Button neutral = view.findViewById(R.id.neutralButton);
        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mood = "neutral";

                // to find out if entry of mood already exist for today
                Mood existingEntry = moodToBeChanged(finalMoodList, date);
                // if not yet, create the entry and add to database
                if (existingEntry == null){
                    addEntryToDB(date, mood, dbHandler);
                    switchingFragmentWithBundle(mood);
                }
                //if already have entry, prompt user abt it
                else{

                    //if mood selected is the same; just show message
                    if (existingEntry.getMood().equals(mood)){
                        switchingFragmentWithBundle(mood);
                    }
                    else{
                        promptChangeMood(existingEntry,dbHandler,mood);
                    }
                }
            }

        });

        //Sad Button
        Button sad = view.findViewById(R.id.sadButton);
        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mood = "sad";

                /// to find out if entry of mood already exist for today
                Mood existingEntry = moodToBeChanged(finalMoodList, date);
                // if not yet, create the entry and add to database
                if (existingEntry == null){
                    addEntryToDB(date, mood, dbHandler);
                    switchingFragmentWithBundle(mood);
                }
                //if already have entry, prompt user abt it
                else{

                    //if mood selected is the same; just show message
                    if (existingEntry.getMood().equals(mood)){
                        switchingFragmentWithBundle(mood);
                    }
                    else{
                        promptChangeMood(existingEntry,dbHandler,mood);
                    }
                }
            }
        });

        //Stressed Button
        Button stressed = view.findViewById(R.id.stressButton);
        stressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mood = "stressed";

                // to find out if entry of mood already exist for today
                Mood existingEntry = moodToBeChanged(finalMoodList, date);
                // if not yet, create the entry and add to database
                if (existingEntry == null){
                    addEntryToDB(date, mood, dbHandler);
                    switchingFragmentWithBundle(mood);
                }
                //if already have entry, prompt user abt it
                else{

                    //if mood selected is the same; just show message
                    if (existingEntry.getMood().equals(mood)){
                        switchingFragmentWithBundle(mood);
                    }
                    else{
                        promptChangeMood(existingEntry,dbHandler,mood);
                    }
                }
            }
        });

        return view;
    }

    public void addEntryToDB (String date, String mood, DBHandler dbHandler){

        //create Mood object
        Mood newEntry = new Mood(date, mood);

        //add new task to db
        dbHandler.addMood(newEntry);
    }

    public void switchingFragmentWithBundle (String message){
        // To know what message to display
        Bundle b = new Bundle();
        b.putString("key", message);
        Fragment messageFrag = new moodmsgFragment();
        messageFrag.setArguments(b);
        // switch fragments
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.moodContainerView, messageFrag);
        fragmentTransaction.commit();
    }

    public Mood moodToBeChanged (ArrayList<Mood> moodList, String today){

        Mood changeMood = null;

        for (int i = 0; i < moodList.size(); i++){
            Mood mood = moodList.get(i);

            String entryDate = mood.getDate();
            if (entryDate.equals(today)){
                changeMood = mood;
                break;
            }
            else{
                continue;
            }
        }

        return changeMood;
    }

    public void promptChangeMood (Mood mood, DBHandler dbHandler, String newMood){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Feeling different now?");
        builder.setMessage("It seems like you have submitted your mood previously but " +
                "it's normal to have a change of mood throughout the day." +
                "\n\nWould you like to update your mood?");
        builder.setCancelable(true);
        builder.setPositiveButton("Change Mood", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        mood.changeMood(newMood);
                        dbHandler.changeMood(mood);
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