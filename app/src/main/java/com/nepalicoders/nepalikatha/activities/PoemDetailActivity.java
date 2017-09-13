package com.nepalicoders.nepalikatha.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nepalicoders.nepalikatha.R;
import com.nepalicoders.nepalikatha.application.App;
import com.nepalicoders.nepalikatha.database.DatabaseHandler;
import com.nepalicoders.nepalikatha.database.DatabaseHelper;
import com.nepalicoders.nepalikatha.objects.Poem;
import com.nepalicoders.nepalikatha.utils.Messages;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PoemDetailActivity extends AppCompatActivity {
    DatabaseHandler handler;
    Poem poem;

    Tracker mTracker;

    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("B59D8EAE0A53F0E6D4AE81B8F12C19B9").build();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        //AdRequest adRequestInter = new AdRequest.Builder().addTestDevice("B59D8EAE0A53F0E6D4AE81B8F12C19B9").build();
        AdRequest adRequestInter = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequestInter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            handler = new DatabaseHandler(PoemDetailActivity.this).Open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                String shareText = "Story Title: " + Html.fromHtml(poem.getTitle()) + "\nWritten By: " + Html.fromHtml(poem.getWriter()) + "\n\n" + Html.fromHtml(poem.getContent()) + "\n\nStory Shared Via Nepali Katha App";
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        poem = (Poem) getIntent().getSerializableExtra("Poem");

        getSupportActionBar().setTitle(Html.fromHtml(poem.getTitle()));

        TextView poemTitle = (TextView) findViewById(R.id.poem_title);
        TextView publishedDate = (TextView) findViewById(R.id.poem_published_date);
        TextView content = (TextView) findViewById(R.id.poem_content);
        TextView writer = (TextView) findViewById(R.id.poem_writer);
        TextView submittedBy = (TextView) findViewById(R.id.poem_submitted_by);

        //change date format
        SimpleDateFormat dbDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat displayDate = new SimpleDateFormat("MMMM dd, yyyy");
        String date = null;
        try {
            Date convertedCurrentDate = dbDate.parse(poem.getPublishedOn());
            date = displayDate.format(convertedCurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        poemTitle.setText(poem.getTitle());
        writer.setText(" - " + Html.fromHtml(poem.getWriter()));
        content.setText(Html.fromHtml(poem.getContent()));
        publishedDate.setText("Submitted: " + date);
        submittedBy.setText(poem.getSubmittedBy());

        mTracker = ((App) (this.getApplication())).getDefaultTracker();
        mTracker.setScreenName("Story Detail View");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("AddStoryToFavourite")
                .setAction("Story = " + poem.getTitle() + " Writer = " + poem.getWriter())
                .build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.poem_detail_menu, menu);
        if (checkPoemInFavList()) {
            menu.findItem(R.id.action_favourite).setIcon(R.drawable.ic_favourite);

            Drawable drawable = menu.findItem(R.id.action_favourite).getIcon();
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, getResources().getColor(R.color.colorText));
            menu.findItem(R.id.action_favourite).setIcon(drawable);
        } else {
            menu.findItem(R.id.action_favourite).setIcon(R.drawable.ic_favourites);

            Drawable drawable = menu.findItem(R.id.action_favourite).getIcon();
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, getResources().getColor(R.color.colorText));
            menu.findItem(R.id.action_favourite).setIcon(drawable);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_favourite) {

            Poem poem = (Poem) getIntent().getSerializableExtra("Poem");
            poem.setId(poem.getId());
            poem.setTitle(poem.getTitle());
            poem.setWriter(poem.getWriter());
            poem.setContent(poem.getContent());
            poem.setCategory(poem.getCategory());
            poem.setPublishedOn(poem.getPublishedOn());
            poem.setSubmittedBy(poem.getSubmittedBy());
            poem.setEmail(poem.getEmail());


            if (checkPoemInFavList()) {
                int result = handler.removePoemFromFavourite(poem.getId());
                if (result > 0) {
                    item.setIcon(R.drawable.ic_favourites);
                    Messages.snackBarShort(getWindow().getDecorView(), getString(R.string.action_remove_favourite));

                    Drawable drawable = item.getIcon();
                    drawable = DrawableCompat.wrap(drawable);
                    DrawableCompat.setTint(drawable, getResources().getColor(R.color.colorText));
                    item.setIcon(drawable);
                    return true;
                }
            } else {
                long poemid = handler.addPoemToFavourite(poem);
                if (poemid > 0) {
                    item.setIcon(R.drawable.ic_favourite);
                    Messages.snackBarShort(getWindow().getDecorView(), getString(R.string.action_favourite));

                    Drawable drawable = item.getIcon();
                    drawable = DrawableCompat.wrap(drawable);
                    DrawableCompat.setTint(drawable, getResources().getColor(R.color.colorText));
                    item.setIcon(drawable);
                    return true;
                }
            }
            //handler.close();
        } else if (id == android.R.id.home){

            onBackPressed();

        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkPoemInFavList() {

        Cursor cursor = handler.getAllPoemsFromFavourite();
        //if(cursor!=null && cursor.getCount()>0){
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            Poem poem = (Poem) getIntent().getSerializableExtra("Poem");
            if (poem.getId() == cursor.getLong(cursor.getColumnIndex(DatabaseHelper.KEY_FAVOURITES_ITEM_ID))) {
                return true;
            }
        }
        return false;
    }

}
