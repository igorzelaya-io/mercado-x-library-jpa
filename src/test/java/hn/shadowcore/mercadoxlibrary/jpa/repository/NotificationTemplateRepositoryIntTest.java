package hn.shadowcore.mercadoxlibrary.jpa.repository;

import hn.shadowcore.mercadoxlibrary.entity.model.core.NotificationTemplate;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.LanguageKey;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.NotificationTemplateName;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.TemplateChannel;
import hn.shadowcore.mercadoxlibrary.jpa.config.JpaConfig;
import hn.shadowcore.mercadoxlibrary.jpa.querydsl.OrgAwareQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = { JpaConfig.class, OrgAwareQueryFactory.class, NotificationTemplateRepository.class })
@Sql(scripts = { "classpath:schema.sql", "classpath:sequence-reset.sql" },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD )
class NotificationTemplateRepositoryIntTest extends BaseJpaIntegrationTest {

    @Autowired
    private NotificationTemplateRepository templateRepository;

    @Test
    void shouldReturnTemplateByNameAndChannel() {

        final NotificationTemplate invalidTemplate = NotificationTemplate
                .builder()
                .name(NotificationTemplateName.ORDER_CONFIRMATION_TEMPLATE.getValue())
                .templateChannel(TemplateChannel.WHATSAPP)
                .languageCode(LanguageKey.SPANISH_MEX.getName())
                .subject("Invalid template")
                .active(true)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        final NotificationTemplate template = NotificationTemplate.builder()
                .name(NotificationTemplateName.USER_VALIDATION_TEMPLATE.getValue())
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
                .findByNameAndChannel(NotificationTemplateName.USER_VALIDATION_TEMPLATE.getValue(), TemplateChannel.EMAIL);

        assertThat(retrievedTemplate).isNotNull();
        assertThat(retrievedTemplate.getOrgId()).isEqualTo(organization.getId());
        assertThat(retrievedTemplate.getId()).isEqualTo(template.getId());
    }

}
