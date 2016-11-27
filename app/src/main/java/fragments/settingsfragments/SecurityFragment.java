package fragments.settingsfragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tritiumlabs.grouper.R;

public class SecurityFragment extends Fragment {

    private static final String TAG = "SecurityFragment(Settings)";

    TextView txtSecurity;
    RelativeLayout rlayPrivacy;
    TextView txtViewingPrivacy;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_security_layout, container, false);

        Typeface sab = Typeface.createFromAsset(getActivity().getAssets(), "Sabandija-font-ffp.ttf");

        txtSecurity = (TextView) view.findViewById(R.id.txtSettingsSecurity);
        txtViewingPrivacy = (TextView) view.findViewById(R.id.settingsSecurityViewingPrivacy);
        rlayPrivacy = (RelativeLayout) view.findViewById(R.id.rlaySettingsSecurityViewingPrivacy);

        txtSecurity.setTypeface(sab);
        txtViewingPrivacy.setTypeface(sab);

        rlayPrivacy.setVisibility(View.GONE);

        txtViewingPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_contents();
            }
        });
        return view;
    }

    public void toggle_contents(){
        rlayPrivacy.setVisibility( rlayPrivacy.isShown()
                        ? View.GONE
                        : View.VISIBLE );
    }
}