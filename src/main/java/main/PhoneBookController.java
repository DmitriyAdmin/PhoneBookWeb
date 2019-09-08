package main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import main.model.PhoneBook;
import main.model.PhoneBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PhoneBookController {

  @Autowired
  private PhoneBookRepository phoneBookRepository;


  @GetMapping("/")
  public String index() {
    return "main";
  }

  @GetMapping("/contacts")
  public String contacts(Model model) {
    Iterable<PhoneBook> phoneBookIterable = phoneBookRepository.findAll();
    ArrayList<PhoneBook> phoneBook = new ArrayList<PhoneBook>();
    for (PhoneBook contact : phoneBookIterable) {
      phoneBook.add(contact);
    }
    model.addAttribute("phoneBook", phoneBook);
    model.addAttribute("contactsCount", phoneBook.size());
    return "index";
  }

  @PostMapping("/contacts/add")
  public String addContacts(Model model,
      @RequestParam String firstName,
      @RequestParam String lastName,
      @RequestParam long phoneNumber,
      @RequestParam("file") MultipartFile file,
      @RequestParam String note) {

    PhoneBook phoneBook = new PhoneBook(lastName, firstName, phoneNumber, note);
    phoneBook.setFilename("/img/contact.png");

    if (file != null && !file.getOriginalFilename().isEmpty()) {
      File uploadDir = new File(getAbsolutePath());

      if (!uploadDir.exists()) {
        uploadDir.mkdir();
      }

      String uuidFile = UUID.randomUUID().toString();
      String resultFilename = uuidFile + "." + file.getOriginalFilename();

      try {
        file.transferTo(new File(getAbsolutePath() + resultFilename));
        phoneBook.setFilename("/img/" + resultFilename);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    phoneBookRepository.save(phoneBook);

    model.addAttribute("message", "Контакт успешно добавлен");
    return "add";
  }

  @GetMapping("/contacts/add")
  public String greeting(Model model) {
    return "add";
  }

  @GetMapping("/contacts/{id}")
  public String getContact(Model model, @PathVariable int id) {
    Optional<PhoneBook> optionalPhoneBook = phoneBookRepository.findById(id);
    PhoneBook phoneBook = optionalPhoneBook.isPresent() ? optionalPhoneBook.get() : new PhoneBook();
    model.addAttribute("phoneBook", phoneBook);
    return "contact";
  }

  public String getAbsolutePath() {
    URL res = getClass().getClassLoader().getResource("img/");
    File file = null;
    try {
      file = Paths.get(res.toURI()).toFile();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    String absolutePath = file.getAbsolutePath() + "/";

    return absolutePath;
  }

}
