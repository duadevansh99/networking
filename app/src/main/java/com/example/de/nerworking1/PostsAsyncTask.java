package com.example.de.nerworking1;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class PostsAsyncTask extends AsyncTask<Void,Void,ArrayList<Post>>{

    PostsDownloadListener listener;
    Post post=new Post();
    PostsAsyncTask(PostsDownloadListener listener) {
        this.listener=listener;
    }

    @Override
    protected ArrayList<Post> doInBackground(Void... voids) {

        ArrayList<Post> posts=new ArrayList<>();
        String urlString="https://jsonplaceholder.typicode.com/posts";
        try{
            URL url=new URL(urlString);

            HttpsURLConnection urlConnection=(HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            urlConnection.connect();
            InputStream inputStream=urlConnection.getInputStream();
            Scanner scanner=new Scanner(inputStream);
            String result="";
            while (scanner.hasNext()){
                result=result + scanner.next();
            }

            Log.d("MainActivity","Result: " + result);

            JSONArray rootArray=new JSONArray(result);
            for(int i=0; i<100;i++){
                JSONObject postObject=rootArray.getJSONObject(i);
                String name=postObject.getString("title");
                String body=postObject.getString("body");
                int user_id=postObject.getInt("userId");
                int post_id=postObject.getInt("id");
                post.setTitle(name);
                post.setBody(body);
                post.setUserId(user_id);
                post.setPostId(post_id);
                posts.add(post);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("PostsAsyncTaskBLAHBLAH",posts.size() + "");
        return posts;

    }

    @Override
    protected void onPostExecute(ArrayList<Post> posts) {
        super.onPostExecute(posts);
        listener.onDownload(posts);
    }
}
