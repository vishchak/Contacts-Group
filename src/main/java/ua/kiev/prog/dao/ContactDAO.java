package ua.kiev.prog.dao;

import ua.kiev.prog.model.Contact;
import ua.kiev.prog.model.Group;

import java.util.List;

public interface ContactDAO {
    //all the needed methods
    void add(Contact contact);

    void delete(long[] ids);

    List<Contact> list(Group group, int start,
                       int count);

    List<Contact> list(String pattern);

    //find all contacts
    List<Contact> list();

    long count();
}
