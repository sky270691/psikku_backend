package com.psikku.backend.entity;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    long id;

    @Column(name = "username")
    String username;

    @Column(name = "email")
    String email;

    @Column(name = "firstname")
    String firstname;

    @Column(name = "lastname")
    String lastname;

    @Column(name = "sex")
    String sex;

    @Column(name = "create_time")
    LocalDateTime createTime;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    List<SubmittedAnswer> submittedAnswerList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    public List<TestResult> testResultList;

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

}
