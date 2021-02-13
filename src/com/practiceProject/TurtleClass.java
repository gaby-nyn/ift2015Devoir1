package com.practiceProject;

import java.awt.geom.Point2D;
import java.util.Stack;

public class TurtleClass implements Turtle{
    // step : length of an advance (move or draw)
    // delta : unit angle change in degrees (for turnR and turnL)
    double s, d;

    //  State (x,y,θ)
    State state;

    // StateStack position & angle
    Stack<State> stateStack = new Stack<>();

    @Override
    public void draw() {

    }

    @Override
    public void move() {

    }

    @Override
    public void turnR() {
        //angle -= d;
    }

    @Override
    public void turnL() {
       // angle += d;
    }

    @Override
    public void push() {

    }

    @Override
    public void pop() {

    }

    @Override
    public void stay() {

    }

    @Override
    public void init(Point2D pos, double angle_deg) {
       state = new State(pos,angle_deg);
    }

    @Override
    public Point2D getPosition() {
        return state.getPosition();
    }

    @Override
    public double getAngle() {
        return state.getAngle();
    }

    @Override
    public void setUnits(double step, double delta) {
        s = step;
        d = delta;
    }
}
