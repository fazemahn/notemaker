package com.example.notemaker;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.notemaker.databinding.ActivityMainBinding;
import com.example.notemaker.NotesDao;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // method to search for notes
    public void searchNotes(View view) {
        System.out.println("Searching for notes");
        EditText search_edit_view = findViewById(R.id.search_edit_view);
        String search_value = search_edit_view.getText().toString();
        System.out.println("search value: " + search_value);
        NotesDao dbHelper = new NotesDao(this);
        List<NotesModel> searched_notes = dbHelper.getNotesByTitleSearch(search_value);
        System.out.println("Number of notes: " + searched_notes.size() + " NOTES: " + searched_notes.toString());


        ArrayList<String> searched_notes_arraylist = new ArrayList<>();
        for (int i = 0; i < searched_notes.size(); i++)
            searched_notes_arraylist.add(searched_notes.get(i).toString());

        // Print the array list
        for (String item: searched_notes_arraylist) {
            System.out.println(item);
        }

        ListView search_notes_list = findViewById(R.id.all_notes_list);

        // Create adapter for list to display all notes
        ArrayAdapter searched_notes_listview_adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                searched_notes_arraylist
        );

        // Set adapter for list view
        search_notes_list.setAdapter(searched_notes_listview_adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}