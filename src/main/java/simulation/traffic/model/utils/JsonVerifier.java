package simulation.traffic.model.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import simulation.traffic.exception.JsonVerificationException;

import java.util.List;

public class JsonVerifier {
    public static Command[] verifyInputJson(JSONObject input) throws JsonVerificationException {
        //making local consts as the method will only be used once
        final String COMMANDS = "commands";
        final String TYPE = "type";
        final String STEP = "step";
        final int STEPCOMMANDSIZE = 1;
        final String ADDVEHICLE = "addVehicle";
        final String VEHICLEID = "vehicleId";
        final String STARTROAD = "startRoad";
        final String ENDROAD = "endRoad";
        final int VEHICLECOMMANDSIZE = 4;


        if (!input.has(COMMANDS)) {
            throw new JsonVerificationException("Given json doesn't contain \"" + COMMANDS + "\" array.");
        }
        JSONArray jsonArray = input.getJSONArray(COMMANDS);
        Command[] commands = new Command[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (!jsonObject.has(TYPE)) {
                throw new JsonVerificationException("Given command doesn't have a type.");
            }
            Type commandType;
            try {
                commandType = Type.stringToType(jsonObject.getString(TYPE));
            }catch (IllegalArgumentException e){
                throw new JsonVerificationException("Command has invalid type.");
            }
            if(commandType == Type.STEP){
                if (jsonObject.length() != STEPCOMMANDSIZE) {
                    throw new JsonVerificationException("Command of type " + STEP + " should only contain " + STEPCOMMANDSIZE + " object.");
                }
                commands[i] = new Command(commandType);
                continue;
            }
            if (jsonObject.length() != VEHICLECOMMANDSIZE) {
                throw new JsonVerificationException("Command of type " + ADDVEHICLE + " should only contain " + VEHICLECOMMANDSIZE + " objects.");
            }
            if (!jsonObject.has(VEHICLEID)) {
                throw new JsonVerificationException("Vehicle doesn't have an ID.");
            }
            String vehicleId = jsonObject.getString(VEHICLEID);
            if (!jsonObject.has(STARTROAD)) {
                throw new JsonVerificationException("Vehicle doesn't have startRoad.");
            }
            MapDirection startRoad = MapDirection.stringToMapDirection(jsonObject.getString(STARTROAD));
            if (!jsonObject.has(ENDROAD)) {
                throw new JsonVerificationException("Vehicle doesn't have endRoad.");
            }
            MapDirection endRoad = MapDirection.stringToMapDirection(jsonObject.getString(ENDROAD));
            commands[i] = new Command(commandType, vehicleId, startRoad, endRoad);
        }
        return commands;
    }
}
