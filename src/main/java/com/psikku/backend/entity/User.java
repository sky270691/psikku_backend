package com.psikku.backend.entity;


import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.Auditable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
public class User {

    @Id
    private long id;

    private String username;

    private String email;

    private String firstname;

    private String lastname;

    private String sex;

    private String address;

    private String city;

    private String province;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "lastmodified_time")
    @UpdateTimestamp
    private LocalDateTime modifiedTime;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH})
    @JoinColumn(name = "company_id")
    private Company company;


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<SubmittedAnswer> submittedAnswerList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    public List<TestResult> testResultList;

    @ManyToMany
    @JoinTable(name = "user_voucher",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "voucher_id"))
    private List<Voucher> voucherList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String fullName) {
        this.firstname = fullName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<SubmittedAnswer> getSubmittedAnswerList() {
        return submittedAnswerList;
    }

    public void setSubmittedAnswerList(List<SubmittedAnswer> submittedAnswerList) {
        this.submittedAnswerList = submittedAnswerList;
    }

    public List<TestResult> getTestResult() {
        return testResultList;
    }

    public void setTestResult(List<TestResult> testResultList) {
        this.testResultList = testResultList;
    }

    @Override
    public String toString() {
        return "Username: " +getUsername()+ "email: " +getEmail()+ "fullname: "+ getFirstname();
    }

    public int getAge(LocalDate dateOftest){
        long age = ChronoUnit.MONTHS.between(this.dateOfBirth,LocalDate.now());
        return (int) age;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<Voucher> getVoucherList() {
        return voucherList;
    }

    public void setVoucherList(List<Voucher> voucherList) {
        this.voucherList = voucherList;
    }
}
