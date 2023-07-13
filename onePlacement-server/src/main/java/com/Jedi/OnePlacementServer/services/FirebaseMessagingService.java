package com.Jedi.OnePlacementServer.services;

import com.Jedi.OnePlacementServer.entities.Role;
import com.Jedi.OnePlacementServer.entities.User;
import com.Jedi.OnePlacementServer.payloads.NotifMessage;
import com.Jedi.OnePlacementServer.repositories.UserRepo;
import com.Jedi.OnePlacementServer.utils.AppConstants;
import com.google.firebase.messaging.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FirebaseMessagingService {
    @Autowired
    private FirebaseMessaging firebaseMessaging; // made a bean in config file: serverApplication file.
    @Autowired
    private UserRepo userRepo;

    public void sendNotification(String role ,NotifMessage notifMessage) throws FirebaseMessagingException {
        role = "ROLE_" + role;
        System.out.println("kkkk " + role);
        List<String> tokens = new ArrayList<>();
        Set<String> uniqueTokens = new HashSet<>();
        List<User> users = this.userRepo.findAll();

        for(User user: users){
            Set<Role> roles = user.getRoles();
            for(Role r: roles){
                if(r.getName().matches(role)){
                    if(user.getFcmToken()!=null)
                        uniqueTokens.add(user.getFcmToken());
                }
            }
        }
        tokens.addAll(uniqueTokens);
        System.out.println(tokens.size());

        // todo: Revise that Notification object is useless if you have to deal with user logout status; background me it does not call OnMessageReceived, fcm handles the notification itself and sets it to System Tray :/
//        Notification notification = Notification
//                .builder()
//                .setTitle(notifMessage.getTitle())
//                .setBody(notifMessage.getBody())
//                .build();

        Map<String, String> notifPayload = new HashMap<>();
        notifPayload.put(AppConstants.TITLE, notifMessage.getTitle());
        notifPayload.put(AppConstants.BODY, notifMessage.getBody());

        MulticastMessage message = MulticastMessage
                .builder()
                .addAllTokens(tokens) // list-of-tokens HERE;
                .putAllData(notifPayload)
                .build();

//        System.out.println(message.toString());
        firebaseMessaging.sendMulticast(message);
    }
}