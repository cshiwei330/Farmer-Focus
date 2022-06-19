package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ImagesActivity extends AppCompatActivity {

    public String getString;

    public static final String SHARED_PREF = "shared";
    //public static final ImageView IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        ImageView Image1 = (ImageView) findViewById(R.id.Image1);
        Image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Image1.replace(R.id.ProfilePic);
                // shared preferences
                Log.v("SP", "test");
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.apply();

            }
        });


    }
}