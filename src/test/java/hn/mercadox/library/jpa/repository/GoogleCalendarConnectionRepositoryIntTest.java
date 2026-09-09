package hn.mercadox.library.jpa.repository;

import hn.mercadox.library.jpa.repository.base.H2BaseJpaIntegrationTest;
import hn.shadowcore.mercadox.library.entity.model.enums.GoogleCalendarConnectionStatus;
import hn.shadowcore.mercadox.library.jpa.repository.GoogleCalendarConnectionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GoogleCalendarConnectionRepositoryIntTest extends H2BaseJpaIntegrationTest {

    @Autowired
    private GoogleCalendarConnectionRepository repository;

    @Test
    void findsStatusOnlyForTheRequestedOrganization() {
        em.createNativeQuery("""
                        INSERT INTO appointments.google_calendar_connection (
                            id,
                            org_id,
                            google_subject,
                            google_account_email,
                            refresh_token_ciphertext,
                            credential_key_version,
                            granted_scopes,
                            status,
                            created_by_user_email
                        ) VALUES (
                            :id,
                            :orgId,
                            :subject,
                            :accountEmail,
                            :ciphertext,
                            :keyVersion,
                            :scopes,
                            :status,
                            :administratorEmail
                        )
                        """)
                .setParameter("id", UUID.randomUUID())
                .setParameter("orgId", organization.getId())
                .setParameter("subject", "google-subject")
                .setParameter("accountEmail", "calendar@clinic.example")
                .setParameter("ciphertext", "encrypted-refresh-token")
                .setParameter("keyVersion", "key-v1")
                .setParameter("scopes", "calendar.events calendar.events.freebusy")
                .setParameter("status", GoogleCalendarConnectionStatus.ACTIVE.name())
                .setParameter("administratorEmail", "admin@clinic.example")
                .executeUpdate();
        em.flush();
        em.clear();

        assertThat(repository.findStatusByOrgId(organization.getId()))
                .contains(GoogleCalendarConnectionStatus.ACTIVE);
        assertThat(repository.findStatusByOrgId(secondaryOrg.getId())).isEmpty();
    }
}
