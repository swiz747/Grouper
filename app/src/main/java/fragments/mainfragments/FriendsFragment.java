package fragments.mainfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tritiumlabs.grouper.MyService;
import com.tritiumlabs.grouper.R;
import com.tritiumlabs.grouper.adapters.FriendslistAdapter;

import fragments.ChatsFragment;

public class FriendsFragment extends Fragment {

    public static final String ITEM_NAME = "itemName";
    public static final String ITEM_RESOURCE_ID = "itemID";
    public static final String FRAG_POSITION = "position";

    public static FriendslistAdapter friendslistAdapter;
    ListView lstView_Friends;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friendslist_layout, container, false);

        lstView_Friends = (ListView) view.findViewById(R.id.lstView_Friends);

        // ----Set autoscroll of listview when a new message arrives----//
        lstView_Friends.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lstView_Friends.setStackFromBottom(true);

        lstView_Friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView friendView = (TextView) view.findViewById(R.id.friend_name);
                String friend = friendView.getText().toString();

                Log.d("item click:", position + " " + id + " item:" + friend);
                //TODO Create chat fragment here pass in unique chat ID -AB
                Bundle args = new Bundle();
                //friend = friend.substring(0, friend.indexOf("@"));
                args.putString("friendName", friend);
                //args.putString("friendID", friend);
                Fragment toFragment = new ChatsFragment();
                toFragment.setArguments(args);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragContainer, toFragment, "chats")
                        .addToBackStack("chats").commit();
            }
        });

        lstView_Friends.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                TextView friendView  = (TextView)view.findViewById(R.id.friend_status);
                String friend = friendView.getText().toString();

                Log.d("item click:",position+" " + id +" item:" + friend.toString());

                return true;

            }
        });

        Log.d("Friendslist","about to get roster");

        friendslistAdapter = new FriendslistAdapter(getActivity(), MyService.xmpp.getRoster());

        lstView_Friends.setAdapter(friendslistAdapter);
        friendslistAdapter.notifyDataSetChanged();
        Log.d("Friendslist","Trust me the friendslist was created and in theory the layout was inflated");

        return view;
    }
}
