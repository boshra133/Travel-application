package com.example.firebase;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.security.Key;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class profileFragment extends Fragment {

FirebaseAuth firebaseAuth;
FirebaseUser user;
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
StorageReference storageReference;
String storagePath = "Users_Profile_imgs/";
ImageView avatar;
TextView nameTv, emailTv;
FloatingActionButton fab;

private static final int CAMERA_REQUEST_CODE = 100;
private static final int STORAGE_REQUEST_CODE = 200;

private static final int IMAGE_PICK_CAMERA_CODE = 300;
private static final int IMAGE_PICK_GALLERY_CODE = 400;

String cameraPermissions[];
String storagePermissions[];
Uri image_uri;
ProgressDialog pd;
String profileOrCoverPhoto;
    String uid;
    public profileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
      firebaseAuth = FirebaseAuth.getInstance();
      user = firebaseAuth.getCurrentUser();
      firebaseDatabase = FirebaseDatabase.getInstance();
      databaseReference = firebaseDatabase.getReference("Users");
      storageReference = getInstance().getReference();
      avatar= view.findViewById(R.id.avatarImg);
      nameTv= view.findViewById(R.id.nameTv);
      emailTv= view.findViewById(R.id.emailTv);
      fab =view.findViewById(R.id.fab);

      pd = new ProgressDialog(getActivity());

        cameraPermissions = new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //read from database
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String name = ""+ds.child("name").getValue();
                    String email = ""+ds.child("email").getValue();
                    String image = ""+ds.child("image").getValue();

                    //set data
                    nameTv.setText(name);
                    emailTv.setText(email);
                    try {

                     Picasso.get().load(image).into(avatar);

                    }
                    catch (Exception e){
                        Picasso.get().load(R.drawable.ic_add_image).into(avatar);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            });

        //fab button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowEditProfileDialog();
            }
        });


        return view;
    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        requestPermissions(storagePermissions,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        requestPermissions(cameraPermissions,CAMERA_REQUEST_CODE);
    }

    private void ShowEditProfileDialog() {
        String options []= {"Edit profile picture","edit name","Change password","delete account","logout"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("choose action");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(which == 0){
                    //edit profile pic
                    pd.setMessage("Updating profile picture");
                    profileOrCoverPhoto = "image";
                    showImagePicDialog();
                }
                else if(which ==1){
                    //edit name
                    pd.setMessage("Updating Name");
                    showNameUpdateDialog("name");
                }
                else if(which ==2){
                    //change
                    pd.setMessage("Changing password");
                    showChangePasswordDialog();
                }
                else if(which ==3){
                    //delete
                    uid = user.getUid();
                    delete(uid);
                }
                else if(which ==4){
                    //sinout
                    firebaseAuth.signOut();
                    checkUserStatus();
                     }
                  }
                });
        builder.create().show();
    }
    private void delete(String uid){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Are you sure?");
        dialog.setMessage("deleting this account will result in completely removing your account from the system " +
            "and you won't able to access the app");

        final  DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users").child(uid);
            //delete
        dialog.setPositiveButton("delete", new DialogInterface.OnClickListener() {
        @Override
        ///dalete user
            public void onClick(DialogInterface dialogInterface, int i) {
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "user account deleted", Toast.LENGTH_LONG).show();
                        ref.removeValue();
                        startActivity(new Intent(getActivity(),MainActivity.class));
                        getActivity().finish();

                    }
                    else {
                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                    } }}); } });

    dialog.setNegativeButton("dismiss", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    });
    AlertDialog alertDialog = dialog.create();
    alertDialog.show();
}

 private void showChangePasswordDialog() {
        View view =LayoutInflater.from(getActivity()).inflate(R.layout.dialog_update_pass,null);

        final EditText password1 = view.findViewById(R.id.passwordE);
        final EditText NewPassword1 = view.findViewById(R.id.NewPasswordE);
        Button updatePasswordBtn = view.findViewById(R.id.updatePasswordBtn);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(view);

            final AlertDialog dialog = builder.create();
            dialog.show();

            updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String oldpassword = password1.getText().toString().trim();
                    String newpassword = NewPassword1.getText().toString().trim();


                    if(TextUtils.isEmpty(oldpassword)){
                        Toast.makeText(getActivity(), "Enter your current password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(newpassword.length()<6){
                        Toast.makeText(getActivity(), "password length must at least 6 characters...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    dialog.dismiss();
                    updatePassword(oldpassword,newpassword);
                }
            });

    }

    private void updatePassword(String oldpassword, final String newpassword) {
            pd.show();

           final FirebaseUser user = firebaseAuth.getCurrentUser();
            //re authentication
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(),oldpassword);

        user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //successfully auth begin update

                        // update
                        user.updatePassword(newpassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // password updated
                                pd.dismiss();
                                Toast.makeText(getActivity(), "Password Updated...", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //failed updated
                                pd.dismiss();
                                Toast.makeText(getContext(),"" +e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getContext(),"" +e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNameUpdateDialog(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("update "+key);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        final EditText editText = new EditText(getActivity());
        editText.setHint("Enter"+key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);
        builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = editText.getText().toString().trim();
                if(!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key,value);
                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(),"updated...",Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                else{
                    Toast.makeText(getActivity(),"please enter "+key,Toast.LENGTH_SHORT).show();

                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pd.dismiss();
            }
        });
        builder.create().show();
    }

    private void showImagePicDialog() {
        String options []= {"Camera","gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(which == 0){
                    //camera
                   if(!checkCameraPermission()){
                       requestCameraPermission();
                   }
                   else{
                       pickFromCamera();
                }}
                else if(which ==1){
                    //gallery
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else{
                pickFromGallary();
                    }}
            }
        });
        builder.create().show();
    }


    private void checkUserStatus() {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
        } else {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(getActivity(), "camera & storage both permissions are neccessary...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE :{
                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickFromGallary();
                    }
                    else{
                        Toast.makeText(getActivity(),"storage permissions neccessary...",Toast.LENGTH_SHORT).show();
                    }

                }
            }break;

        }
    }

    private void pickFromGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp pic");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                image_uri = data.getData();
                uploadProfileAvtar(image_uri);

            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                uploadProfileAvtar(image_uri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileAvtar(final Uri uri) {
        pd.show();
        String filePathAndName = storagePath+""+profileOrCoverPhoto+"_"+user.getUid();
    StorageReference storageReference2nd= storageReference.child(filePathAndName);
    storageReference2nd.putFile(uri)
    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while(!uriTask.isSuccessful());
            Uri downloadUri = uriTask.getResult();

            if(uriTask.isSuccessful()){
                HashMap<String ,Object> results = new HashMap<>();

                results.put(profileOrCoverPhoto,downloadUri.toString());
                databaseReference.child(user.getUid()).updateChildren(results)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pd.dismiss();
                                Toast.makeText(getContext(),"image updated...",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getContext(),"error updating image...",Toast.LENGTH_SHORT).show();

                    }
                });
            }
            else{
                pd.dismiss();
                Toast.makeText(getContext(),"some error occured",Toast.LENGTH_SHORT).show();

            }
        }
    })
    .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            pd.dismiss();
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    });
    }
}
