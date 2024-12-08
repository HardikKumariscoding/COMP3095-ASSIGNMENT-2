package ca.gbc.userservice.service;

import ca.gbc.userservice.model.User;
import ca.gbc.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Logger LOG =  LoggerFactory.getLogger(UserService.class);

    // Injecting UserRepository through constructor
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Save or update a user
    public User saveUser(User user) {

        LOG.info("Save user");
        return userRepository.save(user);
    }

    // Find a user by ID
    public Optional<User> findUserById(Long id) {
        LOG.info("findbyUserID {}",id);
        return userRepository.findById(id);
    }

    // Find a user by username
    public User findUserByUsername(String username) {
        LOG.info("findbyUserName {}",username);
        return userRepository.findByUsername(username);
    }

    // Find a user by email
    public User findUserByUserEmail(String userEmail) {

        LOG.info("findbyUserEmail{}",userEmail);
        return userRepository.findByUserEmail(userEmail);
    }

    // Get a list of all users
    public List<User> getAllUsers() {
        LOG.info("Getting all Users");
        return userRepository.findAll();
    }

    // Delete a user by ID
    public void deleteUser(Long id) {
        LOG.info("delete user with ID {}",id);
        userRepository.deleteById(id);
    }
}
