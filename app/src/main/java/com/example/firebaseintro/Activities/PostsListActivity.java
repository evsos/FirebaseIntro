package com.example.firebaseintro.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.firebaseintro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class PostsListActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);


        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance();
        mDatabaseReference=mDatabase.getReference().child("MBlog");
        mDatabaseReference.keepSynced(true);

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
