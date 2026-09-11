package hn.alturaforge.mercadox.library.jpa.repository;

import hn.alturaforge.mercadox.library.entity.model.appointments.GoogleCalendarConnection;
import hn.alturaforge.mercadox.library.entity.model.enums.GoogleCalendarConnectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoogleCalendarConnectionRepository
        extends JpaRepository<GoogleCalendarConnection, UUID> {

    @Query("select connection.status from GoogleCalendarConnection connection "
            + "where connection.orgId = :orgId")
    Optional<GoogleCalendarConnectionStatus> findStatusByOrgId(@Param("orgId") UUID orgId);
}
