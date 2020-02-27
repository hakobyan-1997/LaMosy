package ani.am.e_commerce.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import ani.am.e_commerce.BuildConfig;
import ani.am.e_commerce.R;

public class SettingsFragment extends Fragment {
    private View view;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.settings));
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        init();
        return view;
    }

    private void init() {
        Switch darkModeSwitch = view.findViewById(R.id.swich_dark_mode);
        Spinner languageSpinner = view.findViewById(R.id.spinner_language);
        TextView appVersion = view.findViewById(R.id.app_version_tv);

        appVersion.setText(context.getString(R.string.app_version, BuildConfig.VERSION_NAME));

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("Tag", "switch change " + b);
            }
        });

        String[] spinnerArray = {"English", "Armenian"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        languageSpinner.setAdapter(spinnerArrayAdapter);
    }
}
