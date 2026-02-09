package com.port.solver.model;

/**
 * Preference for a vessel to load/unload near a specific yard zone.
 */
public class YardQuayZonePreference {
    private int yardQuayZoneId;
    private double volume;

    public YardQuayZonePreference() {}

    public YardQuayZonePreference(int yardQuayZoneId, double volume) {
        this.yardQuayZoneId = yardQuayZoneId;
        this.volume = volume;
    }

    public int getYardQuayZoneId() { return yardQuayZoneId; }
    public void setYardQuayZoneId(int yardQuayZoneId) { this.yardQuayZoneId = yardQuayZoneId; }
    public double getVolume() { return volume; }
    public void setVolume(double volume) { this.volume = volume; }
}
