package hn.alturaforge.mercadox.library.jpa.repository.custom.impl;

import hn.alturaforge.mercadox.library.jpa.querydsl.OrgAwareQueryFactory;
import hn.alturaforge.mercadox.library.entity.model.core.NotificationTemplate;
import hn.alturaforge.mercadox.library.entity.model.core.QNotificationTemplate;
import hn.alturaforge.mercadox.library.entity.model.enums.TemplateChannel;
import hn.alturaforge.mercadox.library.jpa.repository.custom.CustomNotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomNotificationTemplateRepositoryImpl implements CustomNotificationTemplateRepository {

    private final OrgAwareQueryFactory queryFactory;

    private static final QNotificationTemplate qNotificationTemplate = QNotificationTemplate.notificationTemplate;

    @Override
    public NotificationTemplate findByOrgIdNameAndChannel
            (String name, TemplateChannel templateChannel) {
        return Optional.ofNullable(queryFactory.selectFrom(qNotificationTemplate, qNotificationTemplate.orgId)
                .where(qNotificationTemplate.active.isTrue()
                        .and(qNotificationTemplate.templateKey.equalsIgnoreCase(name)
                                .and(qNotificationTemplate.templateChannel.eq(templateChannel))))
                .fetchOne())
                .orElseThrow(() -> new ResourceNotFoundException(String
                        .format("Notification template was not found for name: '%s'", name)));
    }
}
