package ua.kiev.prog.model;

import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//1 solves names conflict..?
//for older versions
@Table(name = "Groups1")
public class Group {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    //        @OneToMany(mappedBy="group", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Contact> contacts = new ArrayList<>();

    public Group() {
    }

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                '}';
    }
}
