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
        move();
        // + tracer
    }

    @Override
    public void move() {
        Point2D point2D = getPosition();
        double x = point2D.getX();
        double y = point2D.getY();
        x += s * Math.cos(Math.toRadians(getAngle()));
        y += s * Math.sin(Math.toRadians(getAngle()));
        state.setPosition(x,y);
    }

    @Override
    public void turnR() {
        double angle = getAngle();
        angle -= d;
        state.setAngle(angle);
    }

    @Override
    public void turnL() {
        double angle = getAngle();
        angle += d;
        state.setAngle(angle);
    }

    @Override
    public void push() {
        stateStack.push(state);
    }

    @Override
    public void pop() {
        stateStack.pop();
    }

    @Override
    public void stay() {
        // Not sure ?
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
