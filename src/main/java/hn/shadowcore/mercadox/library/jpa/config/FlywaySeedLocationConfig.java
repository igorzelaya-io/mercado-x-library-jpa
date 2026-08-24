package hn.shadowcore.mercadox.library.jpa.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Adds the shared {@code db/seed} scripts to Flyway's scan path for the "local"
 * profile only.
 *
 * <p>The seed SQL lives in this library (single source of truth). This auto-
 * configuration is the wiring counterpart: any service that depends on
 * mercado-x-library-jpa picks it up automatically via
 * {@code META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports},
 * so no per-service Flyway configuration is required.
 *
 * <p>Gating:
 * <ul>
 *   <li>{@code @Profile("local")} — seed data is applied locally only, never in prod.</li>
 *   <li>{@code @ConditionalOnClass(Flyway.class)} — services without Flyway on the
 *       classpath (e.g. mercado-x-email) skip this entirely.</li>
 * </ul>
 *
 * <p>Existing locations (typically {@code classpath:db/migration}) are preserved;
 * {@code classpath:db/seed} is appended. Seed scripts are idempotent repeatable
 * ({@code R__}) migrations, so replays are safe.
 */
@AutoConfiguration
@Profile("local")
@ConditionalOnClass(Flyway.class)
public class FlywaySeedLocationConfig {

    static final Location SEED_LOCATION = new Location("classpath:db/seed");

    @Bean
    public FlywayConfigurationCustomizer seedDataLocationCustomizer() {
        return configuration -> {
            List<Location> locations =
                    new ArrayList<>(Arrays.asList(configuration.getLocations()));
            if (!locations.contains(SEED_LOCATION)) {
                locations.add(SEED_LOCATION);
            }
            configuration.locations(locations.toArray(new Location[0]));
        };
    }
}
