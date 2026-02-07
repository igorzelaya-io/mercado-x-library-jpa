package hn.shadowcore.mercadoxlibrary.jpa.repository.custom;

import hn.shadowcore.mercadoxlibrary.entity.model.core.NotificationTemplate;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.TemplateChannel;

import java.util.Optional;

public interface CustomNotificationTemplateRepository {

    NotificationTemplate findByNameAndChannel(String name, TemplateChannel templateChannel);

}
