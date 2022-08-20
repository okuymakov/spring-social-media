package com.kuymakov.socialmedia.subscriptions.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kuymakov.socialmedia.subscriptions.model.Subscription;
import com.kuymakov.socialmedia.subscriptions.repository.SubscriptionsRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SubscriptionsController {

    private final SubscriptionsRepository repo;
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionsController.class);


    @GetMapping("/subscribers/get")
    public ResponseEntity<List<String>> getSubscribers(@RequestHeader String username) {
        List<Subscription> subscriptions = repo.findByUsername(username);
        var subscribers = subscriptions.stream()
                .map(Subscription::getSubscriber)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(subscribers);
    }

    @GetMapping("/subscriptions/get")
    public ResponseEntity<List<String>> getSubscriptions(@RequestHeader String username) {
        List<Subscription> subscriptions = repo.findBySubscriber(username);
        var usernames = subscriptions.stream()
                .map(Subscription::getUsername)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(usernames);
    }

    @PostMapping("/subscriptions/subscribe")
    public ResponseEntity<String> subscribe(
            @RequestHeader("username") String subscriber,
            @RequestBody String username
    ) {
        var subscription = Subscription.builder()
                .username(username)
                .subscriber(subscriber)
                .build();
        subscription = repo.insert(subscription);
        logger.info("{} subscribed to {}", subscriber, subscription.getUsername());
        return ResponseEntity.ok("Subscription " + subscription.getId() + " completed successfully!");
    }

    @DeleteMapping("/subscriptions/unsubscribe")
    public ResponseEntity<String> delete(
            @RequestHeader("username") String subscriber,
            @RequestBody String username
    ) {
        var subscriptionOpt = repo.findByUsernameAndSubscriber(username, subscriber);
        if (subscriptionOpt.isEmpty()) return ResponseEntity.badRequest().body("Subscription doesn't exist");
        var subscription = subscriptionOpt.get();

        repo.deleteById(subscription.getId().toString());
        logger.info("{} unsubscribed from {}", subscriber, subscription.getUsername());
        return ResponseEntity.ok("Subscription " + subscription.getId() + " canceled successfully!");
    }
}
