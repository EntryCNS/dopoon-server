package com.swcns.dopoonserver.domain.auth.presentation.service;

import com.swcns.dopoonserver.domain.auth.presentation.dto.request.SignInRequest;
import com.swcns.dopoonserver.domain.auth.presentation.dto.request.SignupRequest;
import com.swcns.dopoonserver.domain.auth.presentation.dto.response.SignInResponse;
import com.swcns.dopoonserver.domain.user.entity.User;
import com.swcns.dopoonserver.domain.user.exception.UserAlreadyExistsException;
import com.swcns.dopoonserver.domain.user.exception.UserNotFoundException;
import com.swcns.dopoonserver.domain.user.repository.UserRepository;
import com.swcns.dopoonserver.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private LocalDate getDateFromString(String input) {
        return LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
    public void signUp(SignupRequest signupRequest) {
        Optional<User> existUserWithLoginId = userRepository.findByLoginId(signupRequest.getLoginId());
        existUserWithLoginId.ifPresent((user) -> { throw new UserAlreadyExistsException(UserAlreadyExistsException.ExistsType.LOGIN_ID); });

        Optional<User> existUserWithEmail = userRepository.findByLoginId(signupRequest.getLoginId());
        existUserWithEmail.ifPresent((user) -> { throw new UserAlreadyExistsException(UserAlreadyExistsException.ExistsType.EMAIL); });

        User newUser = User.builder()
                .userName(signupRequest.getName())
                .loginId(signupRequest.getLoginId())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .birthday(getDateFromString(signupRequest.getBirthDay()))
                .build();
        userRepository.save(newUser);
    }

    public SignInResponse signIn(SignInRequest signInRequest) {
        User queryUser = userRepository.findByLoginIdOrEmail(signInRequest.getEmailOrId(),
                signInRequest.getEmailOrId())
                .orElseThrow(UserNotFoundException::new);

        String userPassword = queryUser.getPassword();
        String queryPassword = signInRequest.getPassword();

        if(!passwordEncoder.matches(queryPassword, userPassword)) { throw new UserNotFoundException(); }

        String accessToken = jwtTokenProvider.generateAccessToken(queryUser.getLoginId());

        return SignInResponse.builder()
                .accessToken(accessToken)
                .build();
    }

}
