package com.example.de.nerworking1;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class DisplayCommentsAsyncTask extends AsyncTask<Integer,Void,ArrayList<String>> {

    CommentAsyncTaskListener listener;
    DisplayCommentsAsyncTask(CommentAsyncTaskListener listener){
        this.listener=listener;
    }

    @Override
    protected ArrayList<String> doInBackground(Integer... integers) {

        String urlString="https://jsonplaceholder.typicode.com/comments";
        int ID=integers[0];
        ArrayList<String> comments=new ArrayList<>();
        try{
            URL url=new URL(urlString);

            HttpsURLConnection urlConnection=(HttpsURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream=urlConnection.getInputStream();
            Scanner scanner=new Scanner(inputStream);
            String result="";
            while (scanner.hasNext()){
                result=result + scanner.next();
            }

            Log.d("DisplayCommentAsyncTask","Result: " + result);

            JSONArray rootArray=new JSONArray(result);
            for(int i=0;i<500;i++){
                JSONObject commentObject=rootArray.getJSONObject(i);
                if(commentObject.getInt("postId")==ID){
                    String commentName=commentObject.getString("name");
                    comments.add(commentName);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("DisplayCommentAsyncTask", comments.size() +" ");
        return comments;
    }

    @Override
    protected void onPostExecute(ArrayList<String> comments) {
        super.onPostExecute(comments);
        listener.onDownloadComments(comments);
    }
}
