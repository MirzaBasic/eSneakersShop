package hci_ib130163.sneakers.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import hci_ib130163.sneakers.Helper.GsonConverter;
import hci_ib130163.sneakers.Models.ProizvodiVM;
import hci_ib130163.sneakers.NavigationDrawerActivity;
import hci_ib130163.sneakers.R;
import hci_ib130163.sneakers.Utils.NetworkUtils;

public class SneakersWomanFragment extends Fragment {
    private TextView mErrorMessage;
    private ProgressBar mProggresBar;
    private ListView mListView;
    private List<ProizvodiVM> mProizvodi;

    private NavigationDrawerActivity activity;
    private TextView mSearchNotFound;
    private  SearchView mSearchQuery;
    private View mSearchView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_proizvodi,container,false);
        activity = ((NavigationDrawerActivity) getActivity());
        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_proizvodi);
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburgere_black_24dp);



        mListView = (ListView) view.findViewById(R.id.list_view_proizvodi);
        mErrorMessage = (TextView) view.findViewById(R.id.textviev_error_message);
        mProggresBar = (ProgressBar) view.findViewById(R.id.proggres_bar);
        mSearchNotFound= (TextView) view.findViewById(R.id.textviev_search_not_found_message);


        mSearchView = (LinearLayout) toolbar.findViewById(R.id.search_layout);
        mSearchQuery = (SearchView) mSearchView.findViewById(R.id.search_view);

        mSearchQuery.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doGetData();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doGetData();
                return true;
            }
        });




        doGetData();
        return view;








    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        super.onCreateOptionsMenu(menu, inflater);


        inflater.inflate(R.menu.main,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) { // This is the home/back button
            activity.OpenDrawer();
        }
        if (id == R.id.action_search) {


            if (mSearchView.getVisibility() == View.GONE) {
                mSearchView.setVisibility(View.VISIBLE);

                mSearchQuery.setIconified(false);
                doGetData();

                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                item.setIcon(R.drawable.ic_close_black_24dp);
            } else {
                mSearchView.setVisibility(View.GONE);
                mSearchQuery.setQuery("", true);
                doGetData();
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                item.setIcon(R.drawable.ic_search_black_24dp);
            }

        }
        return super.onOptionsItemSelected(item);
    }


    private void doGetData() {
        mProggresBar.setVisibility(View.VISIBLE);
        new AsyncTask<URL, Void, String>() {

            @Override

            protected String doInBackground(URL... params) {
                String result="";
                try {
                    result =NetworkUtils.getResponseFromHttpUrl(params[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return result;
            }

            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                mProggresBar.setVisibility(View.INVISIBLE);
                if (data != null) {
                    Type listType = new TypeToken<ArrayList<ProizvodiVM>>() {
                    }.getType();

                    mProizvodi = GsonConverter.JsonToListArray(data, listType);

                    doSetAdapter();
                    if (mProizvodi.size() == 0) {
                        doShowSearchNotFound();
                    } else {
                        doShowData();

                    }

                }
                else{
                    doShowErrorMessage();
                }

            }






        }.execute(NetworkUtils.buildSearchURL(mSearchQuery.getQuery().toString(),2));


    }
    private void doSetAdapter() {

        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mProizvodi.size();
            }

            @Override
            public Object getItem(int position) {
                return mProizvodi.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                final ProizvodiVM proizvod = mProizvodi.get(position);


                if(view==null){
                    final LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view=inflater.inflate(R.layout.proizvodi_list_view,parent,false);
                }
                final TextView naziv = (TextView) view.findViewById(R.id.tvNaziv);

                final TextView cijena = (TextView) view.findViewById(R.id.tvCijena);

                ImageView imageProizvod = (ImageView) view.findViewById(R.id.imageProizvod);
                RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
                ratingBar.setRating((float) proizvod.Ocjena);






                naziv.setText(proizvod.Naziv);

                cijena.setText(proizvod.Cijena+" KM");
                if(proizvod.Slika!=null) {

                    Bitmap decodedByte = BitmapFactory.decodeByteArray(proizvod.Slika, 0, proizvod.Slika.length);
                    imageProizvod.setImageBitmap(decodedByte);
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle arg=new Bundle();
                        arg.putSerializable("proizvod_key",proizvod);
                        DetaljiFragment fragment=new DetaljiFragment();
                        fragment.setArguments(arg);
                        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();

                        ft.replace(R.id.content,fragment);
                        ft.commit();
                    }
                });
                return view;
            }
        });
    }






    public void doShowErrorMessage() {
        mSearchNotFound.setVisibility(View.INVISIBLE);
        mListView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }
    public void doShowSearchNotFound() {

        mSearchNotFound.setVisibility(View.VISIBLE);
    }
    public void doShowData() {
        mSearchNotFound.setVisibility(View.INVISIBLE);
        mListView.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
    }
}
