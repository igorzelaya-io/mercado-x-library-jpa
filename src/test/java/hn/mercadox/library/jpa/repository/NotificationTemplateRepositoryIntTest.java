package hn.mercadox.library.jpa.repository;

import hn.mercadox.library.jpa.repository.base.H2BaseJpaIntegrationTest;
import hn.alturaforge.mercadox.library.entity.model.core.NotificationTemplate;
import hn.alturaforge.mercadox.library.entity.model.enums.LanguageKey;
import hn.alturaforge.mercadox.library.entity.model.enums.TemplateChannel;
import hn.alturaforge.mercadox.library.jpa.repository.NotificationTemplateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationTemplateRepositoryIntTest extends H2BaseJpaIntegrationTest {

    @Autowired
    private NotificationTemplateRepository templateRepository;

    @Test
    void shouldReturnTemplateByNameAndChannel() {

        final NotificationTemplate invalidTemplate = NotificationTemplate
                .builder()
                .templateKey("ORDER_CONFIRMATION_TEMPLATE")
                .templateChannel(TemplateChannel.WHATSAPP)
                .languageCode(LanguageKey.SPANISH_MEX.getName())
                .subject("Invalid template")
                .active(true)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        final NotificationTemplate template = NotificationTemplate.builder()
                .templateKey("USER_VALIDATION_TEMPLATE")
                .templateChannel(TemplateChannel.EMAIL)
                .languageCode(LanguageKey.ENGLISH.getName())
                .subject("Test Email !")
                .active(true)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        template.setOrganization(organization);
        invalidTemplate.setOrganization(organization);

        persistAll(template, invalidTemplate);

        NotificationTemplate retrievedTemplate = templateRepository
                .findByOrgIdNameAndChannel("USER_VALIDATION_TEMPLATE", TemplateChannel.EMAIL);

        assertThat(retrievedTemplate).isNotNull();
        assertThat(retrievedTemplate.getOrgId()).isEqualTo(organization.getId());
        assertThat(retrievedTemplate.getId()).isEqualTo(template.getId());
    }

}
