package com.kimhok.tickets.config;

import com.kimhok.tickets.entity.Role;
import com.kimhok.tickets.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    @Override
    public void run(String... args) throws Exception {
        createRoleIfNotExists("USER");
        createRoleIfNotExists("ORGANIZER");
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("STAFF");
    }
    private void createRoleIfNotExists(String name){
        roleRepository.findRoleByName(name).orElseGet(()->{
            Role role = new Role();
            role.setName(name);
            return roleRepository.save(role);
        });
    }
}
