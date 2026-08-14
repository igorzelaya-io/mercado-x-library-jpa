package hn.shadowcore.mercadox.library.jpa.repository;

import hn.shadowcore.mercadox.library.entity.model.auth.UserNotificationPreference;
import hn.shadowcore.mercadox.library.entity.model.enums.TemplateChannel;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserNotificationPreferenceRepository extends BaseRepository<UserNotificationPreference, UUID> {
    Optional<UserNotificationPreference> findByUserIdAndChannel(UUID userId, TemplateChannel channel);
}
