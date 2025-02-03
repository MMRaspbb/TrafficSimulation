package simulation.traffic.model.utils;

public record Command(Type type, String vehicleId, MapDirection startRoad, MapDirection endRoad) {
    public Command(Type type){
        this(type, null, null, null);
    }
    @Override
    public String toString(){
        if(type == Type.STEP){
            return "step";
        }
        return "Adding a vehicle with ID: " + vehicleId + " on road: " + startRoad + " with destination: " + endRoad;
    }
}
