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

public class DisplayUserAsyncTask extends AsyncTask<Integer,Void,ArrayList<String>> {

    UserAsyncTaskListener listener;

    DisplayUserAsyncTask(UserAsyncTaskListener listener){
        this.listener=listener;
    }

    @Override
    protected ArrayList<String> doInBackground(Integer... integers) {
        ArrayList<String> user=new ArrayList<>();
        String urlString="https://jsonplaceholder.typicode.com/users";

        int ID=integers[0];

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

            Log.d("DisplayUserAsyncTask","Result: " + result);

            JSONArray rootArray=new JSONArray(result);
            for(int i=0;i<10;i++){
                JSONObject userObject=rootArray.getJSONObject(i);
                if(userObject.getInt("id")==ID){
                    String name=userObject.getString("name");
                    String email=userObject.getString("email");
                    user.add(name);
                    user.add(email);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("DisplayUserAsyncTask",user.size()+"");
        return user;
    }

    @Override
    protected void onPostExecute(ArrayList<String> user) {
        super.onPostExecute(user);
        listener.onDownloadUser(user);
    }
}
