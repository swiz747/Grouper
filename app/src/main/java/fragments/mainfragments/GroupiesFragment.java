package fragments.mainfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tritiumlabs.grouper.R;

/**
 * Created by Kyle on 11/21/2016.
 */

public class GroupiesFragment extends Fragment {

    public static final String ITEM_NAME = "itemName";
    public static final String ITEM_RESOURCE_ID = "itemID";
    public static final String FRAG_POSITION = "position";

    ImageView activeFragIcon;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_groupies_frag, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Groupies");

        return view;
    }
}