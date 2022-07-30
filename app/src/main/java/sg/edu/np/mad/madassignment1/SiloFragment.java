package sg.edu.np.mad.madassignment1;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SiloFragment extends Fragment {

    // Store instance variables
    private String title;
    private int page;
    public String GLOBAL_PREF = "MyPrefs";

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView taskPopUpTitle;
    private RecyclerView taskPopUpRecyclerView;
    private Button upgradeButton;

    private TextView taskPopUpSubTitle;
    private TextView taskPopUpSubTitle2;
    private ArrayList<ArrayList<Task>> taskList = new ArrayList<ArrayList<Task>>();
    private DBHandler dbHandler;
    private ArrayList<Integer> siloLevel;

    //requirement to increase height
    private int[] siloHeightUpgradeRequirement = new int[]{1,5,10,15,20,30,40,50,75};
    //requirement to build more silos
    private int[] siloNumUpgradeRequirement = new int[]{1,3,5,7};

    /*
    //USE THESE REQUIREMENTS FOR DEMO TO SHOW UPGRADING
    //requirement to increase height
    private int[] siloHeightUpgradeRequirement = new int[]{1,2,3,4,5,6,7,8,9};
    //requirement to build more silos
    private int[] siloNumUpgradeRequirement = new int[]{1,2,3,4};
     */

    private boolean newSiloAvailable = false;
    private boolean newHeightAvailable = false;

    private ArrayList<String> farmData;

    private ImageView siloImage1,siloImage2,siloImage3;
    private ImageView[] siloImages;

    private ImageView siloExplosion1,siloExplosion2,siloExplosion3;
    private ImageView[] siloExplosionImages;

    private ImageView corn1,corn2,corn3,corn4;
    private ImageView[] cornImages;

    private ImageView farmPlot1,farmPlot2,farmPlot3,farmPlot4;
    private ImageView[] farmPlots;

    private int index = -9999;

    private int[] imageList = new int [] {R.drawable.unconstructed_small, R.drawable.silo1, R.drawable.silo2, R.drawable.silo3};

    public static SiloFragment newInstance(int page, String title) {
        SiloFragment siloFragment = new SiloFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        siloFragment.setArguments(args);
        return siloFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_silo, container, false);

        //this text is purely for debugging
        TextView tvLabel = (TextView) view.findViewById(R.id.SiloFragmentTextView);
        tvLabel.setText(page + " -- " + title + "THIS IS SILO");
        tvLabel.setVisibility(View.GONE);

        //define dbHandler
        dbHandler = new DBHandler(this.getContext(), null, null,6);

        // shared preferences to get username
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        //get farm data
        //index0 = barnLevel
        //index1 = siloLevel
        farmData = new ArrayList<String>();
        farmData = dbHandler.findFarm(user.getUserID());

        //get tasks
        //taskList is an array of task arrays, each task array is a unique recurring task with its owns tasks, both completed and uncompleted
        taskList = getUniqueRecurringTaskList(user.getUserID());

        //get int array from 0:0:0:0 data from DB
        String siloLevelsString = farmData.get(1);
        String[] siloLevelStringArray = siloLevelsString.split(":");
        siloLevel = new ArrayList<Integer>();
        for (String siloLevelString: siloLevelStringArray) {
            siloLevel.add(Integer.valueOf(siloLevelString));
        }

        //set silo image
        siloImage1 = (ImageView) view.findViewById(R.id.siloImage1);
        siloImage2 = (ImageView) view.findViewById(R.id.siloImage2);
        siloImage3 = (ImageView) view.findViewById(R.id.siloImage3);
        siloImages = new ImageView[] {siloImage1,siloImage2,siloImage3};

        for (int i = 0; i < siloImages.length; i++) {
            siloImages[i].setImageResource(imageList[siloLevel.get(i)]);
        }

        //set upgrade animation to gone
        siloExplosion1 = (ImageView) view.findViewById(R.id.silo1_explosion);
        siloExplosion2 = (ImageView) view.findViewById(R.id.silo2_explosion);
        siloExplosion3 = (ImageView) view.findViewById(R.id.silo3_explosion);
        siloExplosionImages = new ImageView[] {siloExplosion1,siloExplosion2,siloExplosion3};

        for (int i = 0; i < siloExplosionImages.length; i++) {
            siloExplosionImages[i].setVisibility(View.GONE);
        }

        //background trees
        ImageView treeImage = view.findViewById(R.id.tree_silo);
        Glide.with(this.getContext()).load(R.drawable.trees).into(treeImage);

        //set farm images
        //based on number of tasks in the next week
        ArrayList<Task> tasksThisWeek = tasksThisWeek(user.getUserID());
        int tasksThisWeekNum = tasksThisWeek.size();

        int[] farmPlotsReq = new int[]{0,1,4,8,12};
        int currentFarmPlotLevel = 999;

        //get plot farm level
        //if eg 5 tasks completed in last week, current plot farm level is 3
        for (int i = 0; i < farmPlotsReq.length; i++) {
            if (tasksThisWeekNum>=farmPlotsReq[i]){
                currentFarmPlotLevel = i;
            }
        }

        //set farm images
        //set waving corn gifs
        corn1 = (ImageView) view.findViewById(R.id.corn1);
        corn2 = (ImageView) view.findViewById(R.id.corn2);
        corn3 = (ImageView) view.findViewById(R.id.corn3);
        corn4 = (ImageView) view.findViewById(R.id.corn4);
        cornImages = new ImageView[] {corn1,corn2,corn3,corn4};

        //set all to invisible
        for (int i = 0; i < cornImages.length; i++) {
            cornImages[i].setVisibility(View.GONE);
        }

        //set visible corn based on number of tasks in following week
        for (int i = 0; i < currentFarmPlotLevel; i++) {
            cornImages[i].setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(R.drawable.farm_plot_corn).into(cornImages[i]);
        }

        //ID farm plots
        farmPlot1 = (ImageView) view.findViewById(R.id.farmPlot1);
        farmPlot2 = (ImageView) view.findViewById(R.id.farmPlot2);
        farmPlot3 = (ImageView) view.findViewById(R.id.farmPlot3);
        farmPlot4 = (ImageView) view.findViewById(R.id.farmPlot4);
        farmPlots = new ImageView[] {farmPlot1,farmPlot2,farmPlot3,farmPlot4};
        //whedn any part of farm is clicked, show toast
        for (int i = 0; i < cornImages.length; i++) {
            //SET EVERYTHING TO BE CLICKED BECAUSE ANDROID DOESN'T UNDERSTAND WHAT A TOUCH IS??
            cornImages[i].setClickable(true);
            farmPlots[i].setClickable(true);
            cornImages[i].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Toast.makeText(getContext(), "Plan more tasks to grow more!", Toast.LENGTH_LONG).show();; }});
            farmPlots[i].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Toast.makeText(getContext(), "Plan more tasks to grow more!", Toast.LENGTH_LONG).show();; }});
        }


        //silo image clicked
        for (ImageView siloImage:siloImages) {
            siloImage.setClickable(true);
            siloImage.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { siloImageClicked(user); }});
        }

        return view;
    }

    private void siloImageClicked(User user) {

        //creation of pop up dialog
        dialogBuilder = new AlertDialog.Builder(this.getContext());
        final View SiloTaskPopupView = getLayoutInflater().inflate(R.layout.popup_silo_tasks, null);

        //define elements
        taskPopUpTitle = (TextView) SiloTaskPopupView.findViewById(R.id.SilotaskPopUpTitle);
        taskPopUpRecyclerView = (RecyclerView) SiloTaskPopupView.findViewById(R.id.SiloTaskPopUpRecyclerView);
        upgradeButton = (Button) SiloTaskPopupView.findViewById(R.id.SiloUpgradeButton);
        taskPopUpSubTitle = (TextView) SiloTaskPopupView.findViewById(R.id.SiloTaskPopUpSubTitle);
        taskPopUpSubTitle2 = (TextView) SiloTaskPopupView.findViewById(R.id.SiloTaskPopUpSubTitle2);

        //set decorative line separator between viewHolders
        //barnTaskPopUpRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));

        //get currently required number of completed tasks to upgrade
//        //requirement to increase height
//        private int[] siloHeightUpgradeRequirement = new int[]{1,5,7,9,12,15,18,21,24};
//        //requirement to build more silos
//        private int[] siloNumUpgradeRequirement = new int[]{1,3,4,5};

        //get total number of completed recurring tasks
        int totalRecurringTaskCompletedNum = 0;
        for (ArrayList<Task> uniqueRecurring:taskList) {
            for (Task t:uniqueRecurring){
                if (t.getStatus()==1){
                    totalRecurringTaskCompletedNum += 1;
                }
            }
        }
        taskPopUpTitle.setText("Recurring Tasks Completed: "+ totalRecurringTaskCompletedNum);

        newSiloAvailable = false;
        newHeightAvailable = false;
        upgradeButton.setVisibility(View.GONE);

        //new silo
        //get total silos built
        int silosBuilt = 0;
        for (int level:siloLevel) {
            if (level > 0){
                silosBuilt += 1;
            }
        }

        //if total level less than 4, not all silo built
        //require ## number of unique recurring tasks to build new silo
        if(silosBuilt<3){

            int newSiloReq = siloNumUpgradeRequirement[silosBuilt];
            int newSiloReqLeft = newSiloReq-taskList.size();

            if (newSiloReqLeft>0){
                taskPopUpSubTitle2.setVisibility(View.VISIBLE);
                taskPopUpSubTitle2.setText("New Silo: Create "+ newSiloReqLeft + " More Recurring Tasks");
//                upgradeButton.setVisibility(View.GONE);
            }

            else{
                taskPopUpSubTitle2.setVisibility(View.INVISIBLE);
//                upgradeButton.setVisibility(View.VISIBLE);
                newSiloAvailable = true;
            }

        }
        else {
            taskPopUpSubTitle2.setVisibility(View.VISIBLE);
            taskPopUpSubTitle2.setText("New Silo : Maximum Reached!");
        }

        //silo height increase
        //require ## number of recurring tasks completed
        //totalRecurringTaskCompletedNum

        //get required number of tasks completed for silo levels
        int totalSiloLevel = 0;
        for (int level:siloLevel) {
            totalSiloLevel += level;
        }

        //if total level is 8>, max level is reached
        //individual level of a silo is max 3
        if(totalSiloLevel<9){

            //if there is an upgradable silo available
            if (checkHeightUpgradeableSilo(siloLevel)!=-1){

                int heightUpgradReq = siloHeightUpgradeRequirement[totalSiloLevel];
                int heightUpgradReqLeft = heightUpgradReq-totalRecurringTaskCompletedNum;

                if(heightUpgradReqLeft>0){
                    taskPopUpSubTitle.setVisibility(View.VISIBLE);
                    taskPopUpSubTitle.setText("Upgrade Silo: Complete " + heightUpgradReqLeft + " More Recurring Tasks");
//                    upgradeButtonHeight.setVisibility(View.GONE);
                }

                else{
                    taskPopUpSubTitle.setVisibility(View.INVISIBLE);
//                    upgradeButtonHeight.setVisibility(View.VISIBLE);
                    newHeightAvailable = true;
                }
            }
            else {
                taskPopUpSubTitle.setVisibility(View.VISIBLE);
                taskPopUpSubTitle.setText("Upgrade Silo: Build More Silos First");
            }
        }
        else{
            taskPopUpSubTitle.setVisibility(View.VISIBLE);
            taskPopUpSubTitle.setText("Upgrade Silo: Maximum Reached!");
        }

        //if upgrade is available make all subTitles disappear and button to appear
        if (newSiloAvailable||newHeightAvailable){
            taskPopUpSubTitle.setVisibility(View.INVISIBLE);
            taskPopUpSubTitle2.setVisibility(View.INVISIBLE);
            upgradeButton.setVisibility(View.VISIBLE);
        }

        // initialize recyclerview for TASKS
        //set adaptor to TaskRecyclerViewAdaptor, given taskList
        SiloTaskAdapter siloTaskAdapter = new SiloTaskAdapter(taskList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        taskPopUpRecyclerView.setLayoutManager(mLayoutManager);
        taskPopUpRecyclerView.setAdapter(siloTaskAdapter);

        dialogBuilder.setView(SiloTaskPopupView);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        //build silo button clicked
        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close dialog
                dialog.dismiss();

                //determine what type of upgrade is happening
                //build silo takes always happens first when available

                //get index of changed silo, set to insane number so it fails spectacularly
                index = -9999;

                if (newSiloAvailable){
                    //update database and local data
                    //+1 to the silo level if it is 0
                    for (int i = 0; i < siloLevel.size(); i++) {

                        if(siloLevel.get(i)==0){
                            siloLevel.set(i,1);
                            index = i;
                            break;
                        }
                    }
                    newSiloAvailable = false;
                }

                else if (newHeightAvailable){
                    //update database and local data
                    //+1 to the silo with the lowest level
                    int lowestSiloLevel=1;

                    //get lowest silo level that is not 0 and maxed level
                    for (Integer level:siloLevel) {
                        if (level!=0 && level!= 3){
                            lowestSiloLevel = level;
                            break;
                        }
                    }

                    for (int i = 0; i < siloLevel.size(); i++) {

                        if(siloLevel.get(i)==lowestSiloLevel){
                            siloLevel.set(i,siloLevel.get(i)+1);
                            index = i;
                            break;
                        }
                    }
                    newHeightAvailable = false;
                }

                //recreate string of silo data for database
                String siloDataString = siloLevel.get(0)+":"+siloLevel.get(1)+":"+siloLevel.get(2);

                dbHandler.buildSilo(user.getUserID(), siloDataString);


                //play gif and set new barnImage
                //upgrade animation
                siloExplosionImages[index].setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(R.drawable.explosion_animation).into(siloExplosionImages[index]);
                //after gif has finished playing, (700ms) set gif to GONE
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        siloExplosionImages[index].setVisibility(View.GONE);
                    }
                }, 700);

                siloImages[index].setImageResource(imageList[siloLevel.get(index)]);

            }
        });
    }
    //returns an array of array of tasks
    //each arraylist of task
    public ArrayList<ArrayList<Task>> getUniqueRecurringTaskList(int userID){
        //fill taskList with db data

        ArrayList<Task> allTaskList = new ArrayList<Task>();
        ArrayList<Task> uniqueRecurringTasks = new ArrayList<Task>();
        ArrayList<Integer> recurringTasksID = new ArrayList<Integer>();

        //get all recurringID
        allTaskList = dbHandler.getTaskData(userID);
        for (Task t:allTaskList){
            if (t.getTaskType().equals("Recurring")){
                recurringTasksID.add(t.getRecurringId());
            }
        }

        //get unique recurringID
        Set<Integer> uniqueRecurringTaskID = new HashSet<Integer>(recurringTasksID);
        //get number of unique recurring tasks
        int uniqueRecurringTaskNum = uniqueRecurringTaskID.size();

        //array of unique recurring tasks put into a bigger array
        ArrayList<ArrayList<Task>> arrayOfArrayOfRecurringTasks = new ArrayList<ArrayList<Task>>();

        //loop and create a new array for each unique recurring task
        for (int j = 0; j < uniqueRecurringTaskNum; j++) {

            arrayOfArrayOfRecurringTasks.add(new ArrayList<Task>());

            //loop through all tasks
            for (int i = 0; i < allTaskList.size(); i++) {

                //if task is recurring and is the same recurring id, add task
                if (allTaskList.get(i).getTaskType().equals("Recurring") && allTaskList.get(i).getRecurringId() == j+1) {
                    arrayOfArrayOfRecurringTasks.get(j).add(allTaskList.get(i));
                }
            }
        }
        return  arrayOfArrayOfRecurringTasks;
    }

    public ArrayList<Task> tasksThisWeek(int userID){

        ArrayList<Task> allTaskList = new ArrayList<Task>();
        ArrayList<Task> taskNextWeek = new ArrayList<Task>();

        //get today's date
        //get current date
        //get current date
        Calendar calendar = Calendar.getInstance();

        ArrayList<String> followingWeekDates = new ArrayList<String>();

        //set date format
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        //apply date format to current date

        //add today's date to list
        Date date = calendar.getTime();
        String stringDate = df.format(date);
        followingWeekDates.add(stringDate);

        //get dates of next week
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DATE,1);
            date = calendar.getTime();
            stringDate = df.format(date);
            followingWeekDates.add(stringDate);
        }

        //get all tasks completed last week
        allTaskList = dbHandler.getTaskData(userID);
        for (Task t:allTaskList){
            for (String d:followingWeekDates){
                //if task date is in past week, add to list and break
                if (t.getTaskDate().equals(d)){
                    taskNextWeek.add(t);
                    break;
                }
            }
        }

        return taskNextWeek;
    }

    //checks if there is a valid silo that can have its height upgraded
    //returns index of upgradable silo, returns -1 when none found
    public int checkHeightUpgradeableSilo(ArrayList<Integer> siloLevelArray){

        //set it to some insane number so when it does break, it breaks spectacularly
        int index=-9999;

        for (int i = 0; i < siloLevelArray.size(); i++) {
            if(siloLevelArray.get(i) !=0 && siloLevelArray.get(i) != 3){
                index = i;
                break;
            }
            index = -1;
        }

        return index;
    }
}