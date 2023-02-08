package com.Jedi.OnePlacementServer;

import com.Jedi.OnePlacementServer.entities.Role;
import com.Jedi.OnePlacementServer.entities.User;
import com.Jedi.OnePlacementServer.payloads.UserDto;
import com.Jedi.OnePlacementServer.repositories.RoleRepo;
import com.Jedi.OnePlacementServer.repositories.UserRepo;
//import com.Jedi.OnePlacementServer.security.CustomAdminDetailsService;
import com.Jedi.OnePlacementServer.utils.AppConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

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


    @Override
    public void run(String... args) throws Exception {
        try {
            Role role = new Role();
            role.setRole_name(AppConstants.I_Role);
            role.setId(AppConstants.Intern_Role_ID);

            Role role1 = new Role();
            role1.setRole_name(AppConstants.P_Role);
            role1.setId(AppConstants.Placement_Role_ID);

            Role role2 = new Role();
            role2.setRole_name(AppConstants.ADMIN);
            role2.setId(AppConstants.ADMIN_Role_ID);
            List<Role> roles = List.of(role, role1, role2);
            this.roleRepo.saveAll(roles);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
