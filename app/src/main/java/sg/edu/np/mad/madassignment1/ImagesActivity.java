package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        Button myButtonSave = findViewById(R.id.SaveBtn3);
        myButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // shared preferences to store latest username to set profile pic
                SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
                String username = sharedPreferences.getString("username", "");

                User userDBdata = dbHandler.findUser(username);

                //if(document.getElementById('button').clicked == true)

                //if(view.getId(Image1).clicked == true)


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



                Toast.makeText(ImagesActivity.this, "Profile picture successfully changed!", Toast.LENGTH_SHORT).show();
                //create intent to go back to Account Settings
                Intent AccountSettingsToPasswordActivity = new Intent(ImagesActivity.this, AccountSettingsActivity.class);
                startActivity(AccountSettingsToPasswordActivity);
            }
        });


        Image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // shared preferences to store latest username to set profile pic
                SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
                String username = sharedPreferences.getString("username", "");
                User userDBdata = dbHandler.findUser(username);

                userDBdata.setImageID(1);
                dbHandler.updateProfile(userDBdata);
                Toast.makeText(ImagesActivity.this, "Team blue chosen", Toast.LENGTH_SHORT).show();

                //create intent to go back to Settings
                Intent ImagesActivityToAccountSettingsActivity = new Intent(ImagesActivity.this, AccountSettingsActivity.class);
                startActivity(ImagesActivityToAccountSettingsActivity);

                //kill this activity
                finish();
            }
        });


    }


//    public void onCLick(View v){
//        switch(v.getId())
//        {
//            case R.id.Image1:
//                Image1.setImageResource(R.drawable.img1);
//                break;
//        }
//        //create intent to go back to Settings
//        Intent ImagesActivityToAccountSettingsActivity = new Intent(ImagesActivity.this, AccountSettingsActivity.class);
//        startActivity(ImagesActivityToAccountSettingsActivity);
//
//    }
}