package tasks.com.movies.activity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Url;
import tasks.com.movies.R;
import tasks.com.movies.model.MovieDetails;

public class FirstActivity extends AppCompatActivity {
   ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        listView= (ListView) findViewById(R.id.list_item);


    }



    class AsynkTaskConnection extends AsyncTask<String ,Void  ,String> {


        @Override
        protected String doInBackground(String... params) {
            URL url = null;
 //As we are passing just one parameter to AsyncTask, so used param[0] to get value at 0th position that is URL
            try {
                url = new URL(params[0]);
            }
           catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try{
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
//Getting inputstream from connection, that is response which we got from server
                InputStream inputStream=httpURLConnection.getInputStream();
//Reading the response
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String s = bufferedReader.readLine();
                bufferedReader.close();
 //Returning the response message to onPostExecute method
                return s;
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage(), e);
            }
            return null;
        }
//This method runs on UIThread and it will execute when doInBackground is completed
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject=null;
            try{
//Parent JSON Object. Json object start at { and end at }
               jsonObject=new JSONObject(s);
                ArrayList<MovieDetails>movieDetailsArrayList=new ArrayList<>();
//JSON Array of parent JSON object. Json array starts from [ and end at ]
                JSONArray jsonArray=jsonObject.getJSONArray("results");
 //Reading JSON object inside Json array
                for (int i =0; i<jsonArray.length();i++){
//Reading JSON object at 'i'th position of JSON Array
                JSONObject object=jsonArray.getJSONObject(i);
                MovieDetails movieDetails = new MovieDetails();
                movieDetails.setOriginal_title(object.getString("original_title"));
                movieDetails.setVote_average(object.getDouble("vote_average"));
                movieDetails.setOverview(object.getString("overview"));
                movieDetails.setRelease_date(object.getString("release_date"));
                movieDetails.setPoster_path(object.getString("poster_path"));
                movieDetailsArrayList.add(movieDetails);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


        }

