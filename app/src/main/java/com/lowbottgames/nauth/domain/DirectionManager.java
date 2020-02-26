package com.lowbottgames.nauth.domain;

import java.util.Arrays;

public class DirectionManager {

    private int[] directions;

    public boolean authenticate(int[] directions) {
        if (this.directions == null
            || directions == null) {
            return false;
        }
        return Arrays.equals(this.directions, directions);
    }

    public void changeDirection(int[] directions) {
        this.directions = directions;
    }

}
