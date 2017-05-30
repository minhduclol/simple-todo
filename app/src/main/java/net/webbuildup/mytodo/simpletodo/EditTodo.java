package net.webbuildup.mytodo.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

public class EditTodo extends AppCompatActivity {
    ArrayAdapter<String> itemAdapter;
    String item, action;
    TextView tvItem;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        Bundle extra = getIntent().getExtras();
        tvItem = (TextView) findViewById(R.id.etEditItem);
        item = extra.getString("item");
        pos = extra.getInt("pos");
        tvItem.setText(item);


    }

    public void saveItem(View view) {
        item = tvItem.getText().toString();
        action = "save";
        finish();
    }
    public void deleteItem(View view) {
        action = "delete";
        finish();
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("text", item);
        data.putExtra("pos", pos);
        data.putExtra("action", action);
        setResult(RESULT_OK, data);
        try {
            super.finish();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
