package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ImagesActivity extends AppCompatActivity {

    public String GLOBAL_PREF = "MyPrefs";
    //SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
    String username;
    User userDBdata;
    DBHandler dbHandler;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        //define dbHandler
        dbHandler = new DBHandler(this, null, null,6);

        ImageView Image1 = findViewById(R.id.Image1);
        ImageView Image2 = findViewById(R.id.Image2);
        ImageView Image3 = findViewById(R.id.Image3);
        ImageView Image4 = findViewById(R.id.Image4);
        ImageView Default = findViewById(R.id.ProfilePic);

        // shared preferences to store latest username to set profile pic
        sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
        username = sharedPreferences.getString("username", "");
        userDBdata = dbHandler.findUser(username);

        Image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClicked(1);
            }
        });

        Image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClicked(2);
            }
        });

        Image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClicked(3);
            }
        });

        Image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClicked(4);
            }
        });
    }

    public void imageClicked(int imageID){

        //kill AccountSettings
        ((ResultReceiver)getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

        userDBdata.setImageID(imageID);
        dbHandler.updateProfile(userDBdata);
        Toast.makeText(ImagesActivity.this, "Team" + imageID + " chosen!", Toast.LENGTH_SHORT).show();

        //creating shared preferences to store a value if user wants to be remembered or not
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("imageID", String.valueOf(imageID));
        editor.apply();

        //create intent to go back to Settings
        Intent ImagesActivityToAccountSettingsActivity = new Intent(ImagesActivity.this, AccountSettingsActivity.class);
        startActivity(ImagesActivityToAccountSettingsActivity);

        //kill this activity
        finish();
    }

}