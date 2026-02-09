package com.port.solver.model;

/**
 * A zone on the quay associated with yard storage.
 */
public class YardQuayZone {
    private int id;
    private String name;
    private int startDist;
    private int endDist;

    public YardQuayZone() {}

    public YardQuayZone(int id, String name, int startDist, int endDist) {
        this.id = id;
        this.name = name;
        this.startDist = startDist;
        this.endDist = endDist;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getStartDist() { return startDist; }
    public void setStartDist(int startDist) { this.startDist = startDist; }
    public int getEndDist() { return endDist; }
    public void setEndDist(int endDist) { this.endDist = endDist; }
}
