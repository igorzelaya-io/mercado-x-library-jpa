package hn.shadowcore.mercadox.library.jpa.repository;


import hn.shadowcore.mercadox.library.jpa.repository.custom.CustomNotificationTemplateRepository;
import hn.shadowcore.mercadox.library.entity.model.core.NotificationTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTemplateRepository extends BaseRepository<NotificationTemplate, Long>,
        CustomNotificationTemplateRepository {


}
