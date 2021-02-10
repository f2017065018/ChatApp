package com.example.chatapp.Fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.chatapp.R;
import com.example.chatapp.model.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile_Fragment extends Fragment {

    TextView user_name;
    ImageView image_view;

    DatabaseReference reference;
    FirebaseUser fuse;

    //Store Image of Profile
    StorageReference storageReference;

    private Uri image_uri;
    private static final int image_request = 1;
    private StorageTask upload_task;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profile_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile_Fragment newInstance(String param1, String param2) {
        Profile_Fragment fragment = new Profile_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate((R.layout.fragment_profile_), container, false);
        user_name = view.findViewById(R.id.username2);
        image_view = view.findViewById(R.id.image_profile2);

        //store reference of profile image
        storageReference = FirebaseStorage.getInstance().getReference("Uploads");

        fuse = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("My User's").child(fuse.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                user_name.setText(users.getUsername());

                if (users.getImageurl().equals("defolt")) {
                    image_view.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getContext()).load(users.getImageurl()).into(image_view);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slect_image();
            }
        });


        return view;
    }

    private String get_file_extention(Uri URI) {
        ContentResolver content_resolver = getContext().getContentResolver();

        MimeTypeMap mime_type_map = MimeTypeMap.getSingleton();

        return mime_type_map.getExtensionFromMimeType(content_resolver.getType(URI));
    }


    private void slect_image() {
        Intent j = new Intent();
        j.setType("Image");
        j.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(j, image_request);

    }

    private void upload_my_image() {
        final ProgressDialog progress_dialog = new ProgressDialog(getContext());

        progress_dialog.setMessage("Image Uploding");
        progress_dialog.show();

        if (image_uri != null) {
            final StorageReference file_reference = storageReference.child(System.currentTimeMillis() + "." + get_file_extention(image_uri));

            upload_task = file_reference.putFile(image_uri);
            upload_task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return file_reference.getDownloadUrl();

                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful())
                            {

                                Uri download_uri = task.getResult();

                                String my_uri = download_uri.toString();

                                reference = FirebaseDatabase.getInstance().getReference("My User's").child(fuse.getUid());

                                HashMap<String, Object> map = new HashMap<>();

                                map.put("Image_URL", my_uri);
                                reference.updateChildren(map);

                                progress_dialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(getContext(), "! Failed..", Toast.LENGTH_SHORT).show();
                            }



                    }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            progress_dialog.dismiss();

                        }
                    });

        } else {
            Toast.makeText(getContext(), "Please Select Image", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onActivityResult(int request_Code, int result_Code, @Nullable Intent data)
    {
        super.onActivityResult(request_Code, result_Code, data);

        if(request_Code==RESULT_OK&&request_Code==image_request&&data!=null)
        {
            image_uri=data.getData();

            if(upload_task.isInProgress()&&upload_task!=null)
            {
                Toast.makeText(getContext(),"Uploding...",Toast.LENGTH_SHORT).show();
            }
            else
            {
                upload_my_image();
            }

        }
    }


}

