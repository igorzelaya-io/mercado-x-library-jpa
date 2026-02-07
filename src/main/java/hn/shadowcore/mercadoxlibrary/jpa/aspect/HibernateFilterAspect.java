package hn.shadowcore.mercadoxlibrary.jpa.aspect;


import hn.shadowcore.mercadoxcontext.utils.OrgIdContextHolder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Aspect
@Component
public class HibernateFilterAspect {

    @PersistenceContext
    private EntityManager em;

    @Before("execution(* org.springframework.data.repository.Repository+.*(..))")
    public void enableFilters() {
        Session session = em.unwrap(Session.class);
        if(Optional.ofNullable(session.getEnabledFilter("enabledEntityFilter")).isEmpty()) {
            session.enableFilter("enabledEntityFilter")
                    .setParameter("enabled", true);
        }

        if(Optional.ofNullable(session.getEnabledFilter("orgIdFilter")).isEmpty()
                && OrgIdContextHolder.hasTenantId()) {
            session.enableFilter("orgIdFilter")
                    .setParameter("orgId", UUID.fromString(OrgIdContextHolder.getTenantId()));
        }
    }


}
