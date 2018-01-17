package com.example.satyajit.a108ambulancefordrivers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.satyajit.a108ambulancefordrivers.MainActivity;
import com.example.satyajit.a108ambulancefordrivers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity{
    Button signup;
    EditText name,email,pass,repass;
    private  AwesomeValidation mAwesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
    private FirebaseAuth mAuth;
    CheckBox checkBox;
    int value=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name=(EditText)findViewById(R.id.namesignup);
        email=(EditText)findViewById(R.id.userEmailsignup);
        pass=(EditText)findViewById(R.id.userPasswordsignup);
        repass=(EditText)findViewById(R.id.userCPasswordsignup);
        mAuth = FirebaseAuth.getInstance();



        signup=(Button)findViewById(R.id.signupsignup);
        checkBox=findViewById(R.id.checkbox);


        mAwesomeValidation.addValidation(this, R.id.namesignup, "[a-zA-Z\\s]+", R.string.err_firstname);
        mAwesomeValidation.addValidation(this, R.id.userEmailsignup, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);
        String regexPassword = "^(?=\\S+$).{8,}$";
        mAwesomeValidation.addValidation(this, R.id.userPasswordsignup, regexPassword, R.string.err_pass);
        mAwesomeValidation.addValidation(this, R.id.userCPasswordsignup, R.id.userPasswordsignup, R.string.err_repass);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
                if (checkBox.isChecked()){
                    onRegister();
                }
                else
                {
                    Toast.makeText(Signup.this, "Please accept Tearms of use and Provacy policy",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    public void onRegister()
    {
        final ProgressDialog progressDialog = new ProgressDialog(Signup.this,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        final String cemail = email.getText().toString();
        final String password = pass.getText().toString();
        mAuth.createUserWithEmailAndPassword(cemail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("AmbulanceDrivers").child(user_id);
                            current_user_db.setValue(true);
                            progressDialog.dismiss();
                            Toast.makeText(Signup.this, "Registered Successfully.",
                                    Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(Signup.this,MainActivity.class);
                            startActivity(i);

                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.i("TAG", "createUserWithEmail:failure");
                            Toast.makeText(getApplicationContext(),"E-mail id Already Exist",Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    private void submitForm() {
        mAwesomeValidation.validate();
        if(name.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter name",Toast.LENGTH_SHORT).show();
        }
        if(email.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter email",Toast.LENGTH_SHORT).show();
        }
        if(pass.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_SHORT).show();
        }
        if(repass.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please reenter password",Toast.LENGTH_SHORT).show();
        }
        else if( mAwesomeValidation.validate()==true)
        {
            value=1;
        }
    }

}
