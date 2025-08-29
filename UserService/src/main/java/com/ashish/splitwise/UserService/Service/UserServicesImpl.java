package com.ashish.splitwise.UserService.Service;

import com.ashish.splitwise.UserService.DTO.LoginRequest;
import com.ashish.splitwise.UserService.DTO.UserRegistrationRequest;
import com.ashish.splitwise.UserService.DTO.UserRegistrationResponse;
import com.ashish.splitwise.UserService.Dao.UserDao;
import com.ashish.splitwise.UserService.Exception.UserNotFoundException;
import com.ashish.splitwise.UserService.Model.User;
import com.ashish.splitwise.UserService.Security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServicesImpl implements UserService{

    private UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserServicesImpl(UserDao userDao, PasswordEncoder passwordEncoder){
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user){
        return userDao.save(user);
    }

    @Override
    public User updateUser(User user, Long id){
        user = convertToUser(user, id);
        return userDao.save(user);
    }

    @Override
    public Integer getAge(Long id) {
        User user = getUserById(id);
        int year = LocalDate.now().getYear();
        return year-user.getYearOfBirth();
    }

    @Override
    public User getUserById(Long id){
        return userDao.findById(id).orElseThrow(() -> new UserNotFoundException("User with given id: "+id+" not found"));
    }

    @Override
    public User getUserByEmail(String email){
        return userDao.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with given email: "+email+" not found"));
    }

    @Override
    public List<User> findAllUser(){
        return userDao.findAll();
    }

    @Override
    public void deleteUser(long id){
        userDao.deleteById(id);
    }

    @Override
    public UserRegistrationRequest getUserInfo(long id) {
        User user = getUserById(id);
        return new UserRegistrationRequest(user.getName(), user.getEmail(), user.getMobNo(), user.getPassword());
    }

    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        if(userDao.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already present");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setMobNo(request.getMobNo());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User createdUser = createUser(user);
        return new UserRegistrationResponse(createdUser.getId(), createdUser.getName(), createdUser.getEmail());
    }

    @Override
    public String loginUser(LoginRequest request) {
        User user = getUserByEmail(request.getEmail());
        if(!user.getPassword().equals(request.getPassword())){
            throw new RuntimeException("Invalid Credentials");
        }
        return JwtUtil.generateToken(request.getEmail());
    }


    public User convertToUser(User user, Long id){
        User user1 = getUserById(id);
        if(user.getName() != null) {
            user1.setName(user.getName());
        }
        if(user.getEmail() != null && user.getEmail()!=user1.getEmail()) {
            user1.setEmail(user.getEmail());
        }
        if(user.getPassword() != null) {
            user1.setPassword(user.getPassword());
        }
        if(user.getMobNo() != null) {
            user1.setMobNo(user.getMobNo());
        }
        if(user.getGender() != null) {
            user1.setGender(user.getGender());
        }
        if(user.getYearOfBirth() != null) {
            user1.setYearOfBirth(user.getYearOfBirth());
        }
        if(user.getSport() != null) {
            user1.setSport(user.getSport());
        }
        if(user.getWeight() != null) {
            user1.setWeight(user.getWeight());
        }
        if(user.getCity() != null) {
            user1.setCity(user.getCity());
        }
        return user1;
    }
}
