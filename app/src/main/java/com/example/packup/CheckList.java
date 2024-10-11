package com.example.packup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
// Ensure you have this import
import com.example.packup.R;

// Remove any incorrect imports like:
// import android.R;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.packup.adapter.CheckListAdapter;
import com.example.packup.constants.MyConstants;
import com.example.packup.data.AppData;
import com.example.packup.database.RoomDB;
import com.example.packup.models.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CheckList extends AppCompatActivity {
        RecyclerView recyclerView;
        CheckListAdapter checkListAdapter;
        RoomDB database;
        List<Items> itemsList = new ArrayList<>();
        String header, show;
        EditText txtAdd;
        Button btnAdd;
        LinearLayout linearLayout;


    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_one, menu);

        if(MyConstants.MY_SELECTIONS.equals(header)) {
            menu.getItem(0).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        } else if (MyConstants.MY_LIST_CAMEL_CASE.equals(header)) {
            menu.getItem(1).setVisible(false);
        }

        MenuItem menuItem = menu.findItem(R.id.btnSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Items> mFinalList = new ArrayList<>();
                for(Items items:itemsList) {
                    if(items.getItemname().toLowerCase().startsWith(newText.toLowerCase())) {
                        mFinalList.add(items);
                    }
                }
                updateRecycler(mFinalList);
                return false;
            }
        });
        return super.onCreatePanelMenu(featureId, menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, CheckList.class);
        AppData appData = new AppData(database, this);

        int itemId = item.getItemId();
        if (itemId == R.id.btnMySelections) {
            intent.putExtra(MyConstants.HEADER_SMALL, MyConstants.MY_SELECTIONS);
            intent.putExtra(MyConstants.SHOW_SMALL, MyConstants.FALSE_STRING);
            startActivityForResult(intent, 101);
            return true;
        }
        if(itemId == R.id.btnCustomList) {
            intent.putExtra(MyConstants.HEADER_SMALL, MyConstants.MY_LIST_CAMEL_CASE);
            intent.putExtra(MyConstants.SHOW_SMALL, MyConstants.TRUE_STRING);
            startActivity(intent);
            return true;
        }


        if(itemId == R.id.btnDeleteDefault) {
            new AlertDialog.Builder(this).setTitle("Delete default data")
                    .setMessage("Are you sure your want to delete all default items?\n\nThis means all the default items provided by (PackUp) while installing will be deleted.")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            appData.persistDataByCategory(header, true);
                            itemsList = database.mainDao().getAll(header);
                            updateRecycler(itemsList);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setIcon(R.drawable.baseline_warning_amber_24).show();
            return true;
        }

        if(itemId == R.id.btnReset) {
            new AlertDialog.Builder(this).setTitle("Reset to default?")
                    .setMessage("Are your sure you want to reset back to default items?")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            appData.persistDataByCategory(header, false);
                            itemsList = database.mainDao().getAll(header);
                            updateRecycler(itemsList);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setIcon(R.drawable.baseline_warning_amber_24).show();
            return true;
        }

        if(itemId == R.id.btnExit) {
            finishAffinity();
        }
        // Handle other menu items if necessary

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        header = intent.getStringExtra(MyConstants.HEADER_SMALL);
        show = intent.getStringExtra(MyConstants.SHOW_SMALL);

        getSupportActionBar().setTitle(header);

        txtAdd = findViewById(R.id.txtAdd);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        linearLayout = findViewById(R.id.linearlayout);
        database = RoomDB.getInstance(this);  // Ensure database is initialized properly

        // Conditional view visibility and data retrieval
        if (MyConstants.FALSE_STRING.equals(show)) {
            linearLayout.setVisibility(View.GONE);
            itemsList = database.mainDao().getAllSelected(true);  // Fetch filtered data
        } else {
            itemsList = database.mainDao().getAll(header);  // Fetch all data based on 'header'
        }

        // Call updateRecycler() only after data has been fetched
        updateRecycler(itemsList);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemnName = txtAdd.getText().toString();
                if(itemnName != null && !itemnName.isEmpty()) {
                    addNewItem(itemnName);
                    Toast.makeText(CheckList.this, "Item Added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CheckList.this, "Empty item cannot be added!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
       onBackPressed();
       return true;
    }

    private void addNewItem(String itemName) {
        Items item = new Items();
        item.setChecked(false);
        item.setCategory(header);
        item.setItemname(itemName);
        item.setAddedby(MyConstants.USER_SMALL);
        database.mainDao().saveItems(item);
        itemsList = database.mainDao().getAll(header);
        updateRecycler(itemsList);
        recyclerView.scrollToPosition(checkListAdapter.getItemCount() - 1);
        txtAdd.setText("");
    }



    private void updateRecycler(List<Items> itemsList) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        checkListAdapter = new CheckListAdapter(CheckList.this, itemsList, database, show);
        recyclerView.setAdapter(checkListAdapter);

    }
}