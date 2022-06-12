package sg.edu.np.mad.madassignment1;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import sg.edu.np.mad.madassignment1.databinding.FragmentSignUpBinding;
import sg.edu.np.mad.madassignment1.ui.home.HomeFragment;
import sg.edu.np.mad.madassignment1.ui.login.LoginFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String TAG = "Sign Up Fragment";
    public String GLOBAL_PREF = "MyPrefs";
    public String MY_USERNAME = "MyUsername";
    public String MY_PASSWORD = "MyPassword";
    private FragmentSignUpBinding binding;
    sg.edu.np.mad.madassignment1.DBHandler dbHandler = new sg.edu.np.mad.madassignment1.DBHandler(getActivity(), null, null, 1);

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        final EditText newUsername = binding.createUsername;
        final EditText newPassword = binding.createPassword;
        final EditText confirmPassword = binding.confirmPassword;
        final Button CreateAccount = binding.signUpButton;
        final Button cancelButton = binding.backToLogin;

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment loginFragment = new LoginFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frameLayout, loginFragment);
                ft.show(loginFragment);
                ft.commit();
            }
        });

        CheckBox tandc = binding.termsAndConditions;
        tandc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tandc.isChecked()) {
                    Toast.makeText(getActivity(), "Agree to Terms and Conditions!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User userDBData = dbHandler.findUser(newUsername.getText().toString());
                if (userDBData == null) {
                    if (newUsername.getText().toString().equals(confirmPassword.getText().toString())) {
                        User userDataDB = new User();
                        userDataDB.setUsername(newUsername.getText().toString());
                        userDataDB.setPassword(confirmPassword.getText().toString());
                        dbHandler.addUser(userDataDB);
                        if (tandc.isChecked()) {
                            Toast.makeText(getActivity(), "Account Created Successfully", Toast.LENGTH_SHORT).show();
                            Fragment loginFragment = new LoginFragment();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.frameLayout, loginFragment);
                            ft.show(loginFragment);
                            ft.commit();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "User already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }
}