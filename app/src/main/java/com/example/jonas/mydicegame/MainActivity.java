package com.example.jonas.mydicegame;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonas.mydicegame.model.ILogicRoller;
import com.example.jonas.mydicegame.model.LogicRoller;
import com.example.jonas.mydicegame.model.ModelState;
import com.example.jonas.mydicegame.model.RollBE;
import com.example.jonas.mydicegame.model.ShakeDetector;
import com.example.jonas.mydicegame.model.SwipeListener;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btnRoll;
    Button btnClear;

    ImageView image1;
    ImageView image2;

    ProgressBar progressBar;
    TextView procent;

    LinearLayout listHistory;
    LinearLayout mainLayout;

    ILogicRoller logic;

    int numbRolls = 0;

    int historyLength = 10;

    private ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    boolean canRoll = true;

    ModelState modelState = ModelState.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                clickRoll();
            }
        });

        btnRoll = findViewById(R.id.btnRoll);
        btnRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickRoll();
            }
        });
        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickClear();
            }
        });

        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);

        progressBar = findViewById(R.id.progressBar);
        procent = findViewById(R.id.procent);

        listHistory = findViewById(R.id.listHistory);
        mainLayout = findViewById(R.id.mainLayout);

        logic = new LogicRoller();

        mainLayout.setOnTouchListener(new SwipeListener(MainActivity.this) {
            public void onSwipeLeft() {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                finish();
            }
        });

        if (savedInstanceState != null)
        {
            numbRolls = savedInstanceState.getInt("numRolls");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    private void clickRoll() {
        if (canRoll) {
            canRoll = false;

            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 750 milliseconds
            v.vibrate(750);

            btnRoll.setEnabled(false);
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                int count = 0;

                // Animation
                @Override
                public void run() {
                    int rollA = logic.doRoll();
                    int rollB = logic.doRoll();

                    setDice(rollA, image1);
                    setDice(rollB, image2);
                    count++;
                    if (count == 10) {
                        roll(rollA, rollB);
                        btnRoll.setEnabled(true);
                        canRoll = true;
                        return;
                    }
                    handler.postDelayed(this, 75L);
                }
            };
            runnable.run();
        }
    }

    private void roll(int one, int two)
    {
        if (numbRolls == historyLength) {
            numbRolls = 0;
            listHistory.removeAllViewsInLayout();
            clickRoll();
        }

        numbRolls++;
        int roll1 = logic.doRoll();
        setDice(roll1, image1);
        int roll2 = logic.doRoll();
        setDice(roll2, image2);
        int sum = roll1 + roll2;

        modelState.addRoll(new RollBE(roll1, roll2, LocalDateTime.now()));

        TextView newText = new TextView(this);
        newText.setText("Roll " + numbRolls + ": " + roll1 + " + " + roll2 + " = " + sum);
        listHistory.addView(newText);

        int progress = (100/historyLength)*numbRolls;
        progressBar.setProgress(progress);
        procent.setText(progress+"/100");

    }

    private void clickClear() {
        numbRolls = 0;
        listHistory.removeAllViewsInLayout();
        progressBar.setProgress(0);
        procent.setText("0/100");
    }

    public void setDice(int number, ImageView view) {
        switch (number) {
            case 1:
                view.setImageResource(R.drawable.one);
                break;
            case 2:
                view.setImageResource(R.drawable.two);
                break;
            case 3:
                view.setImageResource(R.drawable.three);
                break;
            case 4:
                view.setImageResource(R.drawable.four);
                break;
            case 5:
                view.setImageResource(R.drawable.five);
                break;
            case 6:
                view.setImageResource(R.drawable.six);
                break;
            default:
                break;
        }
    }

    protected void onSaveInstance(Bundle state)
    {
        super.onSaveInstanceState(state);
        state.putInt("numRolls", numbRolls);
    }
}
