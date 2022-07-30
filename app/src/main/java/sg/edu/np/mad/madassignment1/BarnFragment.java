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

    //requirement to upgrade barn
    private int[] barnUpgradeRequirement = new int[]{1,4,8,14};

    /*
    //USE THESE REQUIREMENTS FOR DEMO TO SHOW UPGRADING
    //requirement to upgrade barn
    private int[] barnUpgradeRequirement = new int[]{1,2,3,4};
    */

    private ArrayList<String> farmData;
    private Integer barnLevel;
    private ImageView barnImage;
    private ImageView explosion;
    private int[] imageList = new int [] {R.drawable.unconstructed_small, R.drawable.barn1, R.drawable.barn2, R.drawable.barn3};



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

        //this text is purely for debugging
        TextView tvLabel = (TextView) view.findViewById(R.id.BarnFragmentTextView);
        tvLabel.setText(page + " -- " + title + "THIS IS BARN");
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

        barnLevel = Integer.valueOf(farmData.get(0));

        //get tasks
        taskList = getFilteredCompleteTaskList(user.getUserID());

        //set barn image
        barnImage = (ImageView) view.findViewById(R.id.BarnImageView);
        barnImage.setImageResource(imageList[barnLevel]);
        //set upgrade animation to gone
        explosion = (ImageView) view.findViewById(R.id.explosion_imageView);
        explosion.setVisibility(View.GONE);

        //background trees
        ImageView treeImage = view.findViewById(R.id.tree_barn);
        ImageView treeImage2 = view.findViewById(R.id.tree_barn2);
        Glide.with(this.getContext()).load(R.drawable.trees).into(treeImage);
        Glide.with(this.getContext()).load(R.drawable.trees).into(treeImage2);

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

        barnTaskPopUpTitle = (TextView) BarnTaskPopupView.findViewById(R.id.BarntaskPopUpTitle);
        barnTaskPopUpRecyclerView = (RecyclerView) BarnTaskPopupView.findViewById(R.id.BarnTaskPopUpRecyclerView);
        upgradeButton = (Button) BarnTaskPopupView.findViewById(R.id.BarnUpgradeButton);
        barnTaskPopUpSubTitle = (TextView) BarnTaskPopupView.findViewById(R.id.BarnTaskPopUpSubTitle);

        //set decorative line separator between viewHolders
        //barnTaskPopUpRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));

        //get currently required number of completed tasks to upgrade
        int req = barnUpgradeRequirement[barnLevel];
        int reqTaskLeft = req-taskList.size();

        //set texts
        barnTaskPopUpSubTitle.setText("Event Tasks Completed: "+ taskList.size());

        //if barn level is not maxed
        if (barnLevel<3){
            if (reqTaskLeft>0){
                barnTaskPopUpTitle.setVisibility(View.VISIBLE);
                barnTaskPopUpTitle.setText("Next Upgrade: Complete "+ reqTaskLeft + " More Event Tasks");
                upgradeButton.setVisibility(View.GONE);
            }
            else {
                barnTaskPopUpTitle.setVisibility(View.INVISIBLE);
                upgradeButton.setVisibility(View.VISIBLE);
            }
        }
        else{
            barnTaskPopUpTitle.setVisibility(View.VISIBLE);
            barnTaskPopUpTitle.setText("Next Upgrade: Maximum Reached!");
            upgradeButton.setVisibility(View.GONE);
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
                barnLevel += 1;
                dbHandler.upgradeBarn(user.getUserID(), barnLevel);

                //play gif and set new barnImage

                //upgrade animation
                explosion.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(R.drawable.explosion_animation).into(explosion);
                //after gif has finished playing, (700ms) set gif to GONE
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        explosion.setVisibility(View.GONE);
                    }
                }, 700);


                barnImage.setImageResource(imageList[barnLevel]);

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