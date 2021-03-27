package com.autoclub_156.demo.controller;

import com.autoclub_156.demo.controller.requests.AddRecordRequest;
import com.autoclub_156.demo.model.Car;
import com.autoclub_156.demo.model.ServiceEntry;
import com.autoclub_156.demo.security.CustomUserDetails;
import com.autoclub_156.demo.services.ServiceEntryService;
import com.autoclub_156.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class ServiceEntryController {

    @Autowired
    ServiceEntryService serviceEntryService;

    @Autowired
    UserService userService;

    @PostMapping("/admin/addRecord")
    private Car addRecord(@RequestBody AddRecordRequest request) {
        String executorName = userService.findByLogin(request.executor).getLogin();
        ServiceEntry serviceEntry = new ServiceEntry(request.date, executorName, request.description);
        ServiceEntry record = serviceEntryService.saveRecord(serviceEntry);
        return serviceEntryService.bindRecordToCar(record, request.vincode);
    }

    @GetMapping("/user/getServiceBook/{vincode}")
    private ArrayList<ServiceEntry> getServiceBook(HttpServletRequest request, @PathVariable String vincode) {
        Authentication auth = (Authentication) request.getUserPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
        String login = customUserDetails.getUsername();

        if (userService.findByLogin(login).getRole().getName().equals("ROLE_USER") && userService.isAccess(login, vincode)) {
            return serviceEntryService.getServiceBookByVincode(vincode);
        } else if (userService.findByLogin(login).getRole().getName().equals("ROLE_ADMIN")) {
            return serviceEntryService.getServiceBookByVincode(vincode);
        }

        return null;
    }

    @PutMapping("/admin/addPhotoToRecord/{recordId}")
    private String updateRecord(@PathVariable String recordId) {
        return "Not implemented";
    }

    @PostMapping("/admin/updateRecord")
    private String updateRecord(@RequestBody AddRecordRequest request) {
        return "Not implemented";
    }

    @DeleteMapping("/admin/deleteRecord/{recordId}")
    private void deleteRecord(@PathVariable String recordId) {
        serviceEntryService.deleteRecord(recordId);
    }
}
