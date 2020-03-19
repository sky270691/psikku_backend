package com.psikku.backend.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "package")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int price;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @JoinTable(name = "package_test",
                joinColumns = @JoinColumn(name = "package_id"),
                inverseJoinColumns = @JoinColumn(name = "test_id"))
    private List<Test> testList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Test> getTestList() {
        return testList;
    }

    public void setTestList(List<Test> testList) {
        this.testList = testList;
    }
}
