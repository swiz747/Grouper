package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tritiumlabs.grouper.R;
import com.tritiumlabs.grouper.adapters.FriendslistAdapter;

public class FriendsListFragment extends Fragment {

    String friendString = "";
    public static FriendslistAdapter friendslistAdapter;
    ListView lstView_Friends;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friendslist_layout, container, false);

        lstView_Friends = (ListView) view.findViewById(R.id.lstView_Friends);

        // ----Set autoscroll of listview when a new message arrives----//
        lstView_Friends.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lstView_Friends.setStackFromBottom(true);

        return view;
    }
}
