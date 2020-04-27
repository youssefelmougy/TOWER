package com.example.tower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import static com.example.tower.MainActivity.loggedIn;

public class AccountSettings extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextView name;
    TextView id;
    TextView newPassTitle;
    TextView currentPassTitle;
    TextView strength;
    EditText email;
    EditText currentPass;
    EditText newPass;
    Boolean emailChanged = false;
    Boolean passChanged = false;
    String userEmail;
    String password;
    Button submit;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        name = findViewById(R.id.name_final);
        id = findViewById(R.id.id_final);
        email = findViewById(R.id.email_change);
        currentPass = findViewById(R.id.original_pass);
        newPass = findViewById(R.id.new_pass);
        newPassTitle = findViewById(R.id.new_pass_title);
        currentPassTitle = findViewById(R.id.current_pass_title);
        strength = findViewById(R.id.strength);
        submit = findViewById(R.id.confirm_changes_button);
        
        loadInformation();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.settings);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), Search.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        if(loggedIn == false) {
                            startActivity(new Intent(getApplicationContext(), Profile.class));
                            overridePendingTransition(0, 0);
                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), ProfileLogin.class));
                            overridePendingTransition(0,0);
                        }
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;

            }
        });
    }

    public void goBack(View view) {
        onBackPressed();
        overridePendingTransition(0,0);
    }

    public void loadInformation() {
        DatabaseReference ref = database.getReference().child("students");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    if (student.getId() == MainActivity.id) {
                        name.setText(student.getName());
                        id.setText(String.valueOf(student.getId()));
                        email.setText(student.getEmail());
                        userEmail = student.getEmail();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void editEmail (View view) {
        if (emailChanged) {
            if (!passChanged) {
                submit.setVisibility(View.GONE);
                currentPass.setVisibility(View.GONE);
                currentPassTitle.setVisibility(View.GONE);
            }
            emailChanged = false;
            email.setFocusable(false);
            email.setFocusableInTouchMode(false);
            email.setClickable(false);
            email.setText(userEmail);
        }
        else {
            submit.setVisibility(View.VISIBLE);
            emailChanged = true;
            currentPassTitle.setVisibility(View.VISIBLE);
            currentPass.setVisibility(View.VISIBLE);
            email.setFocusable(true);
            email.setFocusableInTouchMode(true);
            email.setClickable(true);
            email.setSelectAllOnFocus(true);
            email.requestFocus();
        }
        }
        

    public void editPassword (View view) {
        if (!passChanged) {
            passChanged = true;
            submit.setVisibility(View.VISIBLE);
            currentPassTitle.setVisibility(View.VISIBLE);
            strength.setVisibility(View.VISIBLE);
            currentPass.setVisibility(View.VISIBLE);
            newPassTitle.setText("New Password");
            newPass.setText("");
            newPass.setFocusable(true);
            newPass.setFocusableInTouchMode(true);
            newPass.setClickable(true);
            newPass.requestFocus();
            newPass.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    
                }
    
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    calculatePasswordStrength(s.toString());
                }
    
                @Override
                public void afterTextChanged(Editable s) {
    
                }
            });
        } else {
            if (!emailChanged) {
                submit.setVisibility(View.GONE);
                currentPass.setVisibility(View.GONE);
                currentPassTitle.setVisibility(View.GONE);
            }
            passChanged = false;
            strength.setVisibility(View.GONE);
            newPass.setFocusable(false);
            newPass.setFocusableInTouchMode(false);
            newPass.setClickable(false);
            newPass.setText("password");
        }
    }

    public void submitChanges(View view) throws Exception{
        try {
            final FirebaseUser user = mAuth.getCurrentUser();
            EditText[] etArr = {email, currentPass, newPass};
            for (EditText et: etArr) {
                if (et.getText().toString().equals("")) {
                    throw new Exception("Don't Leave Any Information Blank");
                }
            }
            AuthCredential credential = EmailAuthProvider.getCredential(userEmail, currentPass.getText().toString());

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if (emailChanged) {
                            user.updateEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AccountSettings.this, "Email Has Been Changed", Toast.LENGTH_LONG).show();
                                        emailChanged = false;
                                        email.setFocusable(false);
                                        email.setFocusableInTouchMode(false);
                                        email.setClickable(false);
                                        userEmail = email.getText().toString();
                                        email.setText(userEmail);
                                        if (!passChanged) {
                                            submit.setVisibility(View.GONE);
                                            currentPass.setVisibility(View.GONE);
                                            currentPassTitle.setVisibility(View.GONE);
                                        }
                                    }
                                    else {
                                        Toast.makeText(AccountSettings.this, "User with this Email Already Exists", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                        }
                        if (passChanged) {
                            passChanged = false;
                            user.updatePassword(newPass.getText().toString());
                            if (!emailChanged) {
                                submit.setVisibility(View.GONE);
                                currentPass.setVisibility(View.GONE);
                                currentPassTitle.setVisibility(View.GONE);
                            }
                            strength.setVisibility(View.GONE);
                            newPass.setFocusable(false);
                            newPass.setFocusableInTouchMode(false);
                            newPass.setClickable(false);
                            newPass.setText("password");

                            Toast.makeText(AccountSettings.this, "Password Has Been Changed", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(AccountSettings.this, "Incorrect Password", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AccountSettings.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        currentPass.setText("");
    }

    private void calculatePasswordStrength(String password) {
        PasswordStrength passwordStrength = PasswordStrength.calculate(password);
        strength.setText(passwordStrength.msg);
        strength.setTextColor(passwordStrength.color);

    }

}
