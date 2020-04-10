package com.example.firebaseintro.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.firebaseintro.Data.BlogRecyclerAdapter;
import com.example.firebaseintro.Model.Blog;
import com.example.firebaseintro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.*;

public class PostsListActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private RecyclerView mRecyclerView;
    private BlogRecyclerAdapter mBlogRecyclerAdapter;
    private List<Blog> mBlogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);


        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        mDatabase= FirebaseDatabase.getInstance();
        mDatabaseReference=mDatabase.getReference().child("MBlog");
        mDatabaseReference.keepSynced(true);


        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

   @Override
    protected void onStart() {
        super.onStart();
       mDatabaseReference.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//retrieve data from the database and put them into the recyclerView
                mBlogList=new ArrayList<>();
                mBlogList.clear();

                Blog blog = dataSnapshot.getValue(Blog.class);

                mBlogList.add(blog);

                Collections.reverse(mBlogList);

                mBlogRecyclerAdapter=new BlogRecyclerAdapter(PostsListActivity.this,mBlogList);
                mRecyclerView.setAdapter(mBlogRecyclerAdapter);

                mBlogRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_signout:
                mAuth.signOut();
                startActivity(new Intent(PostsListActivity.this, MainActivity.class));
                break;
            case R.id.action_add:
                startActivity(new Intent(PostsListActivity.this,PostActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
