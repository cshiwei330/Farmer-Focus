package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import sg.edu.np.mad.madassignment1.databinding.ActivityDashBoardBinding;

public class DashBoardActivity extends DrawerBaseActivity {

    ActivityDashBoardBinding activityDashBoardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashBoardBinding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(activityDashBoardBinding.getRoot());
        Context context = this;
    }
}