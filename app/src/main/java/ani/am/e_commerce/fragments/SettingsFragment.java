package ani.am.e_commerce.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;

import ani.am.e_commerce.R;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.settings));
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

}
