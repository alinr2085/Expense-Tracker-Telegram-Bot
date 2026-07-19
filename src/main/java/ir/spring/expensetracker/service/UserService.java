package ir.spring.expensetracker.service;

import ir.spring.expensetracker.entity.User;
import ir.spring.expensetracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User findOrCreateUser(Long telegramId, String username, String firstName, String lastName) {
        return userRepository.findByTelegramId(telegramId).orElseGet(() -> {
            User newUser = new User();
            newUser.setTelegramId(telegramId);
            newUser.setUsername(username);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            return userRepository.save(newUser);
        });
    }

}
