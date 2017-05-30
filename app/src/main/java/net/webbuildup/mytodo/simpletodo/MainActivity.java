package net.webbuildup.mytodo.simpletodo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.data;
import static org.apache.commons.io.FileUtils.readLines;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 200;
    ArrayList <String> items;
    ArrayAdapter<String> itemAdapter;
    ListView lvItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvlItems);
        //load items from file
        readItems();
        //display items
        itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemAdapter);
        setupListViewListener();
    }

    /**
     * handle Add Item button click
     * @param view
     */
    public void onAddItem(View view) {
        try {
            EditText etNewItem = (EditText) findViewById(R.id.etNewItems);
            String itemText = etNewItem.getText().toString();
            itemAdapter.add(itemText);
            etNewItem.setText("");
            writeItems();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * create event listener for ToDo list remove item feature
     */
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                    return removeItem(pos);
                }
            });
        lvItems.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                    editItem(item, pos);
                }
            });
    }
    private void editItem(View view, int pos) {
        Intent i = new Intent(this, EditTodo.class);
        i.putExtra("item", items.get(pos).toString());
        i.putExtra("pos", pos);
        try {
            startActivityForResult(i, REQUEST_CODE);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private boolean removeItem(int pos) {
        try {
            items.remove(pos);
            itemAdapter.notifyDataSetChanged();
            writeItems();
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    private boolean updateItem(int pos, String todo) {
        try {
            items.set(pos, todo);
            itemAdapter.notifyDataSetChanged();
            writeItems();
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Bundle extra = data.getExtras();
            String action = extra.getString("action");
            int pos = extra.getInt("pos");
            if (action.equals("delete")) {
                removeItem(pos);
                Toast.makeText(this, "Todo deleted",
                        Toast.LENGTH_SHORT).show();
            }
            else if (action.equals("save")) {
                String text = extra.getString("text");
                updateItem(pos, text);
                Toast.makeText(this, "Todo saved",
                        Toast.LENGTH_SHORT).show();
            }


        }
    }
    /**
     * read saved todo from file
     */
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }
    /**
     *  save todo to file
     */
    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
