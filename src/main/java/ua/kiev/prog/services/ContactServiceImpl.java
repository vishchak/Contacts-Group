package ua.kiev.prog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kiev.prog.model.Contact;
import ua.kiev.prog.model.Group;
import ua.kiev.prog.dao.ContactDAO;
import ua.kiev.prog.dao.GroupDAO;

import java.util.List;

//component-like annotation
//creates object, puts it in the context
@Service
public class ContactServiceImpl implements ContactService {

    private final ContactDAO contactDAO;
    private final GroupDAO groupDAO;

    public ContactServiceImpl(ContactDAO contactDAO, GroupDAO groupDAO) {
        this.contactDAO = contactDAO;
        this.groupDAO = groupDAO;
    }

    //To use there should be a Bean TRANSACTIONAL MANAGER
    //spring starts transaction -> your code -> if there is no exception commit; else ->rollback
    @Transactional//(rollbackFor = RuntimeException.class)(required type of exception to rollback)
    public void addContact(Contact contact) {
        contactDAO.add(contact);
    }

    @Transactional
    public void addGroup(Group group) {
        groupDAO.add(group);
    }

    @Transactional
    public void deleteContact(long[] ids) {
        contactDAO.delete(ids);
    }

    //NECESSARY
    //put it even if transaction is unnecessary
    //readOnly using to not switch the driver to "autoCommit = true" -> better performance
    @Transactional(readOnly = true)
    public List<Group> listGroups() {
        return groupDAO.list();
    }

    @Transactional(readOnly = true)
    public List<Contact> listContacts(Group group, int start, int count) {
        return contactDAO.list(group, start, count);
    }

    @Transactional(readOnly = true)
    public List<Contact> listContacts(Group group) {
        return contactDAO.list(group, 0, 0);
    }

    @Transactional(readOnly = true)
    public long count() {
        return contactDAO.count();
    }

    @Transactional(readOnly = true)
    public Group findGroup(long id) {
        return groupDAO.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<Contact> searchContacts(String pattern) {
        return contactDAO.list(pattern);
    }

    //added del. method impl
    @Transactional
    public void deleteGroup(Group group) {
        groupDAO.delete(group);
    }
}
