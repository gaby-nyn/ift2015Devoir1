package com.practiceProject.Model;

import java.awt.geom.Point2D;

public class State {
    // Pos x,y & θ angle
    private double angle;
    private final Point2D position;

    public State(Point2D position, double angle){
        this.angle = angle;
        this.position = position;
    }

    public double getAngle(){
        return angle;
    }

    public Point2D getPosition(){
        return position;
    }

    public void setAngle(double newAngle){
        this.angle = newAngle;
    }
    public void setPosition(double x, double y){
        this.position.setLocation(x,y);
    }
}
