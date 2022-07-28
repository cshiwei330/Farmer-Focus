package sg.edu.np.mad.madassignment1;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashSet;
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
    private ArrayList<ArrayList<Task>> taskList = new ArrayList<ArrayList<Task>>();
    private DBHandler dbHandler;
    private int[] barnUpgradeRequirement = new int[]{0,5,7,9};
    private ArrayList<String> farmData;
    private ImageView siloImage1,siloImage2,siloImage3,siloImage4;
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
        taskList = getUniqueRecurringTaskList(user.getUserID());

        //set silo image
        siloImage1 = (ImageView) view.findViewById(R.id.siloImage1);
        siloImage2 = (ImageView) view.findViewById(R.id.siloImage2);
        siloImage3 = (ImageView) view.findViewById(R.id.siloImage3);
        siloImage4 = (ImageView) view.findViewById(R.id.siloImage4);

        //get int array from 0:0:0:0 data from DB
        String siloLevelsString = farmData.get(1);
        String[] siloLevelStringArray = siloLevelsString.split(":");
        ArrayList<Integer> siloLevel = new ArrayList<Integer>();
        for (String siloLevelString: siloLevelStringArray) {
            siloLevel.add(Integer.valueOf(siloLevelString));
        }

        siloImage1.setImageResource(imageList[siloLevel.get(0)]);
        siloImage2.setImageResource(imageList[siloLevel.get(1)]);
        siloImage3.setImageResource(imageList[siloLevel.get(2)]);
        siloImage4.setImageResource(imageList[siloLevel.get(3)]);


        //background trees
//        ImageView treeImage = view.findViewById(R.id.tree_barn);
//        Glide.with(this.getContext()).load(R.drawable.tree).into(treeImage);

        //barn image clicked
        siloImage1.setClickable(true);
        siloImage2.setClickable(true);
        siloImage3.setClickable(true);
        siloImage4.setClickable(true);
        siloImage1.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { siloImageClicked(user); }});
        siloImage2.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { siloImageClicked(user); }});
        siloImage3.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { siloImageClicked(user); }});
        siloImage4.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { siloImageClicked(user); }});


        return view;
    }

    private void siloImageClicked(User user) {
        Toast.makeText(this.getContext(), "SILO IMAGE CLICKED", Toast.LENGTH_LONG).show();
    }

    public ArrayList<ArrayList<Task>> getUniqueRecurringTaskList(int userID){
        //fill taskList with db data

        ArrayList<Task> allTaskList = new ArrayList<Task>();
        ArrayList<Task> uniqueRecurringTasks = new ArrayList<Task>();
        ArrayList<Integer> recurringTasksID = new ArrayList<Integer>();

        allTaskList = dbHandler.getTaskData(userID);
        for (Task t:allTaskList){
            if (t.getTaskType().equals("Recurring")){
                recurringTasksID.add(t.getRecurringId());
            }
        }

        Set<Integer> uniqueRecurringTaskID = new HashSet<Integer>(recurringTasksID);
        int uniqueRecurringTaskNum = uniqueRecurringTaskID.size();

        ArrayList<ArrayList<Task>> arrayOfArrayOfRecurringTasks = new ArrayList<ArrayList<Task>>();


        for (int j = 0; j < uniqueRecurringTaskNum; j++) {

            arrayOfArrayOfRecurringTasks.add(new ArrayList<Task>());

            for (int i = 0; i < allTaskList.size(); i++) {

                if (allTaskList.get(i).getTaskType().equals("Recurring") && allTaskList.get(i).getRecurringId() == j) {
                    arrayOfArrayOfRecurringTasks.get(j).add(allTaskList.get(i));
                }
            }
        }
        return  arrayOfArrayOfRecurringTasks;
    }
}