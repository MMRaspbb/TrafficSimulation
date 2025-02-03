package simulation.traffic.simulation;

import org.json.JSONObject;
import org.json.JSONTokener;
import simulation.traffic.exception.JsonVerificationException;
import simulation.traffic.model.utils.Command;
import simulation.traffic.model.utils.JsonVerifier;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class SimulationInit {
    public static void init(String[] args) throws JsonVerificationException{
        try(Reader reader = new FileReader(args[0])){
            Command[] commands = JsonVerifier.verifyInputJson(new JSONObject(new JSONTokener(reader)));
            Simulation.run(commands, 2, args[1]);
        } catch (IOException e){
            throw new JsonVerificationException(e.getMessage());
        }
    }
}
