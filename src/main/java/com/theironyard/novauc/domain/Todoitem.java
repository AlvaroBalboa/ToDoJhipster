package com.theironyard.novauc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Todoitem.
 */
@Entity
@Table(name = "todoitem")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Todoitem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "todo_name")
    private String todoName;

    @NotNull
    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;

    @NotNull
    @Column(name = "is_done", nullable = false)
    private Boolean isDone;

    @NotNull
    @Column(name = "date_due", nullable = false)
    private LocalDate dateDue;

    @ManyToOne
    private Citizen citizen;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTodoName() {
        return todoName;
    }

    public Todoitem todoName(String todoName) {
        this.todoName = todoName;
        return this;
    }

    public void setTodoName(String todoName) {
        this.todoName = todoName;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public Todoitem dateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Boolean isIsDone() {
        return isDone;
    }

    public Todoitem isDone(Boolean isDone) {
        this.isDone = isDone;
        return this;
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }

    public LocalDate getDateDue() {
        return dateDue;
    }

    public Todoitem dateDue(LocalDate dateDue) {
        this.dateDue = dateDue;
        return this;
    }

    public void setDateDue(LocalDate dateDue) {
        this.dateDue = dateDue;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public Todoitem citizen(Citizen citizen) {
        this.citizen = citizen;
        return this;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Todoitem todoitem = (Todoitem) o;
        if (todoitem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, todoitem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Todoitem{" +
            "id=" + id +
            ", todoName='" + todoName + "'" +
            ", dateCreated='" + dateCreated + "'" +
            ", isDone='" + isDone + "'" +
            ", dateDue='" + dateDue + "'" +
            '}';
    }
}
