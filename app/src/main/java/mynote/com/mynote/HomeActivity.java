package mynote.com.mynote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.text.DecimalFormat;

import io.paperdb.Paper;
import mynote.com.mynote.Adapter.ViewPageAdapter;
import mynote.com.mynote.Common.Common;
import mynote.com.mynote.Database.Local.MyDatabase;
import mynote.com.mynote.Database.Model.MyCash;
import mynote.com.mynote.Fragment.AboutFragment;
import mynote.com.mynote.Fragment.MyInFragment;
import mynote.com.mynote.Fragment.MyOutFragment;
import mynote.com.mynote.Fragment.NoteFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private MyDatabase database;
    private Boolean starting = true;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = MyDatabase.getInstance(this);

        Paper.init(this);

        sharedPref = getSharedPreferences("starting", 0);
        editor = sharedPref.edit();

        Log.d("SHARE", String.valueOf(sharedPref.getInt("starting", 0)));

        if (sharedPref.getInt("starting", 0) == 0) {

            MyCash myCash = new MyCash();
            myCash.setMycash_id(1);
            myCash.setMycash_card("0");
            myCash.setMycash_kind("Kart");
            myCash.setUser_email(Common.current_user.getUser_email());

            database.myCashDao().insertCard(myCash);


            MyCash myCash2 = new MyCash();
            myCash2.setMycash_id(2);
            myCash2.setMycash_card("0");
            myCash2.setMycash_kind("Nagd");
            myCash2.setUser_email(Common.current_user.getUser_email());
            database.myCashDao().insertCard(myCash2);

            editor.putInt("starting", 1);
            editor.commit();

        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Əsas Səhifə");
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        ft.replace(R.id.container, homeFragment).addToBackStack(null)
                .commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.exit) {
            exitRemmeber();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void exitRemmeber() {
        Paper.book().delete(Common.KEY_LOGIN);
        Paper.book().delete(Common.KEY_PASSWORD);

        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (id == R.id.about) {
            toolbar.setTitle("Haqqımızda");
            AboutFragment aboutFragment = new AboutFragment();
            ft.replace(R.id.container, aboutFragment).addToBackStack(null).commit();

        }

        if (id == R.id.head) {
            toolbar.setTitle("Əsas Səhifə");
            HomeFragment homeFragment = new HomeFragment();
            ft.replace(R.id.container, homeFragment).addToBackStack(null)
                    .commit();

        }


        if (id == R.id.myin) {
            toolbar.setTitle("Mənim Gəlirim");
            MyInFragment myInFragment = new MyInFragment();
            ft.replace(R.id.container, myInFragment).addToBackStack(null)
                    .commit();

        }


        if (id == R.id.myout) {
            toolbar.setTitle("Mənim Xərclərim");
            MyOutFragment myOutFragment = new MyOutFragment();
            ft.replace(R.id.container, myOutFragment).addToBackStack(null)
                    .commit();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
