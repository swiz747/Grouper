package fragments.settingsfragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tritiumlabs.grouper.R;

public class DefaultFragment extends Fragment {

    private static final String TAG = "DefaultFragment(Settings)";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_default_layout, container, false);

        ImageView fishImage = (ImageView) view.findViewById(R.id.imgFishBlink);
        fishImage.setBackgroundResource(R.drawable.fish_blink_animation);
        AnimationDrawable fishBlinkAnimation = (AnimationDrawable) fishImage.getBackground();
        fishBlinkAnimation.start();

        return view;
    }
}
