package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ImagesActivity extends AppCompatActivity {

    public String GLOBAL_PREF = "MyPrefs";
    //SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        //define dbHandler
        DBHandler dbHandler = new DBHandler(this, null, null,6);

        ImageView Image1 = findViewById(R.id.Image1);
        ImageView Image2 = findViewById(R.id.Image2);
        ImageView Image3 = findViewById(R.id.Image3);
        ImageView Image4 = findViewById(R.id.Image4);
        ImageView Default = findViewById(R.id.ProfilePic);

        //                switch(view.getId())
//                {
//                    case R.id.Image1:
//                        userDBdata.setImageID(1);
//                        Toast.makeText(ImagesActivity.this, "Testing", Toast.LENGTH_SHORT).show();
//                        dbHandler.updateProfile(userDBdata);
//                        Toast.makeText(ImagesActivity.this, "Team blue chosen", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.Image2:
//                        userDBdata.setImageID(2);
//                        dbHandler.updateProfile(userDBdata);
//                        Toast.makeText(ImagesActivity.this, "Team green chosen", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.Image3:
//                        userDBdata.setImageID(3);
//                        dbHandler.updateProfile(userDBdata);
//                        Toast.makeText(ImagesActivity.this, "Team purple chosen", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.Image4:
//                        userDBdata.setImageID(4);
//                        dbHandler.updateProfile(userDBdata);
//                        Toast.makeText(ImagesActivity.this, "Team yellow chosen", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.ProfilePic:
//                        userDBdata.setImageID(0);
//                        dbHandler.updateProfile(userDBdata);
//                        break;
//                }




        Image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // shared preferences to store latest username to set profile pic
                SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
                String username = sharedPreferences.getString("username", "");
                User userDBdata = dbHandler.findUser(username);

                userDBdata.setImageID(1);
                dbHandler.updateProfile(userDBdata);
                Toast.makeText(ImagesActivity.this, "Team 1 chosen!", Toast.LENGTH_SHORT).show();

                //create intent to go back to Settings
                Intent ImagesActivityToAccountSettingsActivity = new Intent(ImagesActivity.this, AccountSettingsActivity.class);
                startActivity(ImagesActivityToAccountSettingsActivity);

                //kill this activity
                finish();
            }
        });

        Image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // shared preferences to store latest username to set profile pic
                SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
                String username = sharedPreferences.getString("username", "");
                User userDBdata = dbHandler.findUser(username);

                userDBdata.setImageID(2);
                dbHandler.updateProfile(userDBdata);
                Toast.makeText(ImagesActivity.this, "Team 2 chosen!", Toast.LENGTH_SHORT).show();

                //create intent to go back to Settings
                Intent ImagesActivityToAccountSettingsActivity = new Intent(ImagesActivity.this, AccountSettingsActivity.class);
                startActivity(ImagesActivityToAccountSettingsActivity);

                //kill this activity
                finish();
            }
        });

        Image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // shared preferences to store latest username to set profile pic
                SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
                String username = sharedPreferences.getString("username", "");
                User userDBdata = dbHandler.findUser(username);

                userDBdata.setImageID(3);
                dbHandler.updateProfile(userDBdata);
                Toast.makeText(ImagesActivity.this, "Team 3 chosen!", Toast.LENGTH_SHORT).show();

                //create intent to go back to Settings
                Intent ImagesActivityToAccountSettingsActivity = new Intent(ImagesActivity.this, AccountSettingsActivity.class);
                startActivity(ImagesActivityToAccountSettingsActivity);

                //kill this activity
                finish();
            }
        });

        Image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // shared preferences to store latest username to set profile pic
                SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
                String username = sharedPreferences.getString("username", "");
                User userDBdata = dbHandler.findUser(username);

                userDBdata.setImageID(4);
                dbHandler.updateProfile(userDBdata);
                Toast.makeText(ImagesActivity.this, "Team 4 chosen!", Toast.LENGTH_SHORT).show();

                //create intent to go back to Settings
                Intent ImagesActivityToAccountSettingsActivity = new Intent(ImagesActivity.this, AccountSettingsActivity.class);
                startActivity(ImagesActivityToAccountSettingsActivity);

                //kill this activity
                finish();
            }
        });


    }

}