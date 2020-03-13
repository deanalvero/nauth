package com.lowbottgames.nauth.device;

import android.content.Context;
import android.content.SharedPreferences;

import com.lowbottgames.nauth.domain.DirectionRepository;

public class DirectionRepositoryImpl implements DirectionRepository {

    private static final String KEY_DIRECTIONS = "KEY_DIRECTIONS";
    private SharedPreferences sharedPreferences;

    public DirectionRepositoryImpl(Context context) {
        sharedPreferences = context.getSharedPreferences("nauth", Context.MODE_PRIVATE);
    }

    @Override
    public void set(int[] directions) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        StringBuilder stringBuilder = new StringBuilder();
        for (int direction: directions) {
            stringBuilder.append(direction);
            stringBuilder.append(" ");
        }

        editor.putString(KEY_DIRECTIONS, stringBuilder.toString());
        editor.commit();
    }

    @Override
    public int[] get() {
        String string = sharedPreferences.getString(KEY_DIRECTIONS, "");
        String[] stringSplits = string.split(" ");
        if (stringSplits.length == 0) {
            return new int[0];
        }

        int[] result = new int[stringSplits.length];
        try {
            for (int i = 0; i < stringSplits.length; i++) {
                result[i] = Integer.parseInt(stringSplits[i]);
            }
        } catch (NumberFormatException e) {
            return new int[] {-1, -1, -1, -1};
        }

        return result;
    }

}
