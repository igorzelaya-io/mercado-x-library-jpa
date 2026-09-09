CREATE TABLE auth.role_permissions (
                                       role_id UUID NOT NULL,
                                       permission_id UUID NOT NULL,
                                       PRIMARY KEY (role_id, permission_id),
                                       CONSTRAINT fk_role_permissions_role
                                           FOREIGN KEY (role_id)
                                               REFERENCES auth.role(id),
                                       CONSTRAINT fk_role_permissions_permission
                                           FOREIGN KEY (permission_id)
                                               REFERENCES auth.permission(id)
);
