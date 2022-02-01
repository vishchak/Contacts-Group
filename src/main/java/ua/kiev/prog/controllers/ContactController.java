package ua.kiev.prog.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.kiev.prog.model.Contact;
import ua.kiev.prog.model.Group;
import ua.kiev.prog.services.ContactService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLConnection;

import static ua.kiev.prog.controllers.GroupController.DEFAULT_GROUP_ID;

@Controller
public class ContactController {
    private static final int ITEMS_PER_PAGE = 6;
    //inject service
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    //for choosing a page (default first page)
    @RequestMapping("/")
    public String index(Model model, @RequestParam(required = false, defaultValue = "0") Integer page) {
        if (page < 0) page = 0;
//limit req
        //total pages and buttons
        long totalCount = contactService.count();
        int start = page * ITEMS_PER_PAGE;
        long pageCount = (totalCount / ITEMS_PER_PAGE) +
                ((totalCount % ITEMS_PER_PAGE > 0) ? 1 : 0);

        model.addAttribute("groups", contactService.listGroups());
        model.addAttribute("contacts",
                contactService.listContacts(null, start, ITEMS_PER_PAGE));
        model.addAttribute("pages", pageCount);

        return "index";
    }

    @RequestMapping("/contact_add_page")
    public String contactAddPage(Model model) {
        //showing all the groups
        model.addAttribute("groups", contactService.listGroups());
        return "contact_add_page";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@RequestParam String pattern, Model model) {
        model.addAttribute("groups", contactService.listGroups());
        model.addAttribute("contacts", contactService.searchContacts(pattern));
        return "index";
    }

    @RequestMapping(value = "/contact/delete", method = RequestMethod.POST)
    public ResponseEntity<Void> delete(
            @RequestParam(value = "toDelete[]", required = false)
                    long[] toDelete) {
        if (toDelete != null && toDelete.length > 0)
            contactService.deleteContact(toDelete);
        //page's already been loaded
//not returning page coz client is JS, not user
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/contact/add", method = RequestMethod.POST)
    //if params aren't equal -> (value ="layoutName") ...newName
    public String contactAdd(@RequestParam(value = "group") long groupId,
                             @RequestParam String name,
                             @RequestParam String surname,
                             @RequestParam String phone,
                             @RequestParam String email) {
        Group group = (groupId != DEFAULT_GROUP_ID) ?
                contactService.findGroup(groupId) : null;
        Contact contact = new Contact(group, name, surname, phone, email);
        contactService.addContact(contact);

        return "redirect:/";
    }

    @GetMapping(value = "/download")
    public String download(HttpServletResponse response) {
        File file = contactService.contactsJson();
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            String mimeType = URLConnection.guessContentTypeFromStream(inputStream);

            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType);
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));

            FileCopyUtils.copy(inputStream, response.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
}
