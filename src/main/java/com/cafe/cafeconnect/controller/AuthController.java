package com.cafe.cafeconnect.controller;



import com.cafe.cafeconnect.model.Invite;
import com.cafe.cafeconnect.model.User;
import com.cafe.cafeconnect.repository.InviteRepository;
import com.cafe.cafeconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private InviteRepository inviteRepository;

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    // inside AuthController (add helper)
    private static final Pattern EMAIL_RE = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private boolean isValidEmail(String email) {
        return email != null && EMAIL_RE.matcher(email).matches();
    }


    // CUSTOMER REGISTER (NO INVITE CODE)
    // POST /register
    @PostMapping("/register")
    public Object register(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String email = body.get("email");
        String password = body.get("password");

        if (name == null || name.isBlank() || email == null || email.isBlank() || password == null || password.isBlank())
            return Map.of("error", "missing fields");

        if (!isValidEmail(email))
            return Map.of("error", "invalid email");

        if (password.length() < 6)
            return Map.of("error", "password must be at least 6 characters");

        if (userRepository.findByEmail(email).isPresent())
            return Map.of("error", "email already registered");

        User user = new User(name.trim(), email.trim().toLowerCase(), encoder.encode(password), "USER");
        user.setActivated(true);
        userRepository.save(user);

        return Map.of("success", "registered");
    }

    // CHEF/WAITER ACTIVATION (NEEDS INVITE CODE)
    // POST /activate
    @PostMapping("/activate")
    public Object activate(@RequestBody Map<String, Object> body) {
        String inviteCode = (String) body.get("inviteCode");
        String name = (String) body.get("name");
        String email = (String) body.get("email");
        String password = (String) body.get("password");

        if (inviteCode == null || name == null || email == null || password == null)
            return Map.of("error", "missing fields");

        Optional<Invite> opt = inviteRepository.findAll().stream()
                .filter(i -> !i.isUsed())
                .filter(i -> encoder.matches(inviteCode, i.getCodeHash()))
                .findFirst();

        if (opt.isEmpty())
            return Map.of("error", "invalid invite code");

        Invite invite = opt.get();

        if (userRepository.findByEmail(email).isPresent())
            return Map.of("error", "email already registered");

        User user = new User(name, email, encoder.encode(password), invite.getRole());
        user.setActivated(true);
        user.setInvitationCode(inviteCode);
        userRepository.save(user);

        invite.setUsed(true);
        inviteRepository.save(invite);

        return Map.of("success", "activated");
    }

    // LOGIN (all users)
    // POST /login
    // Replace login method with this
    @PostMapping("/login")
    public Object login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        if (email == null || email.isBlank() || password == null || password.isBlank())
            return Map.of("error", "missing fields");

        if (!isValidEmail(email)) return Map.of("error", "invalid email");

        Optional<User> opt = userRepository.findByEmail(email.trim().toLowerCase());
        if (opt.isEmpty()) return Map.of("error", "invalid credentials");

        User u = opt.get();
        if (!encoder.matches(password, u.getPassword())) return Map.of("error", "invalid credentials");
        if (!u.isActivated()) return Map.of("error", "account not activated");

        return Map.of("id", u.getId(), "name", u.getName(), "email", u.getEmail(), "role", u.getRole());
    }
    @PostMapping("/forgot-password")
    public Object forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) return Map.of("error","missing email");

        // Keep privacy: always return success message so existence is not revealed
        // TODO: generate token + send email in production
        return Map.of("success","If that email exists, a reset link has been sent.");
    }


}


