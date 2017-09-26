package com.example.android.gitbuddy;

import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.gitbuddy.utilities.NetworkUtils;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    EditText mSearchBoxEditText;
    TextView mUrlDisplayTextView;
    TextView mSearchResultsTextView;
    TextView mErrorMessage;
    ProgressBar progressBar;
    static String URL_TAG = "URL";
    private static final int GIT_HUB_LOADER_IDENTIFIER = 52;
    Bundle mainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = savedInstanceState;

        mSearchBoxEditText = (EditText)findViewById(R.id.et_search_box);
        mUrlDisplayTextView = (TextView)findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView)findViewById(R.id.tv_github_search_results_json);
        mErrorMessage = (TextView)findViewById(R.id.errorMessage);
        progressBar = (ProgressBar)findViewById(R.id.pb_loading);

        getSupportLoaderManager().initLoader(GIT_HUB_LOADER_IDENTIFIER,null,this);
    }

    private void showJsonDataView()
    {
        mSearchResultsTextView.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessageView()
    {
        mErrorMessage.setVisibility(View.VISIBLE);
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    public void makeGitHubSearchQuery()
    {
        String gitHubQuery = mSearchBoxEditText.getText().toString();
        if(gitHubQuery != null) {
            URL gitHubSearchUrl = NetworkUtils.buildUrl(gitHubQuery);
            mUrlDisplayTextView.setText(gitHubSearchUrl.toString());
            mainActivity = new Bundle();
            mainActivity.putString(URL_TAG, gitHubSearchUrl.toString());
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> githubLoader = loaderManager.getLoader(GIT_HUB_LOADER_IDENTIFIER);
            if(githubLoader == null)
            {
                loaderManager.initLoader(GIT_HUB_LOADER_IDENTIFIER,mainActivity,this);
            }
            else
            {
                loaderManager.restartLoader(GIT_HUB_LOADER_IDENTIFIER,mainActivity,this);
            }
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String storeRawJson=null;

            @Override
            protected void onStartLoading() {

                if(args == null)
                    return;
                else
                    progressBar.setVisibility(View.VISIBLE);


                if(storeRawJson != null)
                {
                    deliverResult(storeRawJson);
                }
                else
                {
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(String data) {
                storeRawJson = data;
                super.deliverResult(data);
            }

            @Override
            public String loadInBackground() {
                Log.d("Background","LoadInBackground");
                if(args == null)
                {
                    return null;
                }
                else
                {
                    String response="";
                    try {
                        URL githubUrl = new URL(args.getString(URL_TAG));
                        Log.d("URL",githubUrl.toString());
                        response = NetworkUtils.getResponseFromHttpUrl(githubUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return  response;
                }
            }
        };


    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        progressBar.setVisibility(View.INVISIBLE);
        if(data != null && !data.equals("")) {
            showJsonDataView();
            Log.d("Data",data);
            mSearchResultsTextView.setText(data);
        }
        else
        {
            showErrorMessageView();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuItemSelected =item.getItemId();

        if(menuItemSelected == R.id.action_search)
        {
            Toast.makeText(this,"Search Menu Selected",Toast.LENGTH_LONG).show();
            makeGitHubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
