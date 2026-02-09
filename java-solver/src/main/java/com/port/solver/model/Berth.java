package com.port.solver.model;

import java.util.Map;
import java.util.TreeMap;

/**
 * The quay/berth where vessels are moored.
 */
public class Berth {
    private int length;
    private Double depth;                   // Uniform depth (if constant)
    private TreeMap<Integer, Double> depthMap; // Position -> depth (variable)

    public Berth() {}

    public Berth(int length, Double depth, Map<Integer, Double> depthMap) {
        this.length = length;
        this.depth = depth;
        this.depthMap = depthMap != null ? new TreeMap<>(depthMap) : null;
    }

    /**
     * Return the depth at a given position along the berth.
     * Matches Python logic: finds the last depth entry at or before this position.
     */
    public double getDepthAt(int position) {
        if (depth != null) {
            return depth;
        }
        if (depthMap != null && !depthMap.isEmpty()) {
            Map.Entry<Integer, Double> entry = depthMap.floorEntry(position);
            return entry != null ? entry.getValue() : 0.0;
        }
        return Double.MAX_VALUE;
    }

    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }
    public Double getDepth() { return depth; }
    public void setDepth(Double depth) { this.depth = depth; }
    public TreeMap<Integer, Double> getDepthMap() { return depthMap; }
    public void setDepthMap(TreeMap<Integer, Double> depthMap) { this.depthMap = depthMap; }
}
