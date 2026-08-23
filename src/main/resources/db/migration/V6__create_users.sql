CREATE TABLE auth.users (
                             id UUID PRIMARY KEY,
                             username VARCHAR(255) NOT NULL,
                             password VARCHAR(255) NOT NULL,
                             first_name VARCHAR(255) NOT NULL,
                             last_name VARCHAR(255) NOT NULL,
                             email VARCHAR(255) NOT NULL,
                             phone_number VARCHAR(50),
                             enabled BOOLEAN NOT NULL,
                             is_admin BOOLEAN NOT NULL,
                             drive_available BOOLEAN,
                             created_at TIMESTAMP NOT NULL,
                             user_type_id UUID,
                             org_id UUID,
                             CONSTRAINT fk_user_user_type
                                 FOREIGN KEY (user_type_id)
                                     REFERENCES auth.user_type(id),
                             CONSTRAINT fk_user_org
                                 FOREIGN KEY (org_id)
                                     REFERENCES auth.organization(id)
);
