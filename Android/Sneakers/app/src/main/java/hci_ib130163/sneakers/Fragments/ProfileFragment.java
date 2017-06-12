package hci_ib130163.sneakers.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

import hci_ib130163.sneakers.Helper.GsonConverter;
import hci_ib130163.sneakers.Helper.MyApp;
import hci_ib130163.sneakers.Helper.Sesija;
import hci_ib130163.sneakers.Models.KorisniciVM;
import hci_ib130163.sneakers.NavigationDrawerActivity;
import hci_ib130163.sneakers.R;
import hci_ib130163.sneakers.Utils.NetworkUtils;
import hci_ib130163.sneakers.NavigationDrawerActivity;

/**
 * Created by Developer on 07.06.2017..
 */

public class ProfileFragment extends Fragment {
    private NavigationDrawerActivity activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        activity= ((NavigationDrawerActivity) getActivity());
        setHasOptionsMenu(true);
        Toolbar mAppBar= (Toolbar) view.findViewById(R.id.toolbar_proizvodi);
        mAppBar.setVisibility(View.VISIBLE);
        activity.setSupportActionBar(mAppBar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburgere_black_24dp);






        Button btnSaveInfo = (Button) view.findViewById(R.id.btnSaveInfo);
        btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_btnSaveChanges_Click(view);
            }
        });



        do_setInfo(view);


        return view;
    }





    private void do_btnSaveChanges_Click(final View view) {


        final EditText firstName = (EditText) view.findViewById(R.id.txtFirstName);
        final EditText lastName = (EditText) view.findViewById(R.id.txtLastName);
        final EditText username = (EditText) view.findViewById(R.id.txtUsername);
        final EditText email = (EditText) view.findViewById(R.id.txtEmail);
        final EditText password = (EditText) view.findViewById(R.id.txtPassword);

        final KorisniciVM korisnik=new KorisniciVM();
        korisnik.Id= Sesija.GetUser().Id;
        korisnik.Ime=firstName.getText().toString();
        korisnik.Prezime=lastName.getText().toString();
        korisnik.Username=username.getText().toString();
        korisnik.Email=email.getText().toString();
        korisnik.Password=password.getText().toString();


        new AsyncTask<URL, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(URL... params) {
                Boolean success=true;
                try {
                    NetworkUtils.postResponseToHttpUrl(params[0], GsonConverter.ObjectToJson(korisnik));
                } catch (IOException e) {
                    e.printStackTrace();
                    success=false;
                }
                return success;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);

                if(success){

                    Sesija.SaveUser(korisnik);
                    Toast.makeText(MyApp.getContext(),"Podaci uspješno izmijenjeni",Toast.LENGTH_SHORT).show();


                }
else{

                    Toast.makeText(MyApp.getContext(),"Došlo je do greške",Toast.LENGTH_SHORT).show();
                }


            }
        }.execute(NetworkUtils.buildPostKorisnikURL(korisnik));

    }



    private void do_setInfo(View view) {

        EditText firstName = (EditText) view.findViewById(R.id.txtFirstName);
        EditText lastName = (EditText) view.findViewById(R.id.txtLastName);
        EditText username = (EditText) view.findViewById(R.id.txtUsername);
        EditText password = (EditText) view.findViewById(R.id.txtPassword);
        EditText email = (EditText) view.findViewById(R.id.txtEmail);

        firstName.setText(Sesija.GetUser().Ime);
        lastName.setText(Sesija.GetUser().Prezime);
        username.setText(Sesija.GetUser().Username);
        password.setText(Sesija.GetUser().Password);
        email.setText(Sesija.GetUser().Email);


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
