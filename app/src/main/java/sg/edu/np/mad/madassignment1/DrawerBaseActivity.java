package sg.edu.np.mad.madassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;

//this DrawerBaseActivity to be extended to all activities which need a hamburger icon
public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //setting global variables
    public String GLOBAL_PREF = "MyPrefs";

    //define navbar drawer
    DrawerLayout drawerLayout;

    //set the nav bar and inflate it
    @Override
    public void setContentView(View view) {
        //inflate
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        //define the fragment container(framelayout) which activities will be placed into
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        //add our view, the activity into the container
        container.addView(view);
        //set contents to the nav bar drawer
        super.setContentView(drawerLayout);

        //define tool bar defined in content_layout.xml
        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //gets the navigation view from somewhere idk help find
        //create listener for nav_ACTIVITYNAME for switching nav items
        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //drawer closing and opening state
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_drawer_open, R.string.menu_drawer_close);
        //add listener which toggle between open and close
        drawerLayout.addDrawerListener(toggle);
        //sync to all activities
        toggle.syncState();
    }

    //items  in the nav bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //close drawer
        drawerLayout.closeDrawer(GravityCompat.START);

        //calender button
        switch (item.getItemId()){
            //link nav bar button to this condition (R.id.NAVBUTTONNAME)
            //nav button name in res>menu>main_drawer_menu.xml>code
            case R.id.nav_calendar:
                //starts activity by intent
                startActivity(new Intent(this,CalenderActivity.class));
                //set animation
                overridePendingTransition(0,0);
                //break out of this switch case and return false, exiting this
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

        //Timer button
        switch (item.getItemId()){
            case R.id.nav_Timer:
                startActivity(new Intent(this,TaskActivity.class));
                overridePendingTransition(0,0);
                break;
        }

        //Settings button
        switch (item.getItemId()){
            case R.id.nav_Settings:
                startActivity(new Intent(this,SettingsActivity.class));
                overridePendingTransition(0,0);
                break;
        }

        //Log Out button
        switch (item.getItemId()){
            case R.id.nav_Logout:
                SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("remember", "false");
                editor.apply();
                startActivity(new Intent(this,LoginPageActivity.class));
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