package com.psikku.backend.dto.monitoring;

public class TestMonitoringDto {

    private int totalParticipant;
    private int participant;
    private double averagePoint;

    public int getTotalParticipant() {
        return totalParticipant;
    }

    public void setTotalParticipant(int totalParticipant) {
        this.totalParticipant = totalParticipant;
    }

    public int getParticipant() {
        return participant;
    }

    public void setParticipant(int participant) {
        this.participant = participant;
    }

    public double getAveragePoint() {
        return averagePoint;
    }

    public void setAveragePoint(double averagePoint) {
        this.averagePoint = averagePoint;
    }
}
