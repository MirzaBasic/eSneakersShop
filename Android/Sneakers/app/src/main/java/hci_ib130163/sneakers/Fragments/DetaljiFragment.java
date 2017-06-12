package hci_ib130163.sneakers.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import hci_ib130163.sneakers.Helper.Global;
import hci_ib130163.sneakers.Helper.GsonConverter;
import hci_ib130163.sneakers.Helper.MyApp;

import hci_ib130163.sneakers.Helper.Sesija;

import hci_ib130163.sneakers.Models.KomentariVM;
import hci_ib130163.sneakers.Models.NaruzbeVM;

import hci_ib130163.sneakers.Models.ProizvodiVM;
import hci_ib130163.sneakers.Models.StavkeNarudzbeVM;
import hci_ib130163.sneakers.NavigationDrawerActivity;
import hci_ib130163.sneakers.R;
import hci_ib130163.sneakers.Utils.NetworkUtils;

/**
 * Created by Developer on 08.06.2017..
 */
public class DetaljiFragment extends Fragment {

    private NavigationDrawerActivity activity;
    ProizvodiVM proizvod;
    protected View view;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     view   = inflater.inflate(R.layout.fragment_detalji_proizvoda, container, false);
        activity = ((NavigationDrawerActivity) getActivity());

        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_proizvodi);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburgere_black_24dp);

        Bundle arg = getArguments();
        if (arg.containsKey("proizvod_key")) {

            proizvod = (ProizvodiVM) arg.getSerializable("proizvod_key");


        }

        RatingBar ocijeni= (RatingBar) view.findViewById(R.id.ratingBar);

        TextView naziv = (TextView) view.findViewById(R.id.tvNaziv);

        TextView cijena = (TextView) view.findViewById(R.id.tvCijena);
        TextView opis = (TextView) view.findViewById(R.id.tvOpis);
        TextView sifra = (TextView) view.findViewById(R.id.tvSifra);

        ImageView image = (ImageView) view.findViewById(R.id.imageDetalji);

        naziv.setText(proizvod.Naziv);
        sifra.setText("Sifra: " + proizvod.Sifra);
        cijena.setText(String.valueOf(proizvod.Cijena) + " KM");
        opis.setText("Opis: " + String.valueOf(proizvod.Opis));

        if (proizvod.Slika != null) {

            Bitmap decodedByte = BitmapFactory.decodeByteArray(proizvod.Slika, 0, proizvod.Slika.length);
            image.setImageBitmap(decodedByte);

        }

        Button btnKorpa = (Button) view.findViewById(R.id.btnKupi);
        btnKorpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                do_UKorpu();

            }
        });


        doGetKomentari();

        ocijeni.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                new AsyncTask<URL, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(URL... params) {
                        Boolean success=true;
                        try {
                            NetworkUtils.getResponseFromHttpUrl(params[0]);
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


                            Toast.makeText(MyApp.getContext(),"Uspjesno ste ocijenili proizvod!",Toast.LENGTH_SHORT).show();

                        }
                        else{

                            Toast.makeText(MyApp.getContext(),"Već ste ocijenili ovaj proizvod!",Toast.LENGTH_SHORT).show();

                        }
                    }
                }.execute(NetworkUtils.buildOcijeniProizvodURL(proizvod.Id,rating));



            }
        });
        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            activity.OpenDrawer();
        }


        return super.onOptionsItemSelected(item);
    }


    private void doGetKomentari() {
        new AsyncTask<URL, Void, String>() {
            @Override
            protected String doInBackground(URL... params) {
                String result="";
                try {
                    result= NetworkUtils.getResponseFromHttpUrl(params[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                    result=null;
                }
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s!=null){
                    Type listType = new TypeToken<ArrayList<KomentariVM>>() {
                    }.getType();
                 List<KomentariVM> komentari= GsonConverter.JsonToListArray(s,listType);



                    doSetAdapter(komentari);
                }

            }
        }.execute(NetworkUtils.buildKomentariByProizvodIdURL(proizvod.Id));


    }

    private void do_btnAddCommentClick(String comment) {

        final KomentariVM komentar=new KomentariVM();
        komentar.Sadrzaj=comment;
        komentar.KorisnikId=Sesija.GetUser().Id;
        komentar.ProizvodId=proizvod.Id;

        new AsyncTask<URL, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(URL... params) {
                Boolean success=true;
                try {
                     NetworkUtils.postResponseToHttpUrl(params[0],GsonConverter.ObjectToJson(komentar));
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

                        doGetKomentari();
                }
                    else{

                    Toast.makeText(MyApp.getContext(),"Došlo je do greške!",Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(NetworkUtils.buildAddKomentarURL());

    }

    private void doSetAdapter(final List<KomentariVM> komentari) {
        komentari.add(new KomentariVM());

        final ListView listKomentari= (ListView) view.findViewById(R.id.listview_komentari);
        listKomentari.setFocusable(false);



        listKomentari.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return komentari.size();
            }

            @Override
            public Object getItem(int position) {
                return komentari.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {



                if((position+1)==komentari.size()){

                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view=inflater.inflate(R.layout.add_komentar,parent,false);


                    ImageButton btnAddComment= (ImageButton) view.findViewById(R.id.btnAddComment);
                    final EditText txtComent = (EditText) view.findViewById(R.id.txtComment);

                    btnAddComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            do_btnAddCommentClick(txtComent.getText().toString());
                        }
                    });




                }
                else {

                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view=inflater.inflate(R.layout.komentari_list_view,parent,false);

                    final TextView komentarTxt = (TextView) view.findViewById(R.id.tvKomentar);
                    final TextView username = (TextView) view.findViewById(R.id.tvUsernameKomentara);
                    final TextView datum = (TextView) view.findViewById(R.id.tvDatumKreiranjaKomentara);
                    komentarTxt.setText(komentari.get(position).Sadrzaj);
                    username.setText(" " + komentari.get(position).Korisnik);
                    datum.setText("on " + komentari.get(position).DatumKreiranja);
                }


                return view;
            }
        });




    }


    private void do_UKorpu() {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        final SeekBar seek = new SeekBar(activity);
        seek.setProgress(1);
        seek.setMax(10);
        alertDialog.setMessage("Količina: 1/10");
        seek.setKeyProgressIncrement(1);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {

                    seek.setProgress(1);
                }
                alertDialog.setMessage("Količina: " + progress + "/10");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }


        });


        alertDialog.setView(seek);
        alertDialog.setTitle("Odaberite željenu količinu");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "POTVRDI",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        boolean proizvodVecUKorpi = false;
                        if (Global.Korpa == null) {
                            Global.Korpa = new NaruzbeVM();
                            Global.Korpa.Stavke = new ArrayList<StavkeNarudzbeVM>();

                        }

                        for (int i = 0; i < Global.Korpa.Stavke.size(); i++) {

                            if (Global.Korpa.Stavke.get(i).ProizvodId == proizvod.Id) {

                                Global.Korpa.Stavke.get(i).Kolicina += seek.getProgress();
                                Global.Korpa.UkupanIznos += proizvod.Cijena * seek.getProgress();

                                proizvodVecUKorpi = true;
                                Toast.makeText(MyApp.getContext(), "Uspješno izmijenjena količina", Toast.LENGTH_SHORT).show();
                                break;


                            }
                        }
                        if (!proizvodVecUKorpi) {
                            StavkeNarudzbeVM stavka = new StavkeNarudzbeVM();

                            stavka.Kolicina = seek.getProgress();
                            stavka.ProizvodId = proizvod.Id;
                            stavka.Proizvod = new ProizvodiVM();
                            stavka.Proizvod.Naziv = proizvod.Naziv;
                            stavka.Proizvod.Slika=proizvod.Slika;
                            stavka.Proizvod.Cijena = proizvod.Cijena;
                            Global.Korpa.Stavke.add(stavka);
                            Global.Korpa.UkupanIznos += proizvod.Cijena * seek.getProgress();
                            Toast.makeText(getContext(), "Uspješno dodan proizvod u korpu", Toast.LENGTH_SHORT).show();
                        }

                    }


                });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ODUSTANI",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();


    }
}










