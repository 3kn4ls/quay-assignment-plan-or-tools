package com.port.solver.model;

/**
 * A quay crane with physical coverage range and productivity limits.
 */
public class Crane {
    private String id;
    private String name;
    private CraneType craneType;
    private int berthRangeStart;
    private int berthRangeEnd;
    private int minProductivity;
    private int maxProductivity;

    public Crane() {}

    public Crane(String id, String name, CraneType craneType,
                 int berthRangeStart, int berthRangeEnd,
                 int minProductivity, int maxProductivity) {
        this.id = id;
        this.name = name;
        this.craneType = craneType;
        this.berthRangeStart = berthRangeStart;
        this.berthRangeEnd = berthRangeEnd;
        this.minProductivity = minProductivity;
        this.maxProductivity = maxProductivity;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public CraneType getCraneType() { return craneType; }
    public void setCraneType(CraneType craneType) { this.craneType = craneType; }
    public int getBerthRangeStart() { return berthRangeStart; }
    public void setBerthRangeStart(int berthRangeStart) { this.berthRangeStart = berthRangeStart; }
    public int getBerthRangeEnd() { return berthRangeEnd; }
    public void setBerthRangeEnd(int berthRangeEnd) { this.berthRangeEnd = berthRangeEnd; }
    public int getMinProductivity() { return minProductivity; }
    public void setMinProductivity(int minProductivity) { this.minProductivity = minProductivity; }
    public int getMaxProductivity() { return maxProductivity; }
    public void setMaxProductivity(int maxProductivity) { this.maxProductivity = maxProductivity; }
}
