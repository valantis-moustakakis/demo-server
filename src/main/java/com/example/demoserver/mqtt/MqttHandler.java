package com.example.demoserver.mqtt;

import com.example.demoserver.dtos.StreetMeasurementDTO;
import com.example.demoserver.mappers.StreetMeasurementMapper;
import com.example.demoserver.repositories.StreetMeasurementRepository;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class MqttHandler {

    StreetMeasurementRepository streetMeasurementRepository;

    public void handleMqttMessage(String message) {
        List<StreetMeasurementDTO> streetMeasurements = parseMeasurements(message);
        for (StreetMeasurementDTO streetMeasurement : streetMeasurements) {
            streetMeasurementRepository.save(StreetMeasurementMapper.toStreetMeasurementEntity(streetMeasurement));
        }
    }

    private List<StreetMeasurementDTO> parseMeasurements(String streetMeasurementsString) {

        List<StreetMeasurementDTO> streetMeasurements = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(streetMeasurementsString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonIncident = jsonArray.getJSONObject(i);

                Long timestamp = jsonIncident.getLong("ts");
                float longitude = jsonIncident.getFloat("lon");
                float latitude = jsonIncident.getFloat("lat");
                float accelerometerX = jsonIncident.getFloat("x");
                float accelerometerY = jsonIncident.getFloat("y");
                float accelerometerZ = jsonIncident.getFloat("z");

                StreetMeasurementDTO streetMeasurement = new StreetMeasurementDTO(timestamp, longitude, latitude, accelerometerX, accelerometerY, accelerometerZ);
                streetMeasurements.add(streetMeasurement);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return streetMeasurements;
    }

}
