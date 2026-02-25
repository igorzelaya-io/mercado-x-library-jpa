package hn.shadowcore.mercadoxlibrary.jpa.repository.custom;

import hn.shadowcore.mercadoxlibrary.entity.model.core.NotificationTemplate;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.TemplateChannel;

public interface CustomNotificationTemplateRepository {

    NotificationTemplate findByOrgIdNameAndChannel(String name, TemplateChannel templateChannel);

}
