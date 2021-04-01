package com.psikku.backend.dto.test;

import com.fasterxml.jackson.annotation.*;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.hibernate.annotations.SortComparator;
import org.springframework.core.annotation.Order;

import javax.persistence.OrderBy;

public class MinimalTestDto{

    private int id;
    @JsonProperty("internal_name")
    private String internalName;
    private String name;
    private String description;
    private int duration;
    //Todo disable this later
    private boolean view;
    private boolean finish;
    private int skippable;
    private int priority;
    private boolean takePict;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public int getSkippable() {
        return skippable;
    }

    public void setSkippable(int skippable) {
        this.skippable = skippable;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isTakePict() {
        return takePict;
    }

    public void setTakePict(boolean takePict) {
        this.takePict = takePict;
    }
}
