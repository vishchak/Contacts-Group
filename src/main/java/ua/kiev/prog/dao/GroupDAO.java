package ua.kiev.prog.dao;

import ua.kiev.prog.model.Group;

import java.util.List;
//all the needed methods
public interface GroupDAO {
    void add(Group group);
    void delete(Group group);
    Group findOne(long id);
    List<Group> list();
}
