package mandrik.nick.guessthenumber;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Random;

public class GuessActivity extends AppCompatActivity
        implements GestureOverlayView.OnGesturePerformedListener {

    public static final String APP_PREFERENCES = "settings_counts";
    public static final String APP_PREFERENCES_GUESSES = "count_guesses";
    public static final String APP_PREFERENCES_CLICKS = "count_clicks";

    GestureLibrary gLib;
    GestureOverlayView gestures;

    TextView infoView;
    TextView editNumber;
    TextView countInfo;

    // for this game
    Integer guessNumber;
    boolean isFinished;
    Integer countProb;

    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        infoView = (TextView)findViewById(R.id.infoView);
        editNumber = (TextView)findViewById(R.id.editText);
        countInfo = (TextView)findViewById(R.id.infoAttemps);

        gLib = GestureLibraries.fromRawResource(this, R.raw.gesture);
        if (!gLib.load()) {
            finish();
        }
        gestures = (GestureOverlayView) findViewById(R.id.geust);
        gestures.addOnGesturePerformedListener(this);

        // for this game
        Random rand = new Random();
        guessNumber = rand.nextInt(100);
        isFinished = false;
        countProb = 0;
        countInfo.setText("");
        editNumber.setText("");
    }

    public void onClick() {
        if (!isFinished){
            int inp = 100;
            try {
                inp = Integer.parseInt(editNumber.getText().toString());
            }
            catch(NumberFormatException ex) {
                infoView.setText(getResources().getString(R.string.error));
            }
            if(inp > 100 || inp < 0)
                infoView.setText(getResources().getString(R.string.error_input));
            else {
                if (inp > guessNumber)
                    infoView.setText(getResources().getString(R.string.ahead));
                if (inp < guessNumber)
                    infoView.setText(getResources().getString(R.string.behind));
                if (inp == guessNumber) {
                    MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.win3);
                    mPlayer.start();
                    infoView.setText(getResources().getString(R.string.hit));
                    isFinished = true;

                    SharedPreferences countSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor e = countSettings.edit();
                    e.putBoolean("hasVisited", true);
                    e.putInt(APP_PREFERENCES_GUESSES, countSettings.getInt(APP_PREFERENCES_GUESSES, 0) + 1);
                    e.putInt(APP_PREFERENCES_CLICKS, countSettings.getInt(APP_PREFERENCES_CLICKS, 0) + countProb + 1);
                    e.commit();
                    e.apply();

                    Intent intent = new Intent(this, StartActivity.class);
                    startActivity(intent);
                }
            }
            countProb++;
            countInfo.setText(getResources().getString(R.string.print_count) + " " + countProb);
        }
        else
        {
            countProb = 0;
            countInfo.setText("");
            Random rand = new Random();
            guessNumber = rand.nextInt(100);
            infoView.setText(getResources().getString(R.string.try_to_guess));
            isFinished = false;
        }
        editNumber.setText("");
        flag = false;
    }

    public void back(View v){
        switch (v.getId()) {
            case R.id.backButton:

                SharedPreferences countSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor e = countSettings.edit();
                e.putBoolean("hasVisited", true);
                e.putInt(APP_PREFERENCES_CLICKS, countSettings.getInt(APP_PREFERENCES_CLICKS, 0) + countProb );
                e.commit();
                e.apply();

                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        //Создаёт ArrayList c загруженными из gestures жестами
        ArrayList<Prediction> predictions = gLib.recognize(gesture);
        if (predictions.size() > 0) {
            //если загружен хотябы один жест из gestures
            Prediction prediction = predictions.get(0);
            if (prediction.score > 1.0) {
                if (prediction.name.equals("one"))
                    editNumber.setText(editNumber.getText() + "1");
                else if (prediction.name.equals("two"))
                    editNumber.setText(editNumber.getText() + "2");
                else if (prediction.name.equals("three"))
                    editNumber.setText(editNumber.getText() + "3");
                else if (prediction.name.equals("four"))
                    editNumber.setText(editNumber.getText() + "4");
                else if (prediction.name.equals("five"))
                    editNumber.setText(editNumber.getText() + "5");
                else if (prediction.name.equals("six"))
                    editNumber.setText(editNumber.getText() + "6");
                else if (prediction.name.equals("seven"))
                    editNumber.setText(editNumber.getText() + "7");
                else if (prediction.name.equals("eight"))
                    editNumber.setText(editNumber.getText() + "8");
                else if (prediction.name.equals("nine"))
                    editNumber.setText(editNumber.getText() + "9");
                else if (prediction.name.equals("zero"))
                    editNumber.setText(editNumber.getText() + "0");
                else if (prediction.name.equals("del"))
                    editNumber.setText(editNumber.getText().subSequence(0, editNumber.getText().length() - 1));
                else if (prediction.name.equals("stop"))
                    onClick();
            }else{
                editNumber.setText("ахахах");
            }

        }
    }
}
