package sg.edu.np.mad.madassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;


public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    //set the nav bar
    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_drawer_open, R.string.menu_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    //items  in the nav bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //close drawer
        drawerLayout.closeDrawer(GravityCompat.START);

        //calender button
        switch (item.getItemId()){
            case R.id.nav_calendar:
                startActivity(new Intent(this,CalenderActivity.class));
                overridePendingTransition(0,0);
                break;
        }

        //Home button
        switch (item.getItemId()){
            case R.id.nav_home:
                startActivity(new Intent(this,HomeActivity.class));
                overridePendingTransition(0,0);
                break;
        }

        //Tasks button
        switch (item.getItemId()){
            case R.id.nav_Tasks:
                startActivity(new Intent(this,TaskActivity.class));
                overridePendingTransition(0,0);
                break;
        }

        return false;
    }

    //give method to apply activity title
    protected void allocateActivityTitle(String titleString) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }
}