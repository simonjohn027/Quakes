package com.driven.quakes;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
/**
 * A simple {@link Fragment} subclass.
 */
public class QuakeListFragment extends Fragment {

    ArrayList<Quake>quakeArrayList = new ArrayList<Quake>();
    private String[] names;
    private String[] location;
    private float[] magnitude;
    private String[] depth;
    private Date[] date;

    public QuakeListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView quakeListRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_quakelist, container, false);
        QuakeListActivity activity = (QuakeListActivity)getActivity();
        quakeArrayList = activity.arrayList();

        if(quakeArrayList.size() != 0){
            int size = quakeArrayList.size();
            names = new String[size];
            location = new String[size];
            magnitude = new float[size];
            depth = new String[size];
            date = new Date[size];

            int iter = 0;

            for (Quake quake : quakeArrayList) {
                names[iter] = quake.getName();
                location[iter] = quake.getLocation();
                magnitude[iter] = quake.getMagnitude();
                depth[iter] = quake.getDepth();
                date[iter] = quake.getDate();
                iter++;
            }
            QuakeCardAdapter adapter = new QuakeCardAdapter(names, location, magnitude, depth,date);
            quakeListRecycler.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            quakeListRecycler.setLayoutManager(layoutManager);
            return quakeListRecycler;
        }
        QuakeCardAdapter adapter = new QuakeCardAdapter(names, location, magnitude, depth,date);

        adapter.setListener(new QuakeCardAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(),QuakeDetailActivity.class);
                intent.putExtra("quake",quakeArrayList);
                intent.putExtra(QuakeDetailActivity.EXTRA_QUAKE_ID, position);
                getActivity().startActivity(intent);
                System.out.println("CLickeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeed");
            }
        });
        quakeListRecycler.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        quakeListRecycler.setLayoutManager(layoutManager);

        return quakeListRecycler;

    }



}
