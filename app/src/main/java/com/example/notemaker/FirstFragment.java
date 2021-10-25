package com.example.notemaker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.notemaker.databinding.FragmentFirstBinding;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //make db instance and get all notes
        NotesDao dbHelper = new NotesDao(getContext());
        List<NotesModel> allNotes = dbHelper.getAllNotes();
        //ArrayAdapter notesAdapter = new ArrayAdapter<NotesModel>(getContext(), android.R.layout.simple_list_item_1, allNotes);

        //test to see it gets all notes; pls put ur own code here
        // Toast.makeText(getContext(), allNotes.toString(), Toast.LENGTH_LONG).show();

        ArrayList<String> all_notes_arraylist = new ArrayList<>();
        for (int i = 0; i < allNotes.size(); i++)
            all_notes_arraylist.add(allNotes.get(i).toString());

        // Print the array list
        for (String item:all_notes_arraylist) {
            System.out.println(item);
        }

        ListView all_notes_list = getView().findViewById(R.id.all_notes_list);

        // Create adapter for list to display all notes
        ArrayAdapter all_notes_listview_adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                all_notes_arraylist
        ){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                view.setBackgroundColor(Color.parseColor(allNotes.get(position).getColour()));

                // Generate ListView Item using TextView
                return view;
            }
        };

        // Set adapter for list view
        all_notes_list.setAdapter(all_notes_listview_adapter);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_new_note);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}