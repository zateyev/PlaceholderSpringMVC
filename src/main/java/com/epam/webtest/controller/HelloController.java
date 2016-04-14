package com.epam.webtest.controller;

import com.epam.webtest.dao.DocumentDao;
import com.epam.webtest.dao.PackDao;
import com.epam.webtest.domain.Document;
import com.epam.webtest.domain.Marker;
import com.epam.webtest.domain.Pack;
import com.epam.webtest.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HelloController {

    @Autowired
    private DocumentDao documentDao;

    @Autowired
    private PackDao packDao;

    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public ModelAndView defaultPage() {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/admin**", method = RequestMethod.GET)
    public ModelAndView adminPage() {

        ModelAndView model = new ModelAndView();
        model.addObject("title", "Spring Security Login Form - Database Authentication");
        model.addObject("message", "This page is for ROLE_ADMIN only!");
        model.setViewName("admin");
        return model;

    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid username and password!");
        }

        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        model.setViewName("login");

        return model;

    }

    //for 403 access denied page
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accesssDenied() {

        ModelAndView model = new ModelAndView();

        //check if user is login
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            model.addObject("username", userDetail.getUsername());
        }

        model.setViewName("403");
        return model;

    }

    @RequestMapping(value = "/my-packs", method = RequestMethod.GET)
    public ModelAndView packList() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        List<Pack> packList = packDao.findByUsername(username);
        ModelAndView model = new ModelAndView("my-packs");
        model.addObject("packList", packList);
        return model;
    }

    /*@RequestMapping(value = "/form", method = RequestMethod.GET)
    public ModelAndView generateForm(HttpServletRequest request) {
        Action action = Actionfactory.getAction(request);
        return action.execute();
    }*/

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public ModelAndView generateForm(@RequestParam(value = "packId") Long packId) {
        Pack pack = packDao.findById(packId);
        List<Document> documents = documentDao.findByPack(pack);
        pack.setDocuments(documents);
        Marker marker = new Marker();
        Set<Tag> tags = marker.getTags(pack);
        ModelAndView model = new ModelAndView("generated-form");
        model.addObject("tags", tags);
        return model;
    }

    private final String FILEPATH = "D:\\tmp2\\upload\\";

    @RequestMapping(method = RequestMethod.GET, value = "/create-pack")
    public String provideUploadInfo(Model model) {
        File rootFolder = new File(FILEPATH);
        List<String> fileNames = Arrays.stream(rootFolder.listFiles())
                .map(f -> f.getName())
                .collect(Collectors.toList());

        model.addAttribute("files",
                Arrays.stream(rootFolder.listFiles())
                        .sorted(Comparator.comparingLong(f -> -1 * f.lastModified()))
                        .map(f -> f.getName())
                        .collect(Collectors.toList())
        );

        return "create-pack";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create-pack")
    public String handleFileUpload(@RequestParam(value = "name") String name,
                                   @RequestParam("file") List<MultipartFile> file,
                                   RedirectAttributes redirectAttributes) {
        if (name.contains("/")) {
            redirectAttributes.addFlashAttribute("message", "Folder separators not allowed");
            return "redirect:create-pack";
        }
        if (name.contains("/")) {
            redirectAttributes.addFlashAttribute("message", "Relative pathnames not allowed");
            return "redirect:create-pack";
        }

        if (!file.isEmpty()) {
            try {
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String username = userDetails.getUsername();
                Pack pack = packDao.insert(new Pack(name, FILEPATH, username));
                for (MultipartFile multipartFile : file) {
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(new File(FILEPATH + multipartFile.getOriginalFilename())));
                    FileCopyUtils.copy(multipartFile.getInputStream(), stream);
                    stream.close();
                    Document document = new Document(multipartFile.getOriginalFilename(), pack);
                    documentDao.insert(document);
                    redirectAttributes.addFlashAttribute("message",
                            "You successfully uploaded " + name + "!");
                }
                return "my-packs";
            }
            catch (Exception e) {
                redirectAttributes.addFlashAttribute("message",
                        "You failed to upload " + name + " => " + e.getMessage());
            }
        }
        else {
            redirectAttributes.addFlashAttribute("message",
                    "You failed to upload " + name + " because the file was empty");
        }

        return "redirect:create-pack";
    }
}