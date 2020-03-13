package com.lowbottgames.nauth.domain;

import java.util.Arrays;

public class DirectionManager {

    private DirectionRepository directionRepository;

    public DirectionManager(DirectionRepository directionRepository) {
        this.directionRepository = directionRepository;
    }

    public boolean authenticate(int[] directions) {
        int[] directionsStored = directionRepository.get();

        if (directionsStored == null
            || directions == null) {
            return false;
        }
        return Arrays.equals(directionsStored, directions);
    }

    public void changeDirection(int[] directions) {
        directionRepository.set(directions);
    }

    public boolean isEnrolled() {
        int[] directionsStored = directionRepository.get();
        return !Arrays.equals(new int[] {-1, -1, -1, -1}, directionsStored);
    }

}
