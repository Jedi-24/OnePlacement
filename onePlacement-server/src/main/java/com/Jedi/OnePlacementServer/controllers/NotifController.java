package com.Jedi.OnePlacementServer.controllers;

import com.Jedi.OnePlacementServer.payloads.ApiResponse;
import com.Jedi.OnePlacementServer.payloads.NotifMessage;
import com.Jedi.OnePlacementServer.services.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotifController {

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/{Role}")
    public ResponseEntity<ApiResponse> sendNotification(@RequestBody NotifMessage msg, @PathVariable("Role") String role) throws FirebaseMessagingException{
//        tokens.add("dADNiGciQyaL6HlTm9ERaY:APA91bE94cmnJU2ZhvENWIYJdDKfsirTU-plwMlFrExJu8xx3StKXdKBnf4P6bzObrZzE2k1MdRRwy20Iv2uS_mtJ4KDUO-DCxbzlMjSwQ8_Ai63AxFF8hz83M8uwnCTQP3Ce90-ROdy");
        firebaseMessagingService.sendNotification(role, msg);

        return new ResponseEntity<ApiResponse>(new ApiResponse("Success", true), HttpStatus.OK);
    }


}
