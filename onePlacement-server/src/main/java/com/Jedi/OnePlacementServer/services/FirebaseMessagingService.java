package com.Jedi.OnePlacementServer.services;

import com.Jedi.OnePlacementServer.entities.Role;
import com.Jedi.OnePlacementServer.entities.User;
import com.Jedi.OnePlacementServer.payloads.NotifMessage;
import com.Jedi.OnePlacementServer.repositories.UserRepo;
import com.google.firebase.messaging.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FirebaseMessagingService {
    @Autowired
    private FirebaseMessaging firebaseMessaging;
    @Autowired
    private UserRepo userRepo;

    public void sendNotification(String role ,NotifMessage notifMessage) throws FirebaseMessagingException {
        role = "ROLE_" + role;
        System.out.println("kkkk " + role);
        List<String> tokens = new ArrayList<>();
        List<User> users = this.userRepo.findAll();

        for(User user: users){
            Set<Role> roles = user.getRoles();
            for(Role r: roles){
                if(r.getRole_name().matches(role)){
                    System.out.println("hhhhh " + r.getRole_name());
                    if(user.getFcmToken()!=null)
                        tokens.add(user.getFcmToken());
                }
            }
        }
        System.out.println(tokens.size());

        Notification notification = Notification
                .builder()
                .setTitle(notifMessage.getTitle())
                .setBody(notifMessage.getBody())
                .build();

        MulticastMessage message = MulticastMessage
                .builder()
                .addAllTokens(tokens) // list-of-tokens HERE;
                .setNotification(notification)
                .build();

        firebaseMessaging.sendMulticast(message);
    }
}