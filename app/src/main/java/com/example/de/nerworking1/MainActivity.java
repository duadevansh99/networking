package com.example.de.nerworking1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    ProgressBar progressBar;
    postsAdapter adapter;
    ArrayList<Post> postTitles=new ArrayList<>();
    public static final String SEND_TITLE="send title";
    public static final String SEND_BODY="send body";
    public static final String SEND_USERID="send user id";
    public static final String SEND_POSTID="send post id";
    public static final int DISPLAY_REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView=findViewById(R.id.listview);
        progressBar=findViewById(R.id.progressbar);

        adapter = new postsAdapter(this,postTitles);
        listView.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);

        listView.setOnItemClickListener(this);

    }

    public void  fetchData(View view){
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        PostsAsyncTask task= new PostsAsyncTask(new PostsDownloadListener() {
            @Override
            public void onDownload(ArrayList<Post> posts) {
                postTitles.clear();
                postTitles.addAll(posts);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
        });
        task.execute();
        Toast.makeText(MainActivity.this,"Fetching Data",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Post post=postTitles.get(i);
        int postId=post.getPostId();
        int userId=post.getUserId();
        String title=post.getTitle();
        String body=post.getBody();
        Bundle displayBundle= new Bundle();
        displayBundle.putString(SEND_TITLE,title);
        displayBundle.putString(SEND_BODY,body);
        displayBundle.putInt(SEND_USERID,userId);
        displayBundle.putInt(SEND_POSTID,postId);

        Intent displayIntent=new Intent(this,DisplayActivity.class);
        displayIntent.putExtras(displayBundle);
        startActivityForResult(displayIntent,DISPLAY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==DISPLAY_REQUEST_CODE){
            if(resultCode==DisplayActivity.RESULT_CODE){
                progressBar.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                final int ID=data.getIntExtra(DisplayActivity.RETURN_POST_ID,0);
                PostsAsyncTask task= new PostsAsyncTask(new PostsDownloadListener() {
                    @Override
                    public void onDownload(ArrayList<Post> posts) {
                        postTitles.clear();
                        for(int i=0;i<100;i++){
                            Post p=posts.get(i);
                            if(p.getUserId()==ID){
                                postTitles.add(p);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }
                });
                task.execute();
                Toast.makeText(MainActivity.this,"Fetching Data",Toast.LENGTH_SHORT).show();
            }
        }
    }
}


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

