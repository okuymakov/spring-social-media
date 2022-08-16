package subscriptions.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import subscriptions.model.Subscription;

import java.util.List;
import java.util.Optional;


public interface SubscriptionsRepository extends MongoRepository<Subscription, String> {
    @Query("{'username': ?0}")
    List<Subscription> findByUsername(String username);

    @Query("{'subscriber': ?0}")
    List<Subscription> findBySubscriber(String subscriber);

    @Query("{'username': ?0, 'subscriber': ?1}")
    Optional<Subscription> findByUsernameAndSubscriber(String username, String subscriber);
}
