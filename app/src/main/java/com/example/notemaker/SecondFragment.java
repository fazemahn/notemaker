package com.example.notemaker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import static android.app.Activity.RESULT_OK;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.notemaker.databinding.FragmentSecondBinding;

import java.io.InputStream;
import java.time.LocalDateTime;

public class SecondFragment extends Fragment {

    private Button saveBtn, discardBtn, imgBtn;
    private EditText title, subtitle, note_colour, note;
    private FragmentSecondBinding binding;
    private ImageView image;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //init view items and db
        NotesDao dbHelper = new NotesDao(getContext());

        saveBtn = getView().findViewById(R.id.buttonSave);
        discardBtn = getView().findViewById(R.id.buttonDiscardChanges);
        title = getView().findViewById(R.id.editTextTitle);
        subtitle = getView().findViewById(R.id.editTextSubtitle);
        note_colour = getView().findViewById(R.id.editTextColour);
        note = getView().findViewById(R.id.editTextNote);
        imgBtn = getView().findViewById(R.id.buttonImage);
        image = getView().findViewById(R.id.image);
        //save listener
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotesModel newNote;

                //check if a title exist
                if (TextUtils.isEmpty(title.getText().toString())){
                    title.setError("Title required");
                    Toast.makeText(getContext(), "Title is required", Toast.LENGTH_SHORT).show();
                } else {
                    //make new note
                     newNote = new NotesModel(
                            title.getText().toString(),
                            subtitle.getText().toString(),
                            note.getText().toString(),
                             note_colour.getText().toString(), //pls add the colour picker here
                             LocalDateTime.now(),
                             LocalDateTime.now()
                    );
                     //add to db
                    long success = dbHelper.createNote(newNote);
                    Toast.makeText(getContext(), Long.toString(success), Toast.LENGTH_LONG).show();
                    //go back to main
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_save_note);
                }
            }
        });

        //discard listener
        discardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_discard_changes);
            }
        });

        // add image listener
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, REQUEST_CODE_STORAGE_PERMISSION);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_CODE_STORAGE_PERMISSION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, REQUEST_CODE_STORAGE_PERMISSION);
                } else {
                    Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //This entire commented section didnt work but i kept for reference
//    private void selectImg(){
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        if(intent.resolveActivity(getActivity().getPackageManager()) != null){
//            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
//        }
//    }
//
//    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0){
//            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                selectImg();
//            }
//        } else{
//            Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK){
//            if(data != null){
//                Uri selectedImageUri = data.getData();
//                if(selectedImageUri != null){
//                    try{
//                        InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedImageUri);
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        image.setImageBitmap(bitmap);
//                        image.setVisibility(View.VISIBLE);
//                    } catch(Exception exception){
//                        Toast.makeText(getContext(), exception.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        }
//    }

    //this function should display selected img
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && null != data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();


        image.setImageBitmap(BitmapFactory.decodeFile(picturePath));

    }
}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}