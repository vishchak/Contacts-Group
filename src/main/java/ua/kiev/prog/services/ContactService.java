package ua.kiev.prog.services;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kiev.prog.model.Contact;
import ua.kiev.prog.model.Group;

import java.io.File;
import java.util.List;

//services usually extend repository's functions
public interface ContactService {
    void addContact(Contact contact);

    void addGroup(Group group);

    void deleteContact(long[] ids);

    List<Group> listGroups();

    List<Contact> listContacts(Group group, int start, int count);

    List<Contact> listContacts(Group group);

    List<Contact> listContacts();

    long count();

    Group findGroup(long id);

    List<Contact> searchContacts(String pattern);

    //added del. group method
    void deleteGroup(Group group);

    //added save cont. method
    File contactsJson();
}
