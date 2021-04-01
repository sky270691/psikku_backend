package com.psikku.backend.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "package")
public class TestPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private int price;
    private String category;

    @Column(name = "required_pre_register")
    private boolean requiredPreRegister;

    @Column(name = "view_type")
    private String viewType;

    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDate createDate;

    @OneToMany(mappedBy = "testPackage",cascade = CascadeType.ALL)
    @OrderBy("priority")
    private List<TestPackageTest> testPackageTestList;

    @OneToMany(mappedBy = "testPackage")
    private List<Voucher> voucher;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void setDescription(String description) {
        this.description = description;
    }


    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<TestPackageTest> getTestPackageTestList() {
        return testPackageTestList;
    }

    public void setTestPackageTestList(List<TestPackageTest> testPackageTests) {
        this.testPackageTestList = testPackageTests;
    }

    public List<Voucher> getVoucher() {
        return voucher;
    }

    public void setVoucher(List<Voucher> voucher) {
        this.voucher = voucher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isRequiredPreRegister() {
        return requiredPreRegister;
    }

    public void setRequiredPreRegister(boolean requiredPreRegister) {
        this.requiredPreRegister = requiredPreRegister;
    }
}
