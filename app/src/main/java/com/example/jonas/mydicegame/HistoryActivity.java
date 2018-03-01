package com.example.jonas.mydicegame;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jonas.mydicegame.model.ModelState;
import com.example.jonas.mydicegame.model.RollBE;
import com.example.jonas.mydicegame.model.SwipeListener;

import java.util.ArrayList;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {

    Button btnClear;

    ListView listViewHistory;

    LinearLayout mainLayout;

    ModelState modelState = ModelState.getInstance();

    RollAdapter ra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        btnClear = findViewById(R.id.btnClear);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearRolls();
            }
        });

        listViewHistory = findViewById(R.id.listViewHistory);

        mainLayout = findViewById(R.id.mainLayout);

        mainLayout.setOnTouchListener(new SwipeListener(HistoryActivity.this) {
            public void onSwipeRight() {
                startActivity(new Intent(HistoryActivity.this, MainActivity.class));
                finish();
            }
        });

        listViewHistory.setOnTouchListener(new SwipeListener(HistoryActivity.this) {
            public void onSwipeRight() {
                startActivity(new Intent(HistoryActivity.this, MainActivity.class));
                finish();
            }
        });


        ra = new RollAdapter(this, R.layout.cell, modelState.getRolls());
        listViewHistory.setAdapter(ra);
    }

    public void clearRolls() {
        modelState.clearRolls();
        listViewHistory.setAdapter(ra);
    }

    class RollAdapter extends ArrayAdapter<RollBE> {
        

        private ArrayList<RollBE> rolls;



        public RollAdapter(Context context, int textViewResourceId,
                             ArrayList<RollBE> rolls) {
            super(context, textViewResourceId, rolls);
            this.rolls = rolls;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {

            if (v == null) {
                LayoutInflater li = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

                v = li.inflate(R.layout.cell, parent,false);
            }

            RollBE roll = rolls.get(position);

            TextView time = v.findViewById(R.id.time);
            ImageView dice1 = v.findViewById(R.id.dice1);
            ImageView dice2 = v.findViewById(R.id.dice2);

            String hour = Integer.toString(roll.getTime().getHour());
            if (hour.length() < 2)
            {
                hour = "0" + hour;
            }
            String minute = Integer.toString(roll.getTime().getMinute());
            if (minute.length() < 2)
            {
                minute = "0" + minute;
            }
            String second = Integer.toString(roll.getTime().getSecond());
            if (second.length() < 2)
            {
                second = "0" + second;
            }
            time.setText(hour + ":" + minute + ":" + second);

            setDice(roll.getRoll1(), dice1);
            setDice(roll.getRoll2(), dice2);


            return v;
        }
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
}
