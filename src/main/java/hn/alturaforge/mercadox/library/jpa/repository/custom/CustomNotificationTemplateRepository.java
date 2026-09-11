package hn.alturaforge.mercadox.library.jpa.repository.custom;

import hn.alturaforge.mercadox.library.entity.model.core.NotificationTemplate;
import hn.alturaforge.mercadox.library.entity.model.enums.TemplateChannel;

public interface CustomNotificationTemplateRepository {

    NotificationTemplate findByOrgIdNameAndChannel(String name, TemplateChannel templateChannel);

}
