package com.msiSpringchat.springchat.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.msiSpringchat.springchat.Holder.UserHolder;
import com.msiSpringchat.springchat.R;

import java.io.InputStream;

public class UpdateDeitlsActivity extends AppCompatActivity {
    ProgressBar UpdatProgressbar;
    de.hdodenhof.circleimageview.CircleImageView updateProfileImg;

    ImageButton update;

    EditText gender,barthDate,name;
    Uri filepath;
    Bitmap bitmap;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_deitls);

        UpdatProgressbar = findViewById(R.id.UpdatProgressbar);

        updateProfileImg = findViewById(R.id.updateProfileImg);

        update = findViewById(R.id.update);

        gender = findViewById(R.id.gender);
        barthDate = findViewById(R.id.editTextDate);
        name = findViewById(R.id.name);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase  = FirebaseDatabase.getInstance();


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addinintofirebase();


            }
        });

        updateProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(UpdateDeitlsActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                opencamera();

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                if (permissionDeniedResponse.isPermanentlyDenied()){
                                    showSettingsDialog();

                                }

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();

                            }
                        }).check();
            }
        });



    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDeitlsActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void opencamera() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Select Image File"),1);
    }

    private void addinintofirebase() {

        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("File Uploading...");
        dialog.show();



        DatabaseReference databaseReference=firebaseDatabase.getReference("users");
        FirebaseStorage storage= FirebaseStorage.getInstance();
        final StorageReference uploader=storage.getReference().child("Profiles").child(firebaseAuth.getUid());


        uploader.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String uid = firebaseAuth.getUid();
                        FirebaseUser firebaseUser  = firebaseAuth.getCurrentUser();

                        UserHolder userHolder = new UserHolder(name.getText().toString(),uri.toString(),barthDate.getText().toString(),gender.getText().toString(),uid);
                        databaseReference.child(firebaseUser.getUid()).setValue(userHolder);
                        Intent gotohomei = new Intent(UpdateDeitlsActivity.this,HomeActivity.class);
                        startActivity(gotohomei);
                        finish();

                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                float percent=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                dialog.setMessage("Uploaded :"+(int)percent+" %");

            }
        });


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1  && resultCode==RESULT_OK)
        {
            filepath=data.getData();
            try{
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                updateProfileImg.setImageBitmap(bitmap);
            }catch (Exception ex)
            {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}