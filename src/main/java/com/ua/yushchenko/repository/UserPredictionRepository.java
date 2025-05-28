package com.ua.yushchenko.repository;

import java.util.List;
import java.util.UUID;

import com.ua.yushchenko.model.User;
import com.ua.yushchenko.model.UserPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for UserPrediction entity operations.
 * Provides methods for querying and managing UserPrediction data in the database.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Repository
public interface UserPredictionRepository extends JpaRepository<UserPrediction, UUID> {

    /**
     * Find Top 30 user predictions by user
     *
     * @param user instance of User
     * @return list of User Predictions
     */
    List<UserPrediction> findAllByUserOrderBySentAtDesc(final User user);

    /**
     * Delete {@link UserPrediction} by {@link User}
     * @param user instance of User
     */
    void deleteAllByUser(final User user);
}
