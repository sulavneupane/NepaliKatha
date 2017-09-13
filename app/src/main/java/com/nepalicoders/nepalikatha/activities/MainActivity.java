package com.nepalicoders.nepalikatha.activities;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.pushbots.push.Pushbots;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.nepalicoders.nepalikatha.constants.ApiUrl;
import com.nepalicoders.nepalikatha.database.DatabaseHandler;
import com.nepalicoders.nepalikatha.fragments.AboutFragment;
import com.nepalicoders.nepalikatha.fragments.EnglishPoemsFragment;
import com.nepalicoders.nepalikatha.fragments.FavouriteFragment;
import com.nepalicoders.nepalikatha.fragments.LatestPoemsFragment;
import com.nepalicoders.nepalikatha.R;
import com.nepalicoders.nepalikatha.fragments.NepaliPoemsFragment;
import com.nepalicoders.nepalikatha.fragments.SubmitPoemFragment;
import com.nepalicoders.nepalikatha.interfaces.Callback;
import com.nepalicoders.nepalikatha.json.JsonParser;
import com.nepalicoders.nepalikatha.objects.Poem;
import com.nepalicoders.nepalikatha.utils.BackgroundThread;
import com.nepalicoders.nepalikatha.utils.Connection;
import com.nepalicoders.nepalikatha.utils.Messages;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static long back_pressed;

    DatabaseHandler handler;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Pushbots.sharedInstance().init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new LatestPoemsFragment()).commit();

        try {
            handler = new DatabaseHandler(MainActivity.this).Open();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //close drawer if open
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else if (getFragmentManager().getBackStackEntryCount() == 0) {
                //close app on double back press
                if (back_pressed + 2000 > System.currentTimeMillis()) {
                    super.onBackPressed();
                } else {
                    Messages.snackBarShort(this.getWindow().getDecorView(), getString(R.string.press_again_exit));
                    back_pressed = System.currentTimeMillis();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        Drawable drawable = menu.findItem(R.id.action_refresh).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, getResources().getColor(R.color.colorText));
        menu.findItem(R.id.action_refresh).setIcon(drawable);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            //Snackbar.make(this.getWindow().getDecorView(), R.string.action_refresh, Snackbar.LENGTH_SHORT).show();
            final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Loading");
            dialog.setMessage("Updating Latest Stories...");
            dialog.show();

            BackgroundThread thread = new BackgroundThread(ApiUrl.NEPALI_KABITA_GET_ALL_POEMS_URL, null, Connection.GET);
            thread.setBackgroundListener(MainActivity.this, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    dialog.dismiss();
                    //Log.d("Nepali Katha", "No Internet Connection!");
                    Messages.snackBarShort(MainActivity.this.getWindow().getDecorView(), getString(R.string.no_internet_connection));
                }

                @Override
                public void onResponse(Response response, String result) throws IOException {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        //Log.d("NepaliKatha", result);

                        List<Poem> poemList = JsonParser.parseAllPoems(result);

                        handler.removeAllPoemsFromDatabase();
                        for (int i = 0; i < poemList.size(); i++) {
                            handler.addPoemToDatabase(poemList.get(i));
                        }

                        Snackbar.make(MainActivity.this.getWindow().getDecorView(), R.string.action_update_poems, Snackbar.LENGTH_SHORT).show();

                        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
                        if(fragment instanceof NepaliPoemsFragment){

                            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new NepaliPoemsFragment()).commit();

                        } else if(fragment instanceof EnglishPoemsFragment){

                            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new EnglishPoemsFragment()).commit();

                        } else {

                            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new LatestPoemsFragment()).commit();
                            navigationView.getMenu().getItem(0).setChecked(true);

                        }
                        //navigationView.getMenu().getItem(0).setChecked(true);
                    } else {
                        Snackbar.make(MainActivity.this.getWindow().getDecorView(), R.string.action_wrong, Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
            thread.execute();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new LatestPoemsFragment()).commit();

        } else if (id == R.id.nav_nepali_poems) {

            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new NepaliPoemsFragment()).commit();

        } else if (id == R.id.nav_english_poems) {

            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new EnglishPoemsFragment()).commit();

        }else if (id == R.id.nav_favourites) {

            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new FavouriteFragment()).commit();

        } else if (id == R.id.nav_submit_poem) {

            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new SubmitPoemFragment()).commit();

        } else if (id == R.id.nav_about) {

            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new AboutFragment()).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
