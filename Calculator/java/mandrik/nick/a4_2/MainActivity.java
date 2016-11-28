package mandrik.nick.a4_2;


import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity implements GestureOverlayView.OnGesturePerformedListener {
    GestureLibrary gLib;
    GestureOverlayView gestures;
    TextView textViewResult;
    ImageButton imageButtonDeleteInput;
    boolean end = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = (TextView) findViewById(R.id.textViewResult);
        gLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
        imageButtonDeleteInput = (ImageButton) findViewById(R.id.imageButtonDeleteInput);
        imageButtonDeleteInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultExpr = textViewResult.getText().toString();
                if (end) {
                    textViewResult.setText("");
                    return;
                }
                if (resultExpr.length() != 0) {
                    textViewResult.setText(resultExpr.substring(0, resultExpr.length() - 1));
                }
            }
        });

        imageButtonDeleteInput.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                textViewResult.setText("");
                return true;
            }
        });
        if (!gLib.load()) {
            finish();
        }
        gestures = (GestureOverlayView) findViewById(R.id.gesture);
        gestures.addOnGesturePerformedListener(this);
    }




    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        if (end) {
            textViewResult.setText("");
            end = false;
        }
        //Создаёт ArrayList c загруженными из gestures жестами
        ArrayList<Prediction> predictions = gLib.recognize(gesture);
        if (predictions.size() > 0) {
            //если загружен хотя бы один жест из gestures
            Prediction prediction = predictions.get(0);
            if (prediction.score > 1.0) {
                if (prediction.name.equals("=")) {
                    parseExpression();
                    return;
                }
                textViewResult.append(prediction.name);
                return;
            }else{
                Toast.makeText(MainActivity.this, R.string.unknown_gesture, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseExpression() {
        String expr = textViewResult.getText().toString();
        Pattern pattern = Pattern.compile("(^\\-?[0-9]+\\.?[0-9]?)(\\+|\\-|\\*|\\/)(\\-?([0-9]+\\.?[0-9]?)$)");

        Matcher matcher = pattern.matcher(expr);

        ArrayList<String> stringArrayList = new ArrayList<>();
        if (!matcher.find()) {
            Toast.makeText(MainActivity.this, R.string.incorrect, Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 1; i < matcher.groupCount(); i++) {
                stringArrayList.add(matcher.group(i));
            }
            setResult(stringArrayList.get(0), stringArrayList.get(1), stringArrayList.get(2));
        }

    }

    private void setResult(String first, String sign, String second) {
        double firstValD = 0;
        double secondValD = 0;

        try {
            firstValD = Double.parseDouble(first);
            secondValD = Double.parseDouble(second);
        } catch (NumberFormatException exp) {
            Toast.makeText(MainActivity.this, R.string.incorrect, Toast.LENGTH_SHORT).show();
            return;
        }
        String resVal = "";
        switch (sign) {
            case "+":
                resVal = String.valueOf(firstValD + secondValD);
                break;
            case "-":
                resVal = String.valueOf(firstValD - secondValD);
                break;
            case "*":
                resVal = String.valueOf(firstValD * secondValD);
                break;
            case "/":

                if (Math.abs(secondValD) <= 0.000001) {
                    Toast.makeText(MainActivity.this, R.string.div_zero, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    resVal = String.valueOf(firstValD / secondValD);
                    break;
                }
            default:
                Toast.makeText(MainActivity.this, R.string.incorrect_sign, Toast.LENGTH_SHORT).show();
                return;
        }
        textViewResult.setText(resVal);
    }

}