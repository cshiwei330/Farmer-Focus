package sg.edu.np.mad.madassignment1;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BarnFragment extends Fragment {

    // Store instance variables
    private String title;
    private int page;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView barnTaskPopUpTitle;
    private RecyclerView barnTaskPopUpRecyclerView;

    public static BarnFragment newInstance(int page, String title) {
        BarnFragment barnFragment = new BarnFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        barnFragment.setArguments(args);
        return barnFragment;
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
        View view = inflater.inflate(R.layout.fragment_barn, container, false);
        TextView tvLabel = (TextView) view.findViewById(R.id.BarnFragmentTextView);
        tvLabel.setText(page + " -- " + title + "THIS IS BARN");

        ImageView barnImage = (ImageView) view.findViewById(R.id.BarnImageView);
        barnImage.setClickable(true);
        barnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),
                        "BARN IMAGE CLICKED",
                        Toast.LENGTH_LONG).show();
                createBarnTaskPopUp();
            }
        });

        return view;
    }

    public void createBarnTaskPopUp(){
        dialogBuilder = new AlertDialog.Builder(this.getContext());
        final View BarnTaskPopupView = getLayoutInflater().inflate(R.layout.popup_barn_tasks, null);

        barnTaskPopUpTitle = (TextView) BarnTaskPopupView.findViewById(R.id.barnTaskPopUpTitle);
        barnTaskPopUpRecyclerView = (RecyclerView) BarnTaskPopupView.findViewById(R.id.barnTaskPopUpRecyclerView);

        dialogBuilder.setView(BarnTaskPopupView);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}