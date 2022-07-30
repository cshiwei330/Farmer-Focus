package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.ArrayList;

//import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator;
import sg.edu.np.mad.madassignment1.databinding.ActivityFarmBinding;
import sg.edu.np.mad.madassignment1.databinding.ActivityTaskBinding;

public class FarmActivity extends DrawerBaseActivity {

    //define activity binding
    ActivityFarmBinding activityFarmBinding;
    //define taskList array
    ArrayList<Task> taskList = new ArrayList<>();

    public String GLOBAL_PREF = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Navigate from navigation bar to activity
        //inflate according to activity binding to show
        activityFarmBinding = ActivityFarmBinding.inflate(getLayoutInflater());
        //set view to this activity
        setContentView(activityFarmBinding.getRoot());
        //set title
        allocateActivityTitle("My Farm");

        //set viewPager adapter
        FragmentPagerAdapter adapterViewPager;
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new PagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        //set indicator
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(vpPager);

        //set moving clouds
        ImageView cloud1 = findViewById(R.id.cloud1);
        ImageView cloud2 = findViewById(R.id.cloud2);
        ImageView[] cloudsList = new ImageView[]{cloud1,cloud2};
        for (ImageView cloud :cloudsList) {
            cloud.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_right));
        }

        //after cloud has moved off screen on the right, (50000ms) reset position and keep doing it evert (5000ms)
        //define handler
        Handler handler = new Handler();
        //define code to run
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                for (ImageView cloud :cloudsList) {
                    //reset position
                    cloud.animate().translationX(0);
                    //clear animation stack
                    cloud.clearAnimation();
                    //reanimate right movement
                    cloud.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_right));                }
                //repeat again
                handler.postDelayed(this, 50000);
            }
        };
        //start the initial runnable code once
        handler.post(runnableCode);

    }
}
