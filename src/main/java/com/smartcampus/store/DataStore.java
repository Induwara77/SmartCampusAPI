package com.smartcampus.store;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {

    // Single shared instance (Singleton pattern)
    private static final DataStore INSTANCE = new DataStore();

    // In-memory storage
    private final Map<String, Room> rooms = new HashMap<>();
    private final Map<String, Sensor> sensors = new HashMap<>();
    private final Map<String, List<SensorReading>> sensorReadings = new HashMap<>();

    // Private constructor - prevents creating multiple instances
    private DataStore() {}

    // Everyone accesses data through this
    public static DataStore getInstance() {
        return INSTANCE;
    }

    // --- Room methods ---
    public Map<String, Room> getRooms() { return rooms; }

    public Room getRoom(String id) { return rooms.get(id); }

    public void addRoom(Room room) { rooms.put(room.getId(), room); }

    public void deleteRoom(String id) { rooms.remove(id); }

    // --- Sensor methods ---
    public Map<String, Sensor> getSensors() { return sensors; }

    public Sensor getSensor(String id) { return sensors.get(id); }

    public void addSensor(Sensor sensor) {
        sensors.put(sensor.getId(), sensor);
    }

    public void deleteSensor(String id) { sensors.remove(id); }

    // --- SensorReading methods ---
    public List<SensorReading> getReadings(String sensorId) {
        return sensorReadings.getOrDefault(sensorId, new ArrayList<>());
    }

    public void addReading(String sensorId, SensorReading reading) {
        sensorReadings
            .computeIfAbsent(sensorId, k -> new ArrayList<>())
            .add(reading);
    }
}