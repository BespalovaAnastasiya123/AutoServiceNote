package org.project.repo;

import org.project.models.Notification;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
}