package com.example.android.gitbuddy;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    EditText mSearchBoxEditText;
    TextView mUrlDisplayTextView;
    TextView mSearchResultsTextView;
    TextView mErrorMessage;
    ProgressBar progressBar;
    static String URL_TAG = "URL";
    static String RESULT_TAG = "RESULT";
    String resultData;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText)findViewById(R.id.et_search_box);
        mUrlDisplayTextView = (TextView)findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView)findViewById(R.id.tv_github_search_results_json);
        mErrorMessage = (TextView)findViewById(R.id.errorMessage);
        progressBar = (ProgressBar)findViewById(R.id.pb_loading);

        if(savedInstanceState != null)
        {
            String url = savedInstanceState.getString(URL_TAG);
            String result = savedInstanceState.getString(RESULT_TAG);
            setUrl(url);
            setResultData(result);
            mUrlDisplayTextView.setText(url);
            mSearchResultsTextView.setText(result);
        }

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
        URL gitHubSearchUrl = NetworkUtils.buildUrl(gitHubQuery);
        mUrlDisplayTextView.setText(gitHubSearchUrl.toString());
        setUrl(gitHubSearchUrl.toString());
        new GitHubQueryTask().execute(gitHubSearchUrl);
    }

    class GitHubQueryTask extends AsyncTask<URL,Void,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String response="";
            try {
                response = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            if(s != null && !s.equals("")) {
                showJsonDataView();
                mSearchResultsTextView.setText(s);
                setResultData(s);
            }
            else
            {
                showErrorMessageView();
            }
        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RESULT_TAG,resultData);
        outState.putString(URL_TAG,url);
    }

    private void setResultData(String data)
    {
        resultData = data;
    }

    private void setUrl(String url)
    {
        this.url = url;
    }
}
