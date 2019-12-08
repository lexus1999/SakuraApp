package com.example.sakurarestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sakurarestaurant.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    MaterialEditText edtPhone,edtName,edtPassword;
    Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName = (MaterialEditText)findViewById(R.id.edtName);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);

        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        //Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = edtPhone.getText().toString();
                if (number.isEmpty() || number.length() < 10) {
                    edtPhone.setError("Number is Required");
                    edtPhone.requestFocus();
                    return;
                }
                else if ( number.length()>11) {
                    edtPhone.setError("Number is invalid");
                    edtPhone.requestFocus();
                    return;
                }
                String name = edtName.getText().toString();
                if (name.isEmpty()) {
                    edtName.setError("Name is Required");
                    edtName.requestFocus();
                    return;
                }
                String password = edtPassword.getText().toString();
                if (password.isEmpty()) {
                    edtPassword.setError("Password is Required");
                    edtPassword.requestFocus();
                    return;
                }

                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please wait..");
                mDialog.show();

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Check if already user phone registered
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "Phone number already registered! ", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(SignUp.this, "Successfully registered! ", Toast.LENGTH_SHORT).show();
                            User user = new User(edtName.getText().toString(),edtPassword.getText().toString());
                            table_user.child(edtPhone.getText().toString()).setValue(user);
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled( DatabaseError databaseError) {
                    }
                });
            }
        });

    }
}