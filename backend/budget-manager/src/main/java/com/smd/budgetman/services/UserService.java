package com.smd.budgetman.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.smd.budgetman.model.User;
import com.smd.budgetman.model.UserLogin;
import com.smd.budgetman.repository.UserLoginRepository;
import com.smd.budgetman.repository.UserRepository;
import com.smd.budgetman.security.SecurityConfig;
import com.smd.budgetman.vo.UserVos.UserAuthorizeRequestVo;
import com.smd.budgetman.vo.UserVos.UserAuthorizeResponseVo;
import com.smd.budgetman.vo.UserVos.UserLoginRequestVo;
import com.smd.budgetman.vo.UserVos.UserRequestVo;
import com.smd.budgetman.vo.UserVos.UserTokenResponseVo;
import com.smd.budgetman.vo.UserVos.UserUpdateVo;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityConfig securityConfig;

    // TODO
    // @Autowired
    // private BudgetService budgetService;

    // TODO
    // @Autowired
    // private ExpendsService expendsService;

    public UserRequestVo findByUserId(Long userId) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId));

        return UserRequestVo.builder()
                .username(user.getUserName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public UserTokenResponseVo registerNewUser(UserRequestVo userRequestVo) {

        if (userRequestVo.getPassword() == null || userRequestVo.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        if (userRequestVo.getUsername() == null || userRequestVo.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (userRequestVo.getPhoneNumber() == null
                || userRequestVo.getPhoneNumber().isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }

        User user = User.builder()
                .userName(userRequestVo.getUsername())
                .password(securityConfig.passwordEncoder().encode(userRequestVo.getPassword()))
                .phoneNumber(userRequestVo.getPhoneNumber())
                .email(userRequestVo.getEmail())
                .build();

        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Cannot create this user; it already exists!");
        }

        String token = createJsonWebToken(savedUser.getUserId());

        UserLogin userLogin = UserLogin.builder()
                .user(savedUser)
                .token(token)
                .tokenExpireTime(getCurrentTimeStamp())
                .build();

        userLoginRepository.save(userLogin);

        return UserTokenResponseVo.builder()
                .userId(savedUser.getUserId())
                .token(token)
                .build();
    }

    public UserTokenResponseVo validateUserCredentialsAndGenerateToken(UserLoginRequestVo userRequestVo) {

        if (userRequestVo.getUsername() == null
                || userRequestVo.getUsername().isBlank()
                || userRequestVo.getPassword() == null
                || userRequestVo.getPassword().isBlank()) {
            throw new IllegalArgumentException("Username and password are required");
        }

        User user = userRepository.findByUserName(userRequestVo.getUsername());

        if (user == null) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        if (!passwordEncoder.matches(userRequestVo.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = createJsonWebToken(user.getUserId());

        UserLogin lastLogin = userLoginRepository.findByUser_UserId(user.getUserId());

        if (lastLogin != null) {
            userLoginRepository.deleteById(lastLogin.getUserLoginId());
        }

        UserLogin userLogin = UserLogin.builder()
                .user(user)
                .token(token)
                .tokenExpireTime(getCurrentTimeStamp())
                .build();

        userLoginRepository.save(userLogin);

        return UserTokenResponseVo.builder()
                .userId(user.getUserId())
                .token(token)
                .build();
    }

    public UserAuthorizeResponseVo authorizeV2(UserAuthorizeRequestVo userRequestVo) {
        try {
            Long userId = extractUserIdFromToken(userRequestVo.getToken());

            UserLogin userLogin = userLoginRepository.findByUser_UserIdAndToken(userId, userRequestVo.getToken());

            if (userLogin != null) {
                return new UserAuthorizeResponseVo(userId, verifyToken(userRequestVo.getToken()));
            }

            return new UserAuthorizeResponseVo(userId, false);
        } catch (JWTVerificationException e) {
            return new UserAuthorizeResponseVo(null, false);
        }
    }

    public void updateUser(String token, UserUpdateVo vo) {

        Long userId;
        try {
            userId = extractUserIdFromToken(token);
        } catch (JWTVerificationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (vo.getUsername() != null && !vo.getUsername().isBlank()) {
            user.setUserName(vo.getUsername());
        }

        if (vo.getPhoneNumber() != null && !vo.getPhoneNumber().isBlank()) {
            user.setPhoneNumber(vo.getPhoneNumber());
        }

        if (vo.getNewPassword() != null && !vo.getNewPassword().isBlank()) {

            if (vo.getOldPassword() == null || vo.getOldPassword().isBlank()) {
                throw new IllegalArgumentException("Old password is required");
            }

            if (!passwordEncoder.matches(vo.getOldPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Old password is incorrect");
            }

            user.setPassword(passwordEncoder.encode(vo.getNewPassword()));
        }

        userRepository.save(user);
    }

    public void deleteUser(Long userId) {

        // TODO : delete expends belonging to this user
        // TODO : delete budgets belonging to this user

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId));

        UserLogin logins = userLoginRepository.findByUser_UserId(userId);
        if (logins != null) {
            userLoginRepository.delete(logins);
        }

        userRepository.delete(user);
    }

    private String getCurrentTimeStamp() {
        Date newDate = DateUtils.addHours(new Date(), 3);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newDate);
    }

    private static String createJsonWebToken(Long userId) {
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withIssuer("auth0")
                .withExpiresAt(DateUtils.addHours(new Date(), 3))
                .sign(Algorithm.HMAC256("secret"));
    }

    private static Long extractUserIdFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build();

        DecodedJWT jwt = verifier.verify(token);
        return Long.valueOf(jwt.getSubject());
    }

    private static boolean verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            Date dateTheTokenWillExpire = jwt.getExpiresAt();
            return (new Date().compareTo(dateTheTokenWillExpire) < 1);
        } catch (JWTVerificationException exception) {
            return false;
        }
    }
}
