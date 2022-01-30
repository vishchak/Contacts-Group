package ua.kiev.prog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.kiev.prog.model.Contact;
import ua.kiev.prog.model.Group;
import ua.kiev.prog.services.ContactService;
import ua.kiev.prog.services.ContactServiceImpl;

//NECESSARY
// class with annotation @Controller handles a query -> returns page
@Controller
public class GroupController {
    static final int DEFAULT_GROUP_ID = -1;
    //inject service
    private final ContactService contactService;

    public GroupController(ContactService contactService) {
        this.contactService = contactService;
    }

    //example: request to this endpoint -> show this page
    @RequestMapping("/group_add_page")
    public String groupAddPage() {
        return "group_add_page";
    }

    //catch post result
    @RequestMapping(value = "/group/add", method = RequestMethod.POST)
    //using req param getting name (in this case)
    public String groupAdd(@RequestParam String name) {
        //using service adding group, that's been created, to the DB
        contactService.addGroup(new Group(name));
        //redirects user to the root of app
        return "redirect:/";
    }

    // smth in {} is variable
    @RequestMapping("/group/{id}")
    //"cuts off" var -> converts to long
    public String listGroup(@PathVariable(value = "id") long groupId,
                            Model model) {
        Group group = (groupId != DEFAULT_GROUP_ID) ?
                //find group by the var (above)
                contactService.findGroup(groupId) : null;
//adding not only group but also contact
        model.addAttribute("groups", contactService.listGroups());
        model.addAttribute("contacts", contactService.listContacts(group));
//if group exists ->return index
        return "index";
    }

    // delete group impl
    @RequestMapping(value = "/delete_group")
    public String deleteGroup(@RequestParam(value = "groupId") long groupId) {
        Group group = (groupId != DEFAULT_GROUP_ID) ?
                contactService.findGroup(groupId) : null;
        contactService.deleteGroup(group);
        return "redirect:/";
    }
}
