package hn.shadowcore.mercadox.library.jpa.repository.custom;

import hn.shadowcore.mercadox.library.entity.model.core.NotificationTemplate;
import hn.shadowcore.mercadox.library.entity.model.enums.TemplateChannel;

public interface CustomNotificationTemplateRepository {

    NotificationTemplate findByOrgIdNameAndChannel(String name, TemplateChannel templateChannel);

}
