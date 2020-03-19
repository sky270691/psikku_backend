package com.psikku.backend.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "test")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "test_id")
    private List<Subtest> subtestList;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @JoinTable(name = "package_test",
            joinColumns = @JoinColumn(name = "test_id"),
            inverseJoinColumns = @JoinColumn(name = "package_id"))
    private List<Package> packageList;

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

    public List<Subtest> getSubtestList() {
        return subtestList;
    }

    public void setSubtestList(List<Subtest> subtestList) {
        this.subtestList = subtestList;
    }

    public List<Package> getPackageList() {
        return packageList;
    }

    public void setPackageList(List<Package> packageList) {
        this.packageList = packageList;
    }
}
