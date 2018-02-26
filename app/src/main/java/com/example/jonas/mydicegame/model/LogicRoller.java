package com.example.jonas.mydicegame.model;

import java.util.Random;

/**
 * Created by jonas on 05-02-2018.
 */

public class LogicRoller implements ILogicRoller
{

    Random r;
    public LogicRoller()
    {
        r = new Random();
    }

    public int doRoll()
    {
        return r.nextInt(6) + 1;
    }
}
