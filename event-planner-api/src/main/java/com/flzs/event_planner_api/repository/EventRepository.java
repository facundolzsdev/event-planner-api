package com.flzs.event_planner_api.repository;

import com.flzs.event_planner_api.model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByUser(User user);

    Optional<Event> findByIdAndUser(Long id, User user);
}
