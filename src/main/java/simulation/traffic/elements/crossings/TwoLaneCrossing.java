package simulation.traffic.elements.crossings;

import simulation.traffic.elements.Lane;
import simulation.traffic.elements.cars.Car;
import simulation.traffic.model.utils.CarDirection;
import simulation.traffic.model.utils.MapDirection;

import java.util.ArrayList;
import java.util.List;

public class TwoLaneCrossing implements Crossing{
    private static final int defaultCycle = 5;
    private int stepsToCyclesEnd = defaultCycle;
    private final List<List<Lane>> lanes = new ArrayList<>();
    //in inner List the first one is left turning lane, the second one is straight and right going
    private final List<Boolean> lightsCycle = new ArrayList<>(List.of(true, false, false, false));

    public TwoLaneCrossing(){
        for(int i = 0; i < 4; i++){
            lanes.add(makeOneDirectionLanes(MapDirection.intToDirection(i)));
        }
    }
    private List<Lane> makeOneDirectionLanes(MapDirection beginning){
        Boolean lightState = lightsCycle.get(MapDirection.parseDirection(beginning));
        Lane leftLane = new Lane(beginning, List.of(beginning.spinClockwise(1)), lightState);
        List<MapDirection> destinations = List.of(beginning.spinClockwise(2), beginning.spinClockwise(3));
        Lane rightLane = new Lane(beginning, destinations, lightState);
        return List.of(leftLane, rightLane);
    }
    @Override
    public void addCar(Car car) {
        List<Lane> sameBeginningLaneGroup = lanes.get(MapDirection.parseDirection(car.getStartRoad()));
        for(Lane lane: sameBeginningLaneGroup){
            for(MapDirection destination: lane.getDesitnations()){
                if(destination == car.getEndRoad()){
                    lane.addCar(car);
                    return;
                }
            }
        }
    }

    @Override
    public void calcNewLightStatus() {
        stepsToCyclesEnd--;
        if(stepsToCyclesEnd == 0){
            int i = 0;
            while(!lightsCycle.get(i)) i++;
            lightsCycle.set(i, false);
            lanes.get(i).get(0).changeLightState();
            lanes.get(i).get(1).changeLightState();
            int nextLaneIndex = (i + 1) % lightsCycle.size();
            lightsCycle.set(nextLaneIndex, true);
            lanes.get(nextLaneIndex).get(0).changeLightState();
            lanes.get(nextLaneIndex).get(1).changeLightState();
            stepsToCyclesEnd = defaultCycle;
        }
    }

    @Override
    public void moveFirstCars() {
        List<Lane> lanesToPoll = new ArrayList<>();
        for(List<Lane> laneList: lanes){
            for(Lane lane: laneList){
                if(carCanMove(lane.getFirstCarInLane(), lane)){
                    lanesToPoll.add(lane);
                }
            }
        }
        for(Lane lane: lanesToPoll){
            lane.pollFirstCarInLane().move();
        }
    }

    private boolean carCanMove(Car car, Lane lane) {
        if(car == null) return false;
        //left and forward going can only go when the light is green
        if(lane.getLightState()){
            return true;
        }

        //Can turn right on the red light if the lane on the left either: has red light, first car there is going right or left, there are no cars
        if(car.getDirection() == CarDirection.RIGHT) {
            Lane straightGoingLaneOnLeft = lanes.get(MapDirection.parseDirection(car.getStartRoad().spinClockwise(1))).get(1);
            if (straightGoingLaneOnLeft.getCarCount() == 0) {
                return true;
            }
            Car firstCarInLaneOnLeft = straightGoingLaneOnLeft.getFirstCarInLane();
            return car.getDirection() == CarDirection.RIGHT && (!straightGoingLaneOnLeft.getLightState() || firstCarInLaneOnLeft.getDirection() != CarDirection.FORWARD);
        }
        return false;
    }

    @Override
    public void incrementWaitingTimes() {
        for(List<Lane> laneList: lanes){
            for(Lane lane: laneList){
                lane.incrementCarsWaitingTime();
            }
        }
    }
}
