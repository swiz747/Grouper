package fragments.settingsfragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tritiumlabs.grouper.R;
import com.tritiumlabs.grouper.adapters.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kyle on 11/27/2016.
 */

public class SecurityFragment extends Fragment {

    private static final String TAG = "SecurityFragment(Settings)";
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    TextView txtSecurity;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_security_layout, container, false);

        Typeface sab = Typeface.createFromAsset(getActivity().getAssets(), "Sabandija-font-ffp.ttf");

        txtSecurity = (TextView) view.findViewById(R.id.txtSettingsSecurity);
        txtSecurity.setTypeface(sab);

        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        prepareListData();
        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild, "Sabandija-font-ffp.ttf");
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getActivity().getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        return view;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Viewing Privacy");
        listDataHeader.add("Friend Request");

        List<String> viewingPrivacy = new ArrayList<String>();
        viewingPrivacy.add("Public");
        viewingPrivacy.add("Friends");

        List<String> friendRequest = new ArrayList<String>();
        friendRequest.add("Public");
        friendRequest.add("Friends of Friends");

        listDataChild.put(listDataHeader.get(0), viewingPrivacy);
        listDataChild.put(listDataHeader.get(1), friendRequest);
    }
}