package simulation.traffic.model.utils;

import java.util.Map;

public enum MapDirection {
    N,
    S,
    W,
    E;

    public static MapDirection stringToMapDirection(String string){
        return switch(string){
            case "north" -> N;
            case "east" -> E;
            case "south" -> S;
            case "west" -> W;
            default -> throw new IllegalStateException("Unexpected value: " + string);
        };
    }

    public static int parseDirection(MapDirection direction) {
        return switch(direction) {
            case N -> 0;
            case E -> 1;
            case S -> 2;
            case W -> 3;
        };
    }
    public static MapDirection intToDirection(int value){
        return switch (value % 4) {
            case 0 -> N;
            case 1 -> E;
            case 2 -> S;
            case 3 -> W;
            default -> throw new IllegalStateException("Unexpected value: " + value % 4);
        };
    }
    public MapDirection spinClockwise(int value) {
        return switch ((parseDirection(this) + value) % 4) {
            case 0 -> N;
            case 1 -> E;
            case 2 -> S;
            case 3 -> W;
            default -> throw new IllegalStateException("Unexpected value: " + ((parseDirection(this) + value) % 4));
        };
    }

    @Override
    public String toString() {
        return switch(this) {
            case N -> "^";
            case S -> "v";
            case W -> "<";
            case E -> ">";
        };
    }

}
