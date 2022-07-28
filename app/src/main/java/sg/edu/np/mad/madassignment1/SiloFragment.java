package sg.edu.np.mad.madassignment1;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
    private Button upgradeButtonHeight;
    private TextView taskPopUpSubTitle;
    private TextView taskPopUpSubTitle2;
    private ArrayList<ArrayList<Task>> taskList = new ArrayList<ArrayList<Task>>();
    private DBHandler dbHandler;
    private ArrayList<Integer> siloLevel;

    //requirement to increase height
    private int[] siloHeightUpgradeRequirement = new int[]{1,5,7,9,12,15,18,21,24};
    //requirement to build more silos
    private int[] siloNumUpgradeRequirement = new int[]{1,3,4,5};

    private boolean newSiloAvailable = false;
    private boolean newHeightAvailable = false;

    private ArrayList<String> farmData;
    private ImageView siloImage1,siloImage2,siloImage3;
    private ImageView[] siloImages;
    private int[] imageList = new int [] {R.drawable.android, R.drawable.a3, R.drawable.farmer, R.drawable.a1};

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
        TextView tvLabel = (TextView) view.findViewById(R.id.SiloFragmentTextView);
        tvLabel.setText(page + " -- " + title + "THIS IS SILO");

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

//        siloImage1.setImageResource(imageList[siloLevel.get(0)]);
//        siloImage2.setImageResource(imageList[siloLevel.get(1)]);
//        siloImage3.setImageResource(imageList[siloLevel.get(2)]);



        //background trees
//        ImageView treeImage = view.findViewById(R.id.tree_barn);
//        Glide.with(this.getContext()).load(R.drawable.tree).into(treeImage);

        //barn image clicked
        siloImage1.setClickable(true);
        siloImage2.setClickable(true);
        siloImage3.setClickable(true);

        siloImage1.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { siloImageClicked(user); }});
        siloImage2.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { siloImageClicked(user); }});
        siloImage3.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { siloImageClicked(user); }});



        return view;
    }

    private void siloImageClicked(User user) {
        Toast.makeText(this.getContext(), "SILO IMAGE CLICKED", Toast.LENGTH_LONG).show();

        //creation of pop up dialog
        dialogBuilder = new AlertDialog.Builder(this.getContext());
        final View SiloTaskPopupView = getLayoutInflater().inflate(R.layout.popup_silo_tasks, null);

        //define elements
        taskPopUpTitle = (TextView) SiloTaskPopupView.findViewById(R.id.SilotaskPopUpTitle);
        taskPopUpRecyclerView = (RecyclerView) SiloTaskPopupView.findViewById(R.id.SiloTaskPopUpRecyclerView);
        upgradeButton = (Button) SiloTaskPopupView.findViewById(R.id.SiloUpgradeButton);
//        upgradeButtonHeight = (Button) SiloTaskPopupView.findViewById(R.id.SiloHeightUpgradebutton);
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
                int index = -9999;

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

                    //get lowest silo level
                    int lowestSiloLevel = Collections.min(siloLevel);

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
                //TODO: create gif
                //code goes here
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

    //checks if there is a valid silo that can have its height upgraded
    //returns index of upgradable silo, returns -1 when none found
    public int checkHeightUpgradeableSilo(ArrayList<Integer> siloLevelArray){

        //set it to some insane number so when it does break, it breaks spectacularly
        int index=-9999;

        for (int i = 0; i < siloLevelArray.size(); i++) {
            if(siloLevelArray.get(i) !=0 || siloLevelArray.get(i) != 3){
                index = i;
                break;
            }
            index = -1;
        }

        return index;
    }
}