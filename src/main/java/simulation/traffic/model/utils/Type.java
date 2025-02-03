package simulation.traffic.model.utils;

public enum Type {
    STEP,
    ADDVEHICLE;
    public static Type stringToType(String string){
        return switch(string){
            case "step" -> STEP;
            case "addVehicle" -> ADDVEHICLE;
            default -> throw new IllegalArgumentException();
        };
    }
}
