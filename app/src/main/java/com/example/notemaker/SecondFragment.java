package com.example.notemaker;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.notemaker.databinding.FragmentSecondBinding;

import java.time.LocalDateTime;

public class SecondFragment extends Fragment {

    private Button saveBtn, discardBtn;
    private EditText title, subtitle, note;
    private FragmentSecondBinding binding;

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
        note = getView().findViewById(R.id.editTextNote);

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
                             "yellow", //pls add the colour picker here
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
        //close db cuz dont want multi instances
        dbHelper.close();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}