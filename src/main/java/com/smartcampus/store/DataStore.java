package com.smartcampus.store;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {

    // Static in-memory storage - exactly like MockDatabase in tutorials
    public static final Map<String, Room> rooms = new HashMap<>();
    public static final Map<String, Sensor> sensors = new HashMap<>();
    public static final Map<String, List<SensorReading>> sensorReadings = new HashMap<>();

    // Room methods
    public static Map<String, Room> getRooms() { return rooms; }

    public static Room getRoom(String id) { return rooms.get(id); }

    public static void addRoom(Room room) { rooms.put(room.getId(), room); }

    public static void deleteRoom(String id) { rooms.remove(id); }

    // Sensor methods
    public static Map<String, Sensor> getSensors() { return sensors; }

    public static Sensor getSensor(String id) { return sensors.get(id); }

    public static void addSensor(Sensor sensor) { sensors.put(sensor.getId(), sensor); }

    public static void deleteSensor(String id) { sensors.remove(id); }

    // SensorReading methods
    public static List<SensorReading> getReadings(String sensorId) {
        return sensorReadings.getOrDefault(sensorId, new ArrayList<>());
    }

    public static void addReading(String sensorId, SensorReading reading) {
        sensorReadings.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);
    }
}