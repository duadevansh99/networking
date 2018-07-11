package com.example.de.nerworking1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {

    TextView title,body,userName,userId,userEmail;
    ListView commentsListView;
    ProgressBar progressBar;
    ArrayAdapter<String> adapter;
    ArrayList<String> displayComments=new ArrayList<>();
    int userID,postID;
    public static final String RETURN_POST_ID="returning post id";
    public static final int RESULT_CODE=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title=findViewById(R.id.posttitle);
        body=findViewById(R.id.postbody);
        userId=findViewById(R.id.userid);
        userName=findViewById(R.id.username);
        userEmail=findViewById(R.id.useremail);
        commentsListView=findViewById(R.id.comments);
        progressBar=findViewById(R.id.displayprogressbar);

        adapter= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,displayComments);
        commentsListView.setAdapter(adapter);

        Intent intent=getIntent();
        userID=intent.getIntExtra(MainActivity.SEND_USERID,0);
        postID=intent.getIntExtra(MainActivity.SEND_POSTID,0);
        title.setText(intent.getStringExtra(MainActivity.SEND_TITLE));
        body.setText(intent.getStringExtra(MainActivity.SEND_BODY));
        userId.setText(userID + " ");

        commentsListView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        getUser();
        getComments();

    }

    void getUser(){
        progressBar.setVisibility(View.VISIBLE);

        DisplayUserAsyncTask task= new DisplayUserAsyncTask(new UserAsyncTaskListener() {
            @Override
            public void onDownloadUser(ArrayList<String> user) {
                userName.setText(user.get(0));
                userEmail.setText(user.get(1));
                progressBar.setVisibility(View.GONE);
            }
        });
        task.execute(userID);
    }

    void getComments(){
        progressBar.setVisibility(View.VISIBLE);

        DisplayCommentsAsyncTask task= new DisplayCommentsAsyncTask(new CommentAsyncTaskListener() {
            @Override
            public void onDownloadComments(ArrayList<String> comments) {
                displayComments.addAll(comments);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                commentsListView.setVisibility(View.VISIBLE);
            }
        });
        task.execute(postID);
    }

    public  void goBack(View view){
        Intent intent=new Intent(DisplayActivity.this,MainActivity.class);
        intent.putExtra(RETURN_POST_ID,postID);
        setResult(RESULT_CODE,intent);
    }
}
