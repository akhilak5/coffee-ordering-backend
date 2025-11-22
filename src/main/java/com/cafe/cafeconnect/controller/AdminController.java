package com.cafe.cafeconnect.controller;

import com.cafe.cafeconnect.model.Invite;
import com.cafe.cafeconnect.repository.InviteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin")          // admin endpoints grouped under /admin
public class AdminController {

    @Autowired
    private InviteRepository inviteRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // POST /admin/generate-invite
    @PostMapping("/generate-invite")
    public Object generateInvite(@RequestBody Map<String, Object> body) {

        String role = (String) body.get("role");

        if (role == null) return Map.of("error", "missing role");

        // generate 8-char plain code and save only hash
        String plainCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String hash = passwordEncoder.encode(plainCode);

        Invite invite = new Invite(hash, role);
        inviteRepository.save(invite);

        return Map.of("inviteCode", plainCode, "role", role);
    }
}






