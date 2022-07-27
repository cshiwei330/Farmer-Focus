package sg.edu.np.mad.madassignment1;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
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

public class BarnFragment extends Fragment {

    // Store instance variables
    private String title;
    private int page;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView barnTaskPopUpTitle;
    private RecyclerView barnTaskPopUpRecyclerView;
    private Button upgradeButton;
    private TextView barnTaskPopUpSubTitle;
    private ArrayList<Task> taskList = new ArrayList<>();
    private DBHandler dbHandler;
    private int[] barnUpgradeRequirement = new int[]{0,5,7,9};
    private ArrayList<Integer> farmData;
    private ImageView barnImage;
    private int[] imageList = new int [] {R.drawable.android, R.drawable.a3, R.drawable.farmer, R.drawable.a1};



    public String GLOBAL_PREF = "MyPrefs";


    public static BarnFragment newInstance(int page, String title) {
        BarnFragment barnFragment = new BarnFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        barnFragment.setArguments(args);
        return barnFragment;
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
        View view = inflater.inflate(R.layout.fragment_barn, container, false);
        TextView tvLabel = (TextView) view.findViewById(R.id.BarnFragmentTextView);
        tvLabel.setText(page + " -- " + title + "THIS IS BARN");

        //define dbHandler
        dbHandler = new DBHandler(this.getContext(), null, null,6);

        // shared preferences to get username
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        //get farm data
        //index0 = barnLevel
        //index1 = siloLevel
        farmData = new ArrayList<Integer>();
        farmData = dbHandler.findFarm(user.getUserID());

        //check farm data is valid
        try {
            if (farmData.get(0) == null || farmData.get(1) == null){
                dbHandler.addFarm(user);
                farmData = dbHandler.findFarm(user.getUserID());
            }
        }
        catch (IndexOutOfBoundsException e){
            dbHandler.addFarm(user);
            farmData = dbHandler.findFarm(user.getUserID());
        }

        //get tasks
        taskList = getFilteredCompleteTaskList(user.getUserID());

        //set barn image
        barnImage = (ImageView) view.findViewById(R.id.BarnImageView);
        barnImage.setImageResource(imageList[farmData.get(0)]);

        //barn image clicked
        barnImage.setClickable(true);
        barnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),
                        "BARN IMAGE CLICKED",
                        Toast.LENGTH_LONG).show();
                createBarnTaskPopUp(user);
            }
        });



        return view;
    }

    public void createBarnTaskPopUp(User user){
        dialogBuilder = new AlertDialog.Builder(this.getContext());
        final View BarnTaskPopupView = getLayoutInflater().inflate(R.layout.popup_barn_tasks, null);

        barnTaskPopUpTitle = (TextView) BarnTaskPopupView.findViewById(R.id.barnTaskPopUpTitle);
        barnTaskPopUpRecyclerView = (RecyclerView) BarnTaskPopupView.findViewById(R.id.barnTaskPopUpRecyclerView);
        upgradeButton = (Button) BarnTaskPopupView.findViewById(R.id.UpgradeButton);
        barnTaskPopUpSubTitle = (TextView) BarnTaskPopupView.findViewById(R.id.barnTaskPopUpSubTitle);

        //set decorative line separator between viewHolders
        //barnTaskPopUpRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));

        //get currently required number of completed tasks to upgrade
        int req = barnUpgradeRequirement[farmData.get(0)];
        int reqTaskLeft = req-taskList.size();

        //set texts
        barnTaskPopUpSubTitle.setText("Event Tasks Completed: "+ taskList.size());

        if (reqTaskLeft>0){
            barnTaskPopUpTitle.setText("Next Upgrade: Complete "+ reqTaskLeft + " More Tasks");
            upgradeButton.setVisibility(View.GONE);
        }
        else {
            barnTaskPopUpTitle.setVisibility(View.INVISIBLE);
            upgradeButton.setVisibility(View.VISIBLE);
        }

        // initialize recyclerview for TASKS
        //set adaptor to TaskRecyclerViewAdaptor, given taskList
        BarnTaskAdapter barnTaskAdapter = new BarnTaskAdapter(taskList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        barnTaskPopUpRecyclerView.setLayoutManager(mLayoutManager);
        barnTaskPopUpRecyclerView.setAdapter(barnTaskAdapter);


        dialogBuilder.setView(BarnTaskPopupView);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        //upgrade clicked
        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close dialog
                dialog.dismiss();

                //update database and local data
                farmData.set(0, farmData.get(0)+1);
                dbHandler.upgradeBarn(user.getUserID(), farmData.get(0));

                //play gif and set new barnImage
                //TODO: create gif
                //code goes here
                barnImage.setImageResource(imageList[farmData.get(0)]);

            }
        });
    }

    public ArrayList<Task> getFilteredCompleteTaskList(int userID){
        //fill taskList with db data

        ArrayList<Task> AlltaskList = new ArrayList<Task>();
        ArrayList<Task> completedTaskList = new ArrayList<Task>();

        AlltaskList = dbHandler.getTaskData(userID);
        for (Task t:AlltaskList){
            if (t.getStatus()!=0 && t.getTaskType().equals("Event")){
                completedTaskList.add(t);
            }
        }
        return  completedTaskList;
    }
}