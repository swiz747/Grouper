package fragments.settingsfragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.tritiumlabs.grouper.R;
import com.tritiumlabs.grouper.VisualEffects;

public class SecurityFragment extends Fragment {

    private static final String TAG = "SecurityFragment(Settings)";

    TextView txtSecurity;
    TextView txtViewingPrivacy;
    TextView txtFriendRequest;
    RelativeLayout rlayPrivacy;
    RelativeLayout rlayFriendRequest;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_security_layout, container, false);

        Typeface sab = Typeface.createFromAsset(getActivity().getAssets(), "Sabandija-font-ffp.ttf");

        txtSecurity = (TextView) view.findViewById(R.id.txtSettingsSecurity);
        txtViewingPrivacy = (TextView) view.findViewById(R.id.settingsSecurityViewingPrivacy);
        txtFriendRequest = (TextView) view.findViewById(R.id.settingsSecurityFriendRequest);
        rlayPrivacy = (RelativeLayout) view.findViewById(R.id.rlaySettingsSecurityViewingPrivacy);
        rlayFriendRequest = (RelativeLayout) view.findViewById(R.id.rlaySettingsSecurityFriendRequest);

        txtSecurity.setTypeface(sab);
        txtViewingPrivacy.setTypeface(sab);
        txtFriendRequest.setTypeface(sab);

        rlayPrivacy.setVisibility(View.GONE);
        rlayFriendRequest.setVisibility(View.GONE);

        txtViewingPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleContentsPrivacy();
            }
        });

        txtFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleContentsFriendRequest();
            }
        });

        return view;
    }

    public void toggleContentsPrivacy(){
        rlayPrivacy.setVisibility( rlayPrivacy.isShown()
                ? View.GONE
                : View.VISIBLE );
    }

    public void toggleContentsFriendRequest(){
        rlayFriendRequest.setVisibility( rlayFriendRequest.isShown()
                ? View.GONE
                : View.VISIBLE );
    }
}