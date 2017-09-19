package com.example.android.gitbuddy.utilities;

/**
 * Created by prabhukonchada on 19/09/17.
 */

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    final static String GITHUB_BASE_URL = "https://api.github.com/search/repositories";

    final static String PARAM_QUERY = "q";

    final static String PARAM_SORT = "sort";
    final static String sortBy = "stars";

    public static URL buildUrl(String githubSearchQuery) {
        Uri buildUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY,githubSearchQuery)
                .appendQueryParameter(PARAM_SORT,sortBy)
                .build();

        Log.d("Build Uri",buildUri.toString());

        URL url = null;
        try
        {
            url = new URL(buildUri.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}