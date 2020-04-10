package com.example.firebaseintro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {

    private ImageView ivImage;
    private EditText etTitle,etDescription;
    private Button btPublish;
    private Uri mImageURI;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        ivImage=findViewById(R.id.ivImage);
        etTitle=findViewById(R.id.etTitle);
        etDescription=findViewById(R.id.etDescription);
        btPublish=findViewById(R.id.btPublish);



        btPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");  //"image/*"  means all types of images
                startActivityForResult(galleryIntent,1);
            }
        });

    }

    public void post(){

        String title = etTitle.getText().toString().trim();
        String description =etDescription.getText().toString().trim();

        StorageReference filepath = mStorage.child("Images").child(mImageURI.getLastPathSegment());
        filepath.putFile(mImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Uri downloadurl = taskSnapshot.getDownloadUrl();
                //DatabaseReference newPost = mPostDatabase
                Toast.makeText(PostActivity.this, "Image published", Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1 && resultCode == RESULT_OK){
            mImageURI = data.getData();
            ivImage.setImageURI(mImageURI);

    }
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
            Intent intent = new Intent(PostActivity.this, MainActivity.class);
             break;
        }
        return super.onOptionsItemSelected(item);
    }


}
