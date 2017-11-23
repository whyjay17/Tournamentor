package ykim164cs242.tournamentor.Fragments.Client;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ykim164cs242.tournamentor.R;

/**
 * Fragment for the scorer table. To be implemented in Week 2
 */
public class ScorerFragment extends Fragment {


    public ScorerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scorer, container, false);
    }

}
