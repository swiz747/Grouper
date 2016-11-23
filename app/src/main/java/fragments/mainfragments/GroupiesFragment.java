package fragments.mainfragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.tritiumlabs.grouper.GroupieActivity;
import com.tritiumlabs.grouper.R;

public class GroupiesFragment extends Fragment {

    public static final String ITEM_NAME = "itemName";
    public static final String ITEM_RESOURCE_ID = "itemID";
    public static final String FRAG_POSITION = "position";

    ImageView activeFragIcon;
    Button btnCreateGroupie;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_groupies_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Groupies");

        btnCreateGroupie = (Button)view.findViewById(R.id.btnDatePicker);

        btnCreateGroupie.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCreateGroupiesFragment();
                    }
                }
        );
        return view;
    }

    private void openCreateGroupiesFragment() {
        Intent intent = new Intent(getActivity(), GroupieActivity.class);
        intent.putExtra("action", "create");
        startActivity(intent);
    }
}