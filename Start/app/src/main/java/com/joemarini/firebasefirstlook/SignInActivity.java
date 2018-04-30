package com.joemarini.firebasefirstlook;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity
        implements View.OnClickListener {

    // Create TAG for logging
    private final String TAG = "FB_SIGNIN";

    // TODO: Add Auth members
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Declare Views
    private EditText etPass;
    private EditText etEmail;

    /**
     * Standard Activity lifecycle methods
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Set up click handlers and view item references
        findViewById(R.id.btnCreate).setOnClickListener(this);
        findViewById(R.id.btnSignIn).setOnClickListener(this);
        findViewById(R.id.btnSignOut).setOnClickListener(this);

        // Get a reference to the views
        etEmail = (EditText) findViewById(R.id.etEmailAddr);
        etPass = (EditText) findViewById(R.id.etPassword);

        // TODO: Get a reference to the Firebase auth object
        mAuth = FirebaseAuth.getInstance();

        // TODO: Attach a new AuthListener to detect sign in and out
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // Get a reference to the current user
                FirebaseUser user = firebaseAuth.getCurrentUser();

                // If there is a user...
                if (user != null) {

                    // Log singed in message
                    Log.d(TAG, "Signed in: " + user.getUid());

                    // If there is no user...
                } else {

                    // Log singed out message
                    Log.d(TAG, "Currently Signed Out");
                }
            }
        };

        // Update the status of the user
        updateStatus();
    }

    /**
     * When the Activity starts and stops, the app needs to connect and
     * disconnect the AuthListener
     */
    @Override
    public void onStart() {
        super.onStart();

        // TODO: add the AuthListener
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        // TODO: Remove the AuthListener

        // If the AuthListener is not nul...
        if (mAuthListener != null) {

            // Remove the AuthListener
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {

        // Get the Id of the button clicked
        switch (v.getId()) {

            // If the button is a SignIn Button...
            case R.id.btnSignIn:

                // Sign a user in
                signUserIn();

                // Break from the switch statement
                break;

            // If the button is a Create Button...
            case R.id.btnCreate:

                // Create a user account
                createUserAccount();

                // Break from the switch statement
                break;

            // If the button is a SignOut Button...
            case R.id.btnSignOut:

                // Sign a user out
                signUserOut();

                // Break from the switch statement
                break;
        }
    }

    private boolean checkFormFields() {

        // Declare email and password Strings
        String email, password;

        // Get the email and password from the EditText views
        email = etEmail.getText().toString();
        password = etPass.getText().toString();

        // If the email is empty...
        if (email.isEmpty()) {

            // Set the error on the email EditText
            etEmail.setError("Email Required");

            // Return from the method
            return false;
        }

        // If the email is empty...
        if (password.isEmpty()) {

            // Set the error on the password EditText
            etPass.setError("Password Required");

            // Return from the method
            return false;
        }

        // Return true
        return true;
    }

    private void updateStatus() {

        // Get a reference to the TextView
        TextView tvStat = (TextView) findViewById(R.id.tvSignInStatus);

        // TODO: get the current user
        FirebaseUser user = mAuth.getCurrentUser();

        // If the user is not null...
        if (user != null) {

            // Display the user as signed in
            tvStat.setText("Signed in: " + user.getEmail());

            // If the user is null...
        } else {

            // Display signed out
            tvStat.setText("Signed Out");
        }
    }

    private void updateStatus(String stat) {

        // Get a reference to the status TextView
        TextView tvStat = (TextView) findViewById(R.id.tvSignInStatus);

        // Set the status
        tvStat.setText(stat);
    }

    private void signUserIn() {

        // If the EditText fields are blank...
        if (!checkFormFields())

            // Return from the method
            return;

        // Get the email and password from the EditText fields
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        // TODO: sign the user in with email and password credentials
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If the Task was successful...
                        if (task.isSuccessful()) {

                            // Display Signed In Toast
                            Toast.makeText(SignInActivity.this, "Signed In", Toast.LENGTH_SHORT).show();

                            // If the Task was not successful...
                        } else {

                            // Display Signed In Failed Toast
                            Toast.makeText(SignInActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                        }

                        // Update the user status
                        updateStatus();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // If the Exception is a FirebaseAuthInvalidCredentialsException...
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {

                            // Update the status with invalid password message
                            updateStatus("Invalid Password");

                            // If the Exception is a FirebaseAuthInvalidUserException...
                        } else if (e instanceof FirebaseAuthInvalidUserException) {

                            // Update the status with no account with email message
                            updateStatus("No account with this email");

                        } else {

                            // Update the status of the exception
                            updateStatus(e.getLocalizedMessage());
                        }
                    }
                });
    }

    private void signUserOut() {
        // TODO: sign the user out
        mAuth.signOut();

        // Update the user status
        updateStatus();
    }

    private void createUserAccount() {

        // If the EditText fields are blank...
        if (!checkFormFields())

            // Return from the method
            return;

        // Get the email and password from the EditText fields
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        // TODO: Create the user account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If the Task was successful...
                        if (task.isSuccessful()) {

                            // Display success Toast
                            Toast.makeText(SignInActivity.this, "User was created", Toast.LENGTH_SHORT).show();

                            // If the Task was not successful...
                        } else {

                            // Display failure Toast
                            Toast.makeText(SignInActivity.this, "Account creation failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // If the Exception is a FirebaseAuthUserCollisionException...
                        if (e instanceof FirebaseAuthUserCollisionException) {

                            // Update the status telling the user that there is already
                            // an email like that in use
                            updateStatus("This email is already in use");

                            // If the Exception is anything else...
                        } else {

                            // Update the status of the exception
                            updateStatus(e.getLocalizedMessage());
                        }
                    }
                });
    }
}