package com.practiceProject;

import java.awt.geom.Point2D;

public class State {
    // Pos x,y & θ angle
    private final double angle;
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
}
