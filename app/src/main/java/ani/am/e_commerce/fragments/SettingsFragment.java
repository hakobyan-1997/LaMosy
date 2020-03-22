package ani.am.e_commerce.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import ani.am.e_commerce.BuildConfig;
import ani.am.e_commerce.Global;
import ani.am.e_commerce.R;
import ani.am.e_commerce.activities.BaseActivity;

import static ani.am.e_commerce.activities.MainActivity.prefConfig;

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
        Spinner languageSpinner = view.findViewById(R.id.spinner_language);
        TextView appVersion = view.findViewById(R.id.app_version_tv);

        appVersion.setText(context.getString(R.string.app_version, BuildConfig.VERSION_NAME));

        String[] spinnerArray = {getString(R.string.english), getString(R.string.armenian)};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        languageSpinner.setAdapter(spinnerArrayAdapter);

        switch (prefConfig.getLang()) {
            case "en":
                languageSpinner.setSelection(0);
                break;
            case "hy":
                languageSpinner.setSelection(1);
                break;
        }

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Intent intent = new Intent(getActivity(), BaseActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                if (position == 0 && !prefConfig.getLang().equals("en")) {
                    Global.setLocaleLanguage(context, "en");
                    prefConfig.setLang("en");
                    context.startActivity(intent);
                } else if (position == 1 && !prefConfig.getLang().equals("hy")) {
                    Global.setLocaleLanguage(context, "hy");
                    prefConfig.setLang("hy");
                    context.startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }
}
