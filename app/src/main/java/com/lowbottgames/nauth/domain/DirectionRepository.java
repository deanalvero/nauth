package com.lowbottgames.nauth.domain;

public interface DirectionRepository {
    void set(int[] directions);
    int[] get();
}
