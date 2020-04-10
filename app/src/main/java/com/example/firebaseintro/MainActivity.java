package com.example.firebaseintro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private EditText etEmail, etPassword;
    private Button btLogin,btRegister;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        btLogin=findViewById(R.id.btLogin);
        btRegister=findViewById(R.id.btRegister);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("message");

        databaseReference.setValue("Hello firebase");

        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Customer> myList = new ArrayList<>();

                Customer customer = dataSnapshot.getValue(Customer.class);

                myList.add(customer);
                for (Customer c: myList){
                   Toast.makeText(MainActivity.this,c.getFirstName(),Toast.LENGTH_LONG).show();
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user is signed in
                    Log.d(TAG,"User signed in");
                    Toast.makeText(MainActivity.this,"Signed in",Toast.LENGTH_LONG).show();
                  }else  {
                    //user is signed out
                    Log.d(TAG,"User signed out");
                    Toast.makeText(MainActivity.this,"Signed out",Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

            if (mAuthStateListener != null){
                mAuth.removeAuthStateListener(mAuthStateListener);
            }
    }

    public void login(){
        if (etEmail.getText().toString()==null || etPassword.getText().toString()==null){
            Toast.makeText(MainActivity.this,"Please enter all fields",Toast.LENGTH_LONG).show();
        }else{
        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("userLogin","createdUserWithEmailAndPass: success!");
                    Toast.makeText(MainActivity.this,"user login: success",Toast.LENGTH_LONG).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    Customer customer = new Customer("Lena","Neig","gina@mail.com",94 );
                    databaseReference.setValue(customer);

                    Intent intent = new Intent(MainActivity.this, PostActivity.class);
                    startActivity(intent);

                }else{
                    Log.w("userLoginStatus","failure",task.getException());
                    Toast.makeText(MainActivity.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
        }
    }

    public void logout(){
        mAuth.signOut();
        Toast.makeText(this,"You signed out",Toast.LENGTH_LONG).show();
    }

    public void register(){
        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                FirebaseUser user =mAuth.getCurrentUser();
                   Toast.makeText(MainActivity.this,"registered new user",Toast.LENGTH_LONG).show();
                   Intent intent = new Intent(MainActivity.this, PostActivity.class);
                   startActivity(intent);

               }else{
                   Toast.makeText(MainActivity.this,task.getException().toString(),Toast.LENGTH_LONG).show();
               }
            }
        });
    }
}
