package sg.edu.np.mad.madassignment1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SiloFragment extends Fragment {

    // Store instance variables
    private String title;
    private int page;

    public static SiloFragment newInstance(int page, String title) {
        SiloFragment siloFragment = new SiloFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        siloFragment.setArguments(args);
        return siloFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_silo, container, false);
        TextView tvLabel = (TextView) view.findViewById(R.id.SiloFragmentTextView);
        tvLabel.setText(page + " -- " + title);
        return view;
    }
}