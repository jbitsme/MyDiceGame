package com.example.jonas.mydicegame.model;

import java.util.ArrayList;
import java.util.List;

public class ModelState {
    private static final ModelState modelState = new ModelState();

    public static ModelState getInstance() {
        return modelState;
    }

    ArrayList<RollBE> Rolls = new ArrayList<>();

    private ModelState() {

    }

    public void addRoll(RollBE roll)
    {
        Rolls.add(roll);
    }

    public void removeRoll(RollBE roll)
    {
        Rolls.remove(roll);
    }

    public ArrayList<RollBE> getRolls()
    {
        return this.Rolls;
    }

    public void clearRolls()
    {
        this.Rolls.clear();
    }
}
