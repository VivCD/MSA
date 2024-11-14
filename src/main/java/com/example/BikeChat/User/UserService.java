package com.example.BikeChat.User;

import com.example.BikeChat.Firebase.FirebaseService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    private final FirebaseService firebaseService;

    public UserService(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    public void saveUser(User user) throws Exception {
        firebaseService.saveUser(user);
    }

    public User getUser(String id) {
        return firebaseService.getUser(id);
    }

    public List<User> getAllUsers(){
        return firebaseService.getAllUsers();
    }

    public String encryptUserPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        System.out.println("password hash:" + hashedPassword );

        return hashedPassword;

    }

}
