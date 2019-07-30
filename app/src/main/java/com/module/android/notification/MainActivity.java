package com.module.android.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "APp Name";
    private static final String CHANNEL_NAME = "APP Subname";
    private static final String CHANNEL_DESC = "This is  notification module";
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mauth = FirebaseAuth.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        progressBar = findViewById(R.id.ProgressBar);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressBar.setVisibility(View.INVISIBLE);

        findViewById(R.id.buttonSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult().getToken();

                        } else {

                        }
                    }
                });


        findViewById(R.id.buttonNotify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNotification();
            }
        });
    }

    private void createUser() {
        final String email = editTextEmail.getText().toString().trim();
        final String pass = editTextPassword.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        mauth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startProfileActivity();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        userlogin(email, pass);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG);
                    }
                }
            }
        });

    }

    private void userlogin(String email, String password) {
        mauth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startProfileActivity();
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG);
                        }
                    }
                });
    }

    private void startProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void displayNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("It is Working")
                .setContentText("This is the description content")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat mnotificationmgr = NotificationManagerCompat.from(this);
        mnotificationmgr.notify(1, mBuilder.build());
    }
}
