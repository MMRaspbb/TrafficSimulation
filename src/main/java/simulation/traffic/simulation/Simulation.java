package simulation.traffic.simulation;

import simulation.traffic.elements.cars.Car;
import simulation.traffic.elements.cars.LeftTurningCar;
import simulation.traffic.elements.cars.RightTurningCar;
import simulation.traffic.elements.cars.StraightGoingCar;
import simulation.traffic.elements.crossings.Crossing;
import simulation.traffic.elements.crossings.OneLaneCrossing;
import simulation.traffic.elements.crossings.TwoLaneCrossing;
import simulation.traffic.model.utils.Command;
import simulation.traffic.model.utils.MapDirection;
import simulation.traffic.model.utils.OutputWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Simulation {
    public static void run(Command[] commands, int crossingType, String outputPath) throws IOException {
        Crossing crossing;
        OutputWriter outputWriter = new OutputWriter();
        if(crossingType == 1){
            crossing = new OneLaneCrossing();
        }
        else{
            crossing = new TwoLaneCrossing();
        }
        for(Command command: commands){
            switch (command.type()){
                case STEP -> {
                    crossing.moveFirstCars();
                    crossing.incrementWaitingTimes();
                    outputWriter.update();
                    crossing.calcNewLightStatus();
                }
                case ADDVEHICLE ->{
                    Car car = new Car(command.vehicleId(), command.startRoad(), command.endRoad(), outputWriter);
                    crossing.addCar(car);
                }
            }
        }
        outputWriter.saveJson(outputPath);
    }
    private static Car determineCarType(Command command, OutputWriter outputWriter){
        MapDirection startRoad = command.startRoad();
        MapDirection endRoad = command.endRoad();
        if(startRoad.spinClockwise(1) == endRoad){
            return new LeftTurningCar(command.vehicleId(), startRoad, endRoad, outputWriter);
        }
        if(command.startRoad().spinClockwise(2) == command.endRoad()){
            return new StraightGoingCar(command.vehicleId(), startRoad, endRoad, outputWriter);
        }
        return new RightTurningCar(command.vehicleId(), startRoad, endRoad, outputWriter);
    }
}
