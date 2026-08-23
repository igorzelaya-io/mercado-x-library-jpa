CREATE TABLE core.location (
                               id UUID PRIMARY KEY,
                               address VARCHAR(255) NOT NULL,
                               location_reference VARCHAR(255) NOT NULL,
                               user_id UUID,
                               CONSTRAINT fk_location_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES auth.users(id)
);
