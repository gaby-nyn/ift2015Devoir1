package com.practiceProject;

import com.practiceProject.Interface.Turtle;
import com.practiceProject.Model.State;

import java.awt.geom.Point2D;
import java.util.Stack;

public class TurtleImpl implements Turtle {
    // step : length of an advance (move or draw)
    // delta : unit angle change in degrees (for turnR and turnL)
    private double step;
    private double delta;

    //  State (x,y,θ)
    private State state;

    // StateStack position & angle
    private Stack<State> stateStack = new Stack<>();

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
        x += step * Math.cos(Math.toRadians(getAngle()));
        y += step * Math.sin(Math.toRadians(getAngle()));
        state.setPosition(x,y);
    }

    @Override
    public void turnR() {
        double angle = getAngle();
        angle -= delta;
        state.setAngle(angle);
    }

    @Override
    public void turnL() {
        double angle = getAngle();
        angle += delta;
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
        this.step = step;
        this.delta = delta;
    }

    public Stack<State> getStateStack(){
        return stateStack;
    }
}
