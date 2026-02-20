package hn.shadowcore.mercadoxlibrary.jpa.aspect;


import hn.shadowcore.mercadoxcontext.utils.OrgIdContextHolder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Aspect
@Slf4j
@Component
public class HibernateFilterAspect {

    @PersistenceContext
    private EntityManager em;

    @Before("execution(* hn.shadowcore.mercadoxlibrary.jpa.repository..*(..))")
    public void enableFilters() {

        Session session = em.unwrap(Session.class);
        if(Optional.ofNullable(session.getEnabledFilter("enabledEntityFilter")).isEmpty()) {
            session.enableFilter("enabledEntityFilter")
                    .setParameter("enabled", true);
        }

        if(Optional.ofNullable(session.getEnabledFilter("orgIdFilter")).isEmpty()
                && OrgIdContextHolder.hasTenantId()) {

            final String orgId = OrgIdContextHolder.getTenantId();

            log.info(String.format("Aspect Hibernate Filter was triggered for OrgID: %s", orgId));

            session.enableFilter("orgIdFilter")
                    .setParameter("orgId", UUID.fromString(orgId));
        }
        else {
            log.warn("Aspect Hibernate Filter was NOT triggered for OrgID Context was not found!");
        }
    }


}
