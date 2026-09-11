package hn.alturaforge.mercadox.library.jpa.repository;


import hn.alturaforge.mercadox.library.jpa.repository.custom.CustomNotificationTemplateRepository;
import hn.alturaforge.mercadox.library.entity.model.core.NotificationTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTemplateRepository extends BaseRepository<NotificationTemplate, Long>,
        CustomNotificationTemplateRepository {


}
