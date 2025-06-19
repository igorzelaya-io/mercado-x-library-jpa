package hn.shadowcore.mercadoxlibrary.jpa.filter;

import lombok.NonNull;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.spi.BootstrapContext;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;


public class HibernateFilterIntegrator implements Integrator {

    private final FilterEnablingPreLoadListener listener;

    public HibernateFilterIntegrator() {
        this.listener = new FilterEnablingPreLoadListener();
    }

    @Override
    public void integrate(@NonNull Metadata metadata, @NonNull BootstrapContext bootstrapContext,
                          SessionFactoryImplementor sessionFactory) {
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        if(registry != null) {
            registry.appendListeners(EventType.PRE_LOAD, listener);
        }
    }

    @Override
    public void disintegrate(@NonNull SessionFactoryImplementor sessionFactoryImplementor,
                             @NonNull  SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {

    }
}
