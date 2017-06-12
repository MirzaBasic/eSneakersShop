package hci_ib130163.sneakers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import hci_ib130163.sneakers.Activitys.KorpaActivity;
import hci_ib130163.sneakers.Activitys.NaruzbeActivity;
import hci_ib130163.sneakers.Fragments.NaslovnaFragment;
import hci_ib130163.sneakers.Fragments.ProfileFragment;
import hci_ib130163.sneakers.Fragments.SneakersChildrensFragment;
import hci_ib130163.sneakers.Fragments.SneakersManFragment;
import hci_ib130163.sneakers.Fragments.SneakersWomanFragment;
import hci_ib130163.sneakers.Helper.Sesija;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);


       mDrawer= (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NaslovnaFragment fragment=new NaslovnaFragment();
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content,fragment).commit();



    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


         if (id == R.id.nav_man) {


             SneakersManFragment fragment=new SneakersManFragment();
             FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
             ft.replace(R.id.content,fragment).commit();




         }
        if (id == R.id.nav_woman) {


            SneakersWomanFragment fragment=new SneakersWomanFragment();
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content,fragment).commit();




        }
        if (id == R.id.nav_korpa) {

            Intent intent=new Intent(NavigationDrawerActivity.this, KorpaActivity.class);
            startActivity(intent);




        }
        if (id == R.id.nav_narudzbe) {

            Intent intent=new Intent(NavigationDrawerActivity.this,NaruzbeActivity.class);
            startActivity(intent);




        }
        if (id == R.id.nav_children) {


            SneakersChildrensFragment fragment=new SneakersChildrensFragment();
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content,fragment).commit();




        }
        if (id == R.id.nav_profil) {


            ProfileFragment fragment=new ProfileFragment();
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content,fragment).commit();




        }
        if (id == R.id.nav_profil) {

        }
        if (id == R.id.nav_logout) {

            Sesija.SaveUser(null);
            Intent intent=new Intent(NavigationDrawerActivity.this,MainActivity.class);
            startActivity(intent);
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void OpenDrawer(){

        mDrawer.openDrawer(GravityCompat.START);
    }
}
