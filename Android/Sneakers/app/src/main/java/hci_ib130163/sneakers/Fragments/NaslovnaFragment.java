package hci_ib130163.sneakers.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import hci_ib130163.sneakers.NavigationDrawerActivity;
import hci_ib130163.sneakers.R;


public class NaslovnaFragment extends Fragment {

    private NavigationDrawerActivity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_naslovna,container,false);
        activity = ((NavigationDrawerActivity) getActivity());
        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_proizvodi);
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburgere_black_24dp);




        return view;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) { // This is the home/back button
            activity.OpenDrawer();
        }

        return super.onOptionsItemSelected(item);
    }
}