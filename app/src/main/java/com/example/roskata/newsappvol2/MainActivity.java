package com.example.roskata.newsappvol2;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    //https://content.guardianapis.com/search?show-tags=contributor&show-fields=thumbnail&page=20&api-key=c56b5ed0-1a11-4d00-bac6-7bbca00c3081
    //https://content.guardianapis.com/search?section=music&show-tags=contributor&show-fields=thumbnail&page=20&api-key=c56b5ed0-1a11-4d00-bac6-7bbca00c3081
    //https://content.guardianapis.com/search?section=politics&order-by=newest&show-tags=contributor&show-fields=thumbnail&page=20&api-key=c56b5ed0-1a11-4d00-bac6-7bbca00c3081
    //private static final String URL_REQUEST = "https://content.guardianapis.com/search?order-by=newest&show-tags=contributor&show-fields=thumbnail&page=20&page-size=20&api-key=c56b5ed0-1a11-4d00-bac6-7bbca00c3081";
    //https:content.guardianapis.com/search?order-by=newest&show-tags=contributor&show-fields=thumbnail&page=20&page-size=13&api-key=c56b5ed0-1a11-4d00-bac6-7bbca00c3081
    //https://content.guardianapis.com/search?show-tags=contributor&show-fields=thumbnail&page=20&page-size=20&api-key=c56b5ed0-1a11-4d00-bac6-7bbca00c3081
    //https://content.guardianapis.com/search?q=show-tags=contributor&show-fields=thumbnail&page-size=15&api-key=c56b5ed0-1a11-4d00-bac6-7bbca00c3081
    //https://content.guardianapis.com/search?q=&order-by=newest&show-tags=contributor&show-fields=thumbnail&api-key=c56b5ed0-1a11-4d00-bac6-7bbca00c3081
    private static final String URL_REQUEST = "http://content.guardianapis.com/search?";
    private static final int ARTICLE_LOADER_ID = 1;
    private static final String AND_OPERATOR = "%20AND%20";
    private ArticleAdapter articleAdapter;
    private ProgressBar progressBar;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.list_view);
        progressBar = findViewById(R.id.loading_indicator);
        emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        articleAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        listView.setAdapter(articleAdapter);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }
        boolean isConnected = networkInfo != null && networkInfo.isConnected();

        if (isConnected) {
            progressBar.setVisibility(View.VISIBLE);
            getLoaderManager().initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            emptyView.setText(R.string.no_connection);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Objects.requireNonNull(articleAdapter.getItem(position)).getUrl()));
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String numberOfNews = sharedPreferences.getString(getString(R.string.settings_number_of_articles_key), getString(R.string.settings_number_of_articles_default));
        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
        Boolean music = sharedPreferences.getBoolean(getString(R.string.settings_section_music_key), true);
        Boolean politics = sharedPreferences.getBoolean(getString(R.string.settings_section_politics_key), true);
        Boolean environment = sharedPreferences.getBoolean(getString(R.string.settings_section_environment_key), true);
        Boolean economics = sharedPreferences.getBoolean(getString(R.string.settings_section_economics_key), true);
        Boolean opinion = sharedPreferences.getBoolean(getString(R.string.settings_section_opinion_key), true);
        Boolean society = sharedPreferences.getBoolean(getString(R.string.settings_section_society_key), true);
        Boolean design = sharedPreferences.getBoolean(getString(R.string.settings_section_design_key), true);

        StringBuilder sections = new StringBuilder();
        if (music) {
            sections.append(getString(R.string.settings_section_music_key) + AND_OPERATOR);
        }
        if (politics) {
            sections.append(getString(R.string.settings_section_politics_key) + AND_OPERATOR);
        }
        if (environment) {
            sections.append(getString(R.string.settings_section_environment_key) + AND_OPERATOR);
        }
        if (economics) {
            sections.append(getString(R.string.settings_section_economics_key) + AND_OPERATOR);
        }
        if (opinion) {
            sections.append(getString(R.string.settings_section_opinion_key) + AND_OPERATOR);
        }
        if (society) {
            sections.append(getString(R.string.settings_section_society_key) + AND_OPERATOR);
        }
        if (design) {
            sections.append(getString(R.string.settings_section_design_key) + AND_OPERATOR);
        }

        if (sections.toString().endsWith(AND_OPERATOR)){
            sections.delete(sections.toString().length() - AND_OPERATOR.length(), sections.toString().length());
            Log.i(LOG_TAG, sections.toString());
        }

        Uri baseUri = Uri.parse(URL_REQUEST);

        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("sections", sections.toString());
        uriBuilder.appendQueryParameter("section", "music");
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", numberOfNews);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("api-key", "c56b5ed0-1a11-4d00-bac6-7bbca00c3081");

        Log.i(LOG_TAG, uriBuilder.toString());
        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        emptyView.setText(R.string.no_data);
        progressBar.setVisibility(View.GONE);
        articleAdapter.clear();

        if (data != null && !data.isEmpty()) {
            articleAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) { articleAdapter.clear(); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(LOG_TAG, "TEST: Calling onCreateOptionsMenu().");

        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}