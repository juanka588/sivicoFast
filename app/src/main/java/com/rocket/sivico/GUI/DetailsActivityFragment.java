package com.rocket.sivico.GUI;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.Report;
import com.rocket.sivico.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_details, container, false);
        Bundle bundle = getArguments();
        Report report = bundle.getParcelable(GlobalConfig.PARAM_REPORT);
        Toast.makeText(getContext(),report.getDescription(),Toast.LENGTH_LONG).show();
        return inflate;
    }
}
