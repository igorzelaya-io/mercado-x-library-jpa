package hn.shadowcore.mercadoxlibrary.jpa.repository;

import hn.shadowcore.mercadoxlibrary.entity.model.core.NotificationTemplate;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.LanguageKey;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.NotificationTemplateName;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.TemplateChannel;
import hn.shadowcore.mercadoxlibrary.jpa.repository.base.H2BaseJpaIntegrationTest;
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
                .templateKey(NotificationTemplateName.ORDER_CONFIRMATION_TEMPLATE.toString())
                .templateChannel(TemplateChannel.WHATSAPP)
                .languageCode(LanguageKey.SPANISH_MEX.getName())
                .subject("Invalid template")
                .active(true)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        final NotificationTemplate template = NotificationTemplate.builder()
                .templateKey(NotificationTemplateName.USER_VALIDATION_TEMPLATE.toString())
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
                .findByOrgIdNameAndChannel(NotificationTemplateName.USER_VALIDATION_TEMPLATE.toString(),
                        TemplateChannel.EMAIL);

        assertThat(retrievedTemplate).isNotNull();
        assertThat(retrievedTemplate.getOrgId()).isEqualTo(organization.getId());
        assertThat(retrievedTemplate.getId()).isEqualTo(template.getId());
    }

}
