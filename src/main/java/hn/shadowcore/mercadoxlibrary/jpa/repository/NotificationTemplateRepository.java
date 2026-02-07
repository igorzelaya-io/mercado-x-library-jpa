package hn.shadowcore.mercadoxlibrary.jpa.repository;


import hn.shadowcore.mercadoxlibrary.entity.model.core.NotificationTemplate;
import hn.shadowcore.mercadoxlibrary.jpa.repository.custom.CustomNotificationTemplateRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTemplateRepository extends BaseRepository<NotificationTemplate, Long>,
        CustomNotificationTemplateRepository {


}
