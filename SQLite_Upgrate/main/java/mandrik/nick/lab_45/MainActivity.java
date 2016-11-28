package mandrik.nick.lab_45;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import mandrik.nick.lab_45.Add;
import mandrik.nick.lab_45.Edit;
import mandrik.nick.lab_45.Info;
import matyash.lab44.R;


public class MainActivity extends ListActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] act = new String[3];
        act[0] = (String) this.getText(R.string.info);
        act[1] = (String) this.getText(R.string.add);
        act[2] = (String) this.getText(R.string.edit);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, act);
        setListAdapter(adapter);
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, Info.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(MainActivity.this, Add.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(MainActivity.this, Edit.class);
                        startActivity(intent2);
                        break;
                }
                Toast.makeText(getApplicationContext(),(String) getText(R.string.choose) +  " " +
                                parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }
        };
        getListView().setOnItemClickListener(itemListener);
    }



}


