package sg.edu.np.mad.madassignment1;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.BreakIterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountSettingsFragment extends Fragment {

    public String getString;
    public static final String SHARED_PREF = "shared";
    public static final String TEXT = "text";
    public String text;

//    public static final String PREFERENCES = "preferences";
//    public static final String CUSTOM_THEME = "customTheme";
//    public static final String LIGHT_THEME = "lightTheme";
//    public static final String DARK_THEME = "darkTheme";
//
//    private String customTheme;
//
//    public String getCustomTheme() {
//        return customTheme;
//    }
//
//    public void setCustomTheme(String customTheme) {
//        this.customTheme = customTheme;
//    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountSettingsFragment newInstance(String param1, String param2) {
        AccountSettingsFragment fragment = new AccountSettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);
        Button myButtonSave = (Button) view.findViewById(R.id.SaveBtn);
        TextView myUserName = (TextView) view.findViewById(R.id.UserName);
        TextView editText = (TextView) view.findViewById(R.id.EditAccountUsername);
        TextView myPassword = (TextView) view.findViewById(R.id.ChangePass);

        // Navigate to password fragment
        myPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_AccountSettings_to_nav_Password);
            }
        });

        // Navigate back to settings fragment
        myButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getString = editText.getText().toString();
                myUserName.setText(getString);

                // shared preferences
                Log.v("front", "test");
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TEXT, myUserName.getText().toString());
                editor.apply();

                // Once saved, go back to Settings page
                Navigation.findNavController(view).navigate(R.id.action_nav_AccountSettings_to_nav_Settings); //call the action
                // Display "Saved"
                Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
            }



        });
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");
        myUserName.setText(text);
        return view;
    }

}