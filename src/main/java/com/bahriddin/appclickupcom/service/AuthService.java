package com.bahriddin.appclickupcom.service;

import com.bahriddin.appclickupcom.entity.User;
import com.bahriddin.appclickupcom.entity.enums.SystemRoleName;
import com.bahriddin.appclickupcom.payload.ApiResponse;
import com.bahriddin.appclickupcom.payload.RegisterDTO;
import com.bahriddin.appclickupcom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JavaMailSender javaMailSender;


    //TODO KOD YOZAMIZ
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public ApiResponse registerUser(RegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getEmail()))
            return new ApiResponse("User already exist", false);
        User user = new User(
                registerDTO.getFullName(),
                registerDTO.getEmail(),
                passwordEncoder.encode(registerDTO.getPassword()),
                SystemRoleName.SYSTEM_USER
        );
        int code = new Random().nextInt(999999);
        user.setEmailCode(String.valueOf(code).substring(0, 4));
        userRepository.save(user);
        sendEmail(user.getEmail(), user.getEmailCode());
        return new ApiResponse("User saved", true);
    }

    public Boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Test@pdp.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Akkountni tasdiqlash");
            mailMessage.setText(emailCode);
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ApiResponse verifyEmail(String email, String emailCode) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (emailCode.equals(user.getEmailCode())) {
                user.setEnabled(true);
                userRepository.save(user);
                return new ApiResponse("Account Active", true);
            }
            return new ApiResponse("Code incorrect", false);
        }
        return new ApiResponse("User not exist", false);
    }
}
