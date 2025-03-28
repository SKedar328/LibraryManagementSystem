package com.example.LibraryManagementSystem.Services;

import com.example.LibraryManagementSystem.Entity.Admin;
import com.example.LibraryManagementSystem.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
   @Autowired
    private AdminRepository adminRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public ResponseEntity<String> adminLogin(String adminName, String adminPass){
       Optional<Admin> adminExist= Optional.ofNullable
               (adminRepository.findByAdminName(adminName));
       if(adminExist.isPresent()){
           Admin admin=adminExist.get();
           if(passwordEncoder.matches(adminPass, admin.getAdminPass()))
           return ResponseEntity.ok("Login Successfull");
           else{
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password Mismatch");
           }
       }
       else{
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username or Password Incorrect");
       }
   }
    public Admin adminSignup(String adminName, String adminPass) {
        if (adminPass == null || adminPass.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin password cannot be null or empty!"); // ✅ Returns 400 Bad Request
        }
        if(adminRepository.findByAdminName(adminName)!=null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin with this name already exists!"); // ✅ Returns 409 Conflict
        }
        Admin admin = Admin.builder()
                .adminName(adminName)
                .adminPass(passwordEncoder.encode(adminPass)) // ✅ Encrypt password before saving
                .build();

        return adminRepository.save(admin);
    }

//    public List<Admin> adminSignups(List<Admin> admins) {
//        List<Admin> encryptedAdmins = new ArrayList<>(); // ✅ New list to avoid modifying input directly
//
//        for (Admin admin : admins) {
//            if (admin.getAdminPass() == null || admin.getAdminPass().isEmpty()) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin password cannot be null or empty!"); // ✅ Returns 400 Bad Request
//            }
//
//            Admin encryptedAdmin = Admin.builder()
//                    .adminName(admin.getAdminName())
//                    .adminPass(passwordEncoder.encode(admin.getAdminPass())) // ✅ Encrypt password
//                    .build();
//
//            encryptedAdmins.add(encryptedAdmin);
//        }
//
//        return adminRepository.saveAll(encryptedAdmins);
//    }


   public List<Admin> getAdmins(){
       return adminRepository.findAll();
   }
    public void deleteAdmin(Long id){
       adminRepository.deleteById(id);
    }


}
