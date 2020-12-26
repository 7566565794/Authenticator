package com.example.firebaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity {
// step 1 : Declare the Variables.
    EditText mFullName, mEmail, mPassword, mConfirmPassword;
    Button mRegistration;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Step 2: Connect the XML variable
        mFullName= findViewById(R.id.FullName);
        mEmail=findViewById(R.id.Email);
        mPassword=findViewById(R.id.Password);
        mConfirmPassword=findViewById(R.id.Confirm_Password);
        mRegistration=findViewById(R.id.loginBtn);
        mLoginBtn=findViewById(R.id.Already_Register);
        progressBar=findViewById(R.id.progressBar1);
        fAuth= FirebaseAuth.getInstance();

        //Now before calling below method.. We need to check whether a user is a already registered User.
        // Then we need to start a new activity Login
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });


        // if the user is already logged in or registered. Then there is no requirement of showing login and signup page.
        // so we will directly check with the special method.
//        if(fAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        }

        // step 3 : writing the code so that the actions can be performed
        // on click of button, we want that our data should be store in the firebase database.
        mRegistration.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String email=mEmail.getText().toString().trim();
                // here we are doing trim, just to format the data.
                String password= mPassword.getText().toString();
                String ConfirmPassword= mConfirmPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    return;
                }

                if(password.length()<6){
                    mPassword.setError("Should be more than 6 characters");
                    return;
                }

                //checking whether entered password is same as confirm password otherwise we will set error.
                if(!(password.equals(ConfirmPassword))){
                    mConfirmPassword.setError("Both Passwords Doesnt match ");
                    return;
                }
                // So all these errors we are going to display to the user.
                // if there are no errors in the above case then we need to store user data on the database.
                // So now while the data is getting store to the database. We are going to display a progress bar that the data
                // is getting saved.

                progressBar.setVisibility(View.VISIBLE);

                // Register the User in the firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           Toast.makeText(getApplicationContext(),"User Created", Toast.LENGTH_LONG).show();
                           startActivity(new Intent(getApplicationContext(), MainActivity.class));
                           finish();
                       }
                       else{
                            Toast.makeText(getApplicationContext(),"Error!"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

//                            ==================================
//                           try {
//                               throw task.getException();
//                           } catch(FirebaseAuthWeakPasswordException e) {
//                               mTxtPassword.setError(getString(R.string.error_weak_password));
//                               mTxtPassword.requestFocus();
//                           } catch(FirebaseAuthInvalidCredentialsException e) {
//                               mTxtEmail.setError(getString(R.string.error_invalid_email));
//                               mTxtEmail.requestFocus();
//                           } catch(FirebaseAuthUserCollisionException e) {
//                               mTxtEmail.setError(getString(R.string.error_user_exists));
//                               mTxtEmail.requestFocus();
//                           } catch(Exception e) {
//                               Log.e(TAG, e.getMessage());
//                           }
//                           =====================================
                       }
                    }
                });
            }
        });
    }
}