package ykim164cs242.tournamentor.Fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ykim164cs242.tournamentor.ListItem.MatchListItem;
import ykim164cs242.tournamentor.Adapter.MatchListAdapter;
import ykim164cs242.tournamentor.R;

public class MatchListTab extends Fragment {

    ListView matchListView;

    private MatchListAdapter adapter;
    private List<MatchListItem> matchListItems;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_match_list, container, false);
        matchListView = (ListView) view.findViewById(R.id.match_list);

        matchListItems = new ArrayList<>();

        adapter = new MatchListAdapter(getContext(), matchListItems);
        matchListView.setAdapter(adapter);

        for(int i = 0; i < 3; i++) {
            matchListItems.add(new MatchListItem(i, "Field A - FAR", "FT", "Nov 11", "AS Dreuoit", "FC Gwang", 3, 2, false, false));
        }

        return view;

    }

}
