package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        Button myButtonSave = findViewById(R.id.SaveBtn2);

        // Navigate back to Account Settings
        myButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create intent to go to Password
                Intent AccountSettingsToPasswordActivity = new Intent(PasswordActivity.this, AccountSettingsActivity.class);

                //put extra
                AccountSettingsToPasswordActivity.putExtra("finisher", new ResultReceiver(null) {
                    @Override
                    //when result code =1, received from bundle, kill this activity
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        PasswordActivity.this.finish();
                    }
                });

                // Display "Saved" msg
                Toast.makeText(PasswordActivity.this, "Saved!", Toast.LENGTH_SHORT).show();

                //start activity with result
                startActivityForResult(AccountSettingsToPasswordActivity,1);
            }
        });
    }
}