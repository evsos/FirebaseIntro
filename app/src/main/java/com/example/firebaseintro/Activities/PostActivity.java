package com.example.firebaseintro.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.firebaseintro.Data.BlogRecyclerAdapter;
import com.example.firebaseintro.Model.Blog;
import com.example.firebaseintro.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostActivity extends AppCompatActivity {

    private ImageView ivImage;
    private EditText etTitle,etDescription;
    private Button btPublish;
    private Uri mImageURI;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorage;
    private static final int GALLERY_CODE=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabaseReference=FirebaseDatabase.getInstance().getReference().child("MBlog");

        ivImage= (ImageView) findViewById(R.id.ivImage);
        etTitle= (EditText) findViewById(R.id.etTitle);
        etDescription= (EditText) findViewById(R.id.etDescription);
        btPublish= (Button) findViewById(R.id.btPublish);


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
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });

    }

    public void post(){

        final String title = etTitle.getText().toString().trim();
        final String description =etDescription.getText().toString().trim();

          if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && mImageURI!=null){

              /* Blog blog = new Blog("Title","description","imageURL","timestamp","userID");
                mDatabaseReference.setValue(blog).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Item added",Toast.LENGTH_LONG).show();
                    }
                });*/

        StorageReference filepath = mStorage.child("Images").child(mImageURI.getLastPathSegment());
        filepath.putFile(mImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(PostActivity.this, "Image published", Toast.LENGTH_LONG).show();
                Task<Uri> mImageURI;
                mImageURI = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                DatabaseReference newPost = mDatabaseReference.push();

                Map<String,String> dataToSave = new HashMap<>();
                dataToSave.put("title", title);
                dataToSave.put("description",description);
                dataToSave.put("image",mImageURI.toString());
                dataToSave.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
                dataToSave.put("userID",mUser.getUid());

                newPost.setValue(dataToSave);

                startActivity(new Intent(PostActivity.this,PostsListActivity.class));
                finish();
            }
            });
    }
    }





    //What happens when we return to the activity(from the gallery)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
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
