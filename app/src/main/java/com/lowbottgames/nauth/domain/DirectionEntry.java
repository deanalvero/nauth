package com.lowbottgames.nauth.domain;

import java.util.ArrayList;
import java.util.List;

public class DirectionEntry {

    public static final int TYPE_AUTHENTICATE = 0;
    public static final int TYPE_CHANGE_DIRECTION = 1;

    private DirectionEntry.Listener listener;
    private List<Integer> directions = new ArrayList<>();

    public DirectionEntry(DirectionEntry.Listener listener) {
        this.listener = listener;
    }

    public void enter(int input) {
        directions.add(input);

        if (listener != null) {
            listener.onInputCount(directions.size());
        }

        if (directions.size() == 4) {
            if (listener != null) {
                listener.onDirectionsEntered(toArray(directions));
            }
            directions.clear();
        }
    }

    private int[] toArray(List<Integer> integerList) {
        int size = integerList.size();
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = integerList.get(i);
        }
        return result;
    }

    public void delete() {
        int size = directions.size();
        if (size > 0) {
            directions.remove(size - 1);
        }

        if (listener != null) {
            listener.onInputCount(directions.size());
        }
    }

    public interface Listener {
        void onInputCount(int count);
        void onDirectionsEntered(int[] directions);
    }

}
