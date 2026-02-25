package in.kanimozhi.invoicegeneratorapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.kanimozhi.invoicegeneratorapi.entity.User;
import in.kanimozhi.invoicegeneratorapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class ClerkWebhookController {

    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/clerk")
    public ResponseEntity<Void> handleClerkWebhook(@RequestBody String payload) {

        try {
            JsonNode rootNode = objectMapper.readTree(payload);
            String eventType = rootNode.path("type").asText();
            JsonNode data = rootNode.path("data");

            switch (eventType) {
                case "user.created" -> handleUserCreated(data);
                case "user.updated" -> handleUserUpdated(data);
                case "user.deleted" -> handleUserDeleted(data);
                default -> {
                    // ignore
                }
            }

        } catch (Exception e) {
            // 🔥 NEVER fail webhook
            e.printStackTrace();
        }

        // ✅ ALWAYS 200
        return ResponseEntity.ok().build();
    }

    private void handleUserCreated(JsonNode data) {
        String clerkId = data.path("id").asText();

        // ✅ Check without exception
        if (userService.findByClerkId(clerkId).isPresent()) {
            return;
        }

        User user = User.builder()
                .clerkId(clerkId)
                .email(
                        data.path("email_addresses")
                                .path(0)
                                .path("email_address")
                                .asText()
                )
                .firstName(data.path("first_name").asText())
                .lastName(data.path("last_name").asText())
                .photoUrl(data.path("image_url").asText())
                .build();

        userService.saveOrUpdateUser(user);
    }

    private void handleUserUpdated(JsonNode data) {
        String clerkId = data.path("id").asText();

        userService.findByClerkId(clerkId).ifPresent(existingUser -> {
            existingUser.setEmail(
                    data.path("email_addresses")
                            .path(0)
                            .path("email_address")
                            .asText()
            );
            existingUser.setFirstName(data.path("first_name").asText());
            existingUser.setLastName(data.path("last_name").asText());
            existingUser.setPhotoUrl(data.path("image_url").asText());

            userService.saveOrUpdateUser(existingUser);
        });
    }

    private void handleUserDeleted(JsonNode data) {
        String clerkId = data.path("id").asText();
        userService.deleteAccountSilently(clerkId);
    }
}