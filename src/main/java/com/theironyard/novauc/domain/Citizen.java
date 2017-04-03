package com.theironyard.novauc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Citizen.
 */
@Entity
@Table(name = "citizen")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Citizen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "citizen")
    private String citizen;

    @NotNull
    @Min(value = 10)
    @Max(value = 69)
    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "job")
    private String job;

    @ManyToOne
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCitizen() {
        return citizen;
    }

    public Citizen citizen(String citizen) {
        this.citizen = citizen;
        return this;
    }

    public void setCitizen(String citizen) {
        this.citizen = citizen;
    }

    public Integer getAge() {
        return age;
    }

    public Citizen age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getJob() {
        return job;
    }

    public Citizen job(String job) {
        this.job = job;
        return this;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Company getCompany() {
        return company;
    }

    public Citizen company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Citizen citizen = (Citizen) o;
        if (citizen.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, citizen.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Citizen{" +
            "id=" + id +
            ", citizen='" + citizen + "'" +
            ", age='" + age + "'" +
            ", job='" + job + "'" +
            '}';
    }
}
