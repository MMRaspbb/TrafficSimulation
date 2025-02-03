package simulation.traffic;

import simulation.traffic.exceptions.JsonVerificationException;
import simulation.traffic.simulation.SimulationInit;

public class Main {
    public static void main(String[] args) {
        try {
            SimulationInit.init(args);
        } catch (JsonVerificationException e) {
            System.out.println(e.getMessage());
        }
    }
}
