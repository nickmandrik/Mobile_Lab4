package mandrik.nick.remwords;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SharedPreferences countSettings;

    GestureLibrary gLib;
    GestureOverlayView gestures;

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView)findViewById(R.id.text);

        countSettings = getSharedPreferences("text_set", Context.MODE_PRIVATE);
        boolean hasVisited = countSettings.getBoolean("hasVisited", false);

        if (!hasVisited) {
            SharedPreferences.Editor e = countSettings.edit();
            e.putBoolean("hasVisited", true);
            e.commit();
            e.apply();
        }
        text.setText(countSettings.getString("Text", "Please, add marks"));
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, AddMarkActivity.class);
        startActivity(intent);
    }

    public void clear(View v) {
        SharedPreferences.Editor e = countSettings.edit();
        e.clear();
        e.commit();
        text.setText("");
        Toast.makeText(getApplicationContext(),"Text clear",Toast.LENGTH_SHORT).show();
    }
}
