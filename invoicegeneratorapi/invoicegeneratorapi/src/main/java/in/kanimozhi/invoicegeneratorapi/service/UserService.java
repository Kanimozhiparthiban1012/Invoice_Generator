package in.kanimozhi.invoicegeneratorapi.service;

import in.kanimozhi.invoicegeneratorapi.entity.User;
import in.kanimozhi.invoicegeneratorapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // ✅ Used by webhook (SAFE)
    public User saveOrUpdateUser(User user) {
        return userRepository.findByClerkId(user.getClerkId())
                .map(existingUser -> {
                    existingUser.setEmail(user.getEmail());
                    existingUser.setFirstName(user.getFirstName());
                    existingUser.setLastName(user.getLastName());
                    existingUser.setPhotoUrl(user.getPhotoUrl());
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> userRepository.save(user));
    }

    // ✅ SAFE delete (NO exception)
    public void deleteAccountSilently(String clerkId) {
        userRepository.findByClerkId(clerkId)
                .ifPresent(userRepository::delete);
    }

    // ✅ ONLY for login / secured APIs
    public User getAccountByClerkId(String clerkId) {
        return userRepository.findByClerkId(clerkId)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
    }

    // ✅ Used by webhook to check existence
    public Optional<User> findByClerkId(String clerkId) {
        return userRepository.findByClerkId(clerkId);
    }
}