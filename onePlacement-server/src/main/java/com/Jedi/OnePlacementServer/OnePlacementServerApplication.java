package com.Jedi.OnePlacementServer;

import com.Jedi.OnePlacementServer.entities.Role;
import com.Jedi.OnePlacementServer.repositories.RoleRepo;
//import com.Jedi.OnePlacementServer.security.CustomAdminDetailsService;
import com.Jedi.OnePlacementServer.utils.AppConstants;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
// beans can be declared here, as this is already a configuration class or make a custom config. class;
public class OnePlacementServerApplication implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepo roleRepo;

    public static void main(String[] args) {
        SpringApplication.run(OnePlacementServerApplication.class, args);
    }

    @Bean // spring container detects bean,object is ready -> automatically and inject it during auto wiring.
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    @Bean // important : as we need to create bean in IoC-C to make use of firebaseMessaging object in service code.
    FirebaseMessaging firebaseMessaging() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("oneplacement-57c3c-firebase-adminsdk-hzxaz-82092db09a.json").getInputStream());

        FirebaseOptions firebaseOptions = FirebaseOptions.builder().setCredentials(googleCredentials).build();
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions,AppConstants.APP);

        return FirebaseMessaging.getInstance(app);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Role role = new Role();
            role.setName(AppConstants.I_Role);
            role.setId(AppConstants.Intern_Role_ID);

            Role role1 = new Role();
            role1.setName(AppConstants.P_Role);
            role1.setId(AppConstants.Placement_Role_ID);

            Role role2 = new Role();
            role2.setName(AppConstants.ADMIN);
            role2.setId(AppConstants.ADMIN_Role_ID);
            List<Role> roles = List.of(role, role1, role2);
            this.roleRepo.saveAll(roles);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
