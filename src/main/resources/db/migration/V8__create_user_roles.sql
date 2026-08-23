CREATE TABLE auth.user_roles (
                                 user_id UUID NOT NULL,
                                 role_id UUID NOT NULL,
                                 PRIMARY KEY (user_id, role_id),
                                 CONSTRAINT fk_user_roles_user
                                     FOREIGN KEY (user_id)
                                         REFERENCES auth.users(id),
                                 CONSTRAINT fk_user_roles_role
                                     FOREIGN KEY (role_id)
                                         REFERENCES auth.role(id)
);
