package hci_ib130163.sneakers.Activitys;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

import hci_ib130163.sneakers.Helper.Global;
import hci_ib130163.sneakers.Helper.GsonConverter;
import hci_ib130163.sneakers.Helper.MyApp;
import hci_ib130163.sneakers.Helper.Sesija;
import hci_ib130163.sneakers.Models.StavkeNarudzbeVM;
import hci_ib130163.sneakers.R;
import hci_ib130163.sneakers.Utils.NetworkUtils;


public class KorpaActivity extends AppCompatActivity {

    private ListView mListView;



    private TextView mPraznaKorpa;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korpa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mListView = (ListView)findViewById(R.id.list_view_proizvodi);

        mPraznaKorpa = (TextView) findViewById(R.id.tv_empty_basket_message);

        doGetData();
        Button zakljuci= (Button) findViewById(R.id.btn_zakljuci);
        zakljuci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Global.Korpa!=null&& Sesija.GetUser()!=null){
                    for (int i=0;i<Global.Korpa.Stavke.size();i++){

                        Global.Korpa.Stavke.get(i).Proizvod=null;
                    }
                    Global.Korpa.KorisnikId= Sesija.GetUser().Id;
                    new AsyncTask<URL, Void, Boolean>() {
                        @Override
                        protected Boolean doInBackground(URL... params) {
                            boolean response=true;
                            try {
                                NetworkUtils.postResponseToHttpUrl(params[0], GsonConverter.ObjectToJson(Global.Korpa));
                            } catch (IOException e) {
                                e.printStackTrace();
                                response=false;

                            }
                            return response;
                        }

                        @Override
                        protected void onPostExecute(Boolean success) {
                            super.onPostExecute(success);
                            if(success){
                                Global.Korpa=null;
                                mListView.setAdapter(null);
                                doGetData();
                                Toast.makeText(MyApp.getContext(), "Uspješno izvršena narudžba", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MyApp.getContext(), "Došlo je do greške!", Toast.LENGTH_SHORT).show();


                            }

                        }
                    }.execute(NetworkUtils.buildNarudzbaAddURL());

                }



            }
        });





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.korpa,menu);
        return super.onCreateOptionsMenu(menu);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_isprazni) { // This is the home/back button
            Global.Korpa=null;
            mListView.setAdapter(null);
            doGetData();
        }
        return super.onOptionsItemSelected(item);
    }

    private void doGetData() {
        if (Global.Korpa == null){

            mPraznaKorpa.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.INVISIBLE);
        }
        else{


            doSetAdapter();
        }




    }
    private void doSetAdapter() {

        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return Global.Korpa.Stavke.size();
            }

            @Override
            public Object getItem(int position) {
                return Global.Korpa.Stavke.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                final StavkeNarudzbeVM stavka = Global.Korpa.Stavke.get(position);


                if(view==null){
                    final LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view=inflater.inflate(R.layout.korpa_list_view,parent,false);
                }
                final ImageView imageProizvod = (ImageView) view.findViewById(R.id.tvImage);
                final TextView kolicina = (TextView) view.findViewById(R.id.tvKolicina);
                final TextView cijena = (TextView) view.findViewById(R.id.tvCijena);







                kolicina.setText("Kom: "+stavka.Kolicina);

                if(stavka.Proizvod.Popust!=0){
                    cijena.setText(String.valueOf(stavka.Proizvod.Cijena*stavka.Proizvod.Popust/100)+" KM");
                }
                else{
                    cijena.setText(String.valueOf(stavka.Proizvod.Cijena)+" KM");
                }

                if(stavka.Proizvod.Slika!=null) {

                    Bitmap decodedByte = BitmapFactory.decodeByteArray(stavka.Proizvod.Slika, 0, stavka.Proizvod.Slika.length);
                    imageProizvod.setImageBitmap(decodedByte);
                }




                return view;
            }
        });
    }


}



