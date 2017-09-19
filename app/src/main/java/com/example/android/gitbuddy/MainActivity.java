package com.example.android.gitbuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.gitbuddy.utilities.NetworkUtils;

import org.w3c.dom.Text;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText mSearchBoxEditText;
    TextView mUrlDisplayTextView;
    TextView mSearchResultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText)findViewById(R.id.et_search_box);
        mUrlDisplayTextView = (TextView)findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView)findViewById(R.id.tv_github_search_results_json);
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
