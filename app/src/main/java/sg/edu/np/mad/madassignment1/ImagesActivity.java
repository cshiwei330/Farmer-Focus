package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ImagesActivity extends AppCompatActivity {
    public String GLOBAL_PREF = "MyPrefs";
    public String getString;




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

        Button myButtonSave = findViewById(R.id.SaveBtn3);
        myButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //User userDBData = dbHandler.findUser(myEditUsername.getText().toString());
            }
        });


//        Image1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
////                switch(view.getId())
////                {
////                    case R.id.Image1:
////                        myProfilePic.setImageResource(R.drawable.img1);
////                        break;
////                }
//
//                //create intent to go back to Settings
//                Intent ImagesActivityToAccountSettingsActivity = new Intent(ImagesActivity.this, AccountSettingsActivity.class);
//                startActivity(ImagesActivityToAccountSettingsActivity);
//
//            }
//        });


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