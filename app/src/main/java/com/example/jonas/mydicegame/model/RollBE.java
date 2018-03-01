package com.example.jonas.mydicegame.model;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

public class RollBE implements Serializable {

    int _roll1;
    int _roll2;

    LocalDateTime _time;

    public RollBE(int roll1, int roll2, LocalDateTime time)
    {
        _roll1 = roll1;
        _roll2 = roll2;
        _time = time;
    }

    public int getRoll1() {
        return _roll1;
    }

    public int getRoll2() {
        return _roll2;
    }

    public LocalDateTime getTime() {
        return _time;
    }
}
