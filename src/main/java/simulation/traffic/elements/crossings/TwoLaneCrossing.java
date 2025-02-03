package simulation.traffic.elements.crossings;

import simulation.traffic.elements.Lane;
import simulation.traffic.elements.Car;
import simulation.traffic.utils.CarDirection;
import simulation.traffic.utils.MapDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TwoLaneCrossing implements Crossing {
    private static final int defaultCycle = 5;
    private int stepsToCyclesEnd = defaultCycle;
    private final List<List<Lane>> lanes = new ArrayList<>();
    //in inner List the first one is left turning lane, the second one is straight and right going
    private static final List<Boolean> initialLightsState = new ArrayList<>(List.of(true, false, false, false));
    private final List<Boolean> lightsInCycleDone = new ArrayList<>(List.of(false, false, false, false));
    private MapDirection currentDirectionLightsActivated = MapDirection.N;

    public TwoLaneCrossing() {
        for (int i = 0; i < 4; i++) {
            lanes.add(makeOneDirectionLanes(MapDirection.intToDirection(i)));
        }
    }

    private List<Lane> makeOneDirectionLanes(MapDirection beginning) {
        Boolean lightState = initialLightsState.get(MapDirection.parseDirection(beginning));
        Lane leftLane = new Lane(beginning, List.of(beginning.spinClockwise(1)), lightState);
        List<MapDirection> destinations = List.of(beginning.spinClockwise(2), beginning.spinClockwise(3));
        Lane rightLane = new Lane(beginning, destinations, lightState);
        return List.of(leftLane, rightLane);
    }

    @Override
    public void addCar(Car car) {
        List<Lane> sameBeginningLaneGroup = lanes.get(MapDirection.parseDirection(car.getStartRoad()));
        for (Lane lane : sameBeginningLaneGroup) {
            for (MapDirection destination : lane.getDesitnations()) {
                if (destination == car.getEndRoad()) {
                    lane.addCar(car);
                    return;
                }
            }
        }
    }

    @Override
    public void calcNewLightStatus() {
        List<Lane> activeLaneList = lanes.get(MapDirection.parseDirection(currentDirectionLightsActivated));
        int carCount = activeLaneList.get(0).getCarCount() + activeLaneList.get(1).getCarCount();
        if(carCount == 0) stepsToCyclesEnd = 1;
        stepsToCyclesEnd--;
        switchLightsIfNecessary();
    }

    private void switchLightsIfNecessary() {
        if (stepsToCyclesEnd == 0) {
            //If all of the lights were activated already then begin the cycle again
            lightsInCycleDone.set(MapDirection.parseDirection(currentDirectionLightsActivated), true);
            List<Lane> lanesToDisactivateLights = lanes.get(MapDirection.parseDirection(currentDirectionLightsActivated));
            lanesToDisactivateLights.get(0).changeLightState();
            lanesToDisactivateLights.get(1).changeLightState();
            if (!lightsInCycleDone.contains(false)) {
                Collections.fill(lightsInCycleDone, false);
            }
            MapDirection lanesDirectionToActivate = null;
            int newStepToCyclesEnd = 0;
            int longestAwaitingCarInLane = -1;
            for (int i = 0; i < lanes.size(); i++) {
                if (lightsInCycleDone.get(i)) continue;
                for (Lane lane : lanes.get(i)) {
                    int[] lanesLongestAwaitingTimeAndStepsToPush = lane.getLongestAwaitingTimeAmountToPush(defaultCycle);
                    //prioritizing lanes based on the first car await time
                    if (longestAwaitingCarInLane < lanesLongestAwaitingTimeAndStepsToPush[0]
                            || (longestAwaitingCarInLane == lanesLongestAwaitingTimeAndStepsToPush[0]
                            && newStepToCyclesEnd < lanesLongestAwaitingTimeAndStepsToPush[1])) {
                        lanesDirectionToActivate = lane.getBeginning();
                        longestAwaitingCarInLane = lanesLongestAwaitingTimeAndStepsToPush[0];
                        newStepToCyclesEnd = lanesLongestAwaitingTimeAndStepsToPush[1];
                    }
                }
            }
            currentDirectionLightsActivated = lanesDirectionToActivate;
            stepsToCyclesEnd = newStepToCyclesEnd;
            List<Lane> lanesToActivate = lanes.get(MapDirection.parseDirection(currentDirectionLightsActivated));
            lanesToActivate.get(0).changeLightState();
            lanesToActivate.get(1).changeLightState();
        }
    }

    @Override
    public void moveFirstCars() {
        List<Lane> lanesToPoll = new ArrayList<>();
        for (List<Lane> laneList : lanes) {
            for (Lane lane : laneList) {
                if (carCanMove(lane.getFirstCarInLane(), lane)) {
                    lanesToPoll.add(lane);
                }
            }
        }
        for (Lane lane : lanesToPoll) {
            lane.pollFirstCarInLane().move();
        }
    }

    private boolean carCanMove(Car car, Lane lane) {
        if (car == null) return false;
        //left and forward going can only go when the light is green
        if (lane.getLightState()) {
            return true;
        }

        //Can turn right on the red light if the lane on the left either: has red light, first car there is going right or left, there are no cars
        if (car.getDirection() == CarDirection.RIGHT) {
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
        for (List<Lane> laneList : lanes) {
            for (Lane lane : laneList) {
                lane.incrementCarsWaitingTime();
            }
        }
    }
}
