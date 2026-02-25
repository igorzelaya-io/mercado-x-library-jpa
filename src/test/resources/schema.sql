/* =========================================================
   SCHEMAS
   ========================================================= */

CREATE SCHEMA IF NOT EXISTS auth;
CREATE SCHEMA IF NOT EXISTS core;
CREATE SCHEMA IF NOT EXISTS invoicing;

/* =========================================================
   SEQUENCES
   ========================================================= */

CREATE SEQUENCE IF NOT EXISTS auth.refresh_token_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS auth.audit_log_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS core.notification_template_id_seq START WITH 1 INCREMENT BY 1;

/* =========================================================
   AUTH SCHEMA
   ========================================================= */

/* Base Tables */

CREATE TABLE auth.organization (
                                   id UUID PRIMARY KEY,
                                   name VARCHAR(255) NOT NULL,
                                   enabled BOOLEAN NOT NULL,
                                   created_at TIMESTAMP NOT NULL,
                                   org_admin_id VARCHAR(255)
);

CREATE TABLE auth.user_type (
                                id UUID PRIMARY KEY,
                                name VARCHAR(50) NOT NULL,
                                description VARCHAR(124)
);

CREATE TABLE auth.permission (
                                 id UUID PRIMARY KEY,
                                 name VARCHAR(255) NOT NULL
);

CREATE TABLE auth.role (
                           id UUID PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           org_id UUID,
                           CONSTRAINT fk_role_org
                               FOREIGN KEY (org_id)
                                   REFERENCES auth.organization(id)
);

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

CREATE TABLE auth.branch (
                             id UUID PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             org_id UUID,
                             location_id UUID,
                             CONSTRAINT fk_branch_org
                                 FOREIGN KEY (org_id)
                                     REFERENCES auth.organization(id)
);

CREATE TABLE auth.audit_log (
                                id BIGINT DEFAULT (NEXT VALUE FOR auth.audit_log_seq) PRIMARY KEY,
                                user_id UUID,
                                org_id UUID,

                                action VARCHAR(255) NOT NULL,
                                timestamp TIMESTAMP NOT NULL,

                                CONSTRAINT fk_audit_log_user
                                    FOREIGN KEY (user_id)
                                        REFERENCES auth.users(id),

                                CONSTRAINT fk_audit_log_org
                                    FOREIGN KEY (org_id)
                                        REFERENCES auth.organization(id)
);


CREATE TABLE auth.refresh_tokens (
                                     id BIGINT DEFAULT (NEXT VALUE FOR auth.refresh_token_seq) PRIMARY KEY,
                                     org_id UUID NOT NULL,
                                     user_id UUID NOT NULL,
                                     expires_at TIMESTAMP NOT NULL,

                                     CONSTRAINT fk_refresh_token_org
                                         FOREIGN KEY (org_id)
                                             REFERENCES auth.organization(id),

                                     CONSTRAINT fk_refresh_token_user
                                         FOREIGN KEY (user_id)
                                             REFERENCES auth.users(id)
);

CREATE INDEX idx_branch_org_id ON auth.branch(org_id);

/* =========================================================
   CORE SCHEMA
   ========================================================= */

/* Category must exist before Inventory */

CREATE TABLE core.category (
                               id UUID PRIMARY KEY,
                               name VARCHAR(255) NOT NULL,
                               enabled BOOLEAN NOT NULL,
                               org_id UUID,
                               CONSTRAINT fk_category_org
                                   FOREIGN KEY (org_id)
                                       REFERENCES auth.organization(id)
);

CREATE TABLE core.inventory (
                                id UUID PRIMARY KEY,
                                name VARCHAR(255),
                                quantity INTEGER,
                                org_id UUID NOT NULL,
                                branch_id UUID,
                                category_id UUID,
                                CONSTRAINT fk_inventory_org
                                    FOREIGN KEY (org_id)
                                        REFERENCES auth.organization(id),
                                CONSTRAINT fk_inventory_branch
                                    FOREIGN KEY (branch_id)
                                        REFERENCES auth.branch(id),
                                CONSTRAINT fk_inventory_category
                                    FOREIGN KEY (category_id)
                                        REFERENCES core.category(id)
);

CREATE TABLE core.service_category (
                                       id UUID PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
                                       org_id UUID NOT NULL,
                                       CONSTRAINT fk_service_category_org
                                           FOREIGN KEY (org_id)
                                               REFERENCES auth.organization(id)
);

CREATE TABLE core.location (
                               id UUID PRIMARY KEY,
                               address VARCHAR(255) NOT NULL,
                               location_reference VARCHAR(255) NOT NULL,
                               user_id UUID,
                               CONSTRAINT fk_location_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES auth.users(id)
);

CREATE TABLE core.shipment (
                               id UUID PRIMARY KEY,
                               location_id UUID,
                               placed_at TIMESTAMP,
                               delivered_at TIMESTAMP,
                               shipment_status VARCHAR(50),
                               customer_id UUID,
                               employee_id UUID,
                               org_id UUID,
                               CONSTRAINT fk_shipment_location
                                   FOREIGN KEY (location_id)
                                       REFERENCES core.location(id),
                               CONSTRAINT fk_shipment_customer
                                   FOREIGN KEY (customer_id)
                                       REFERENCES auth.users(id),
                               CONSTRAINT fk_shipment_employee
                                   FOREIGN KEY (employee_id)
                                       REFERENCES auth.users(id),
                               CONSTRAINT fk_shipment_org
                                   FOREIGN KEY (org_id)
                                       REFERENCES auth.organization(id)
);

CREATE TABLE core.orders (
                              id VARCHAR(64) PRIMARY KEY,
                              order_status VARCHAR(50) NOT NULL,
                              created_at TIMESTAMP,
                              dispatched_by VARCHAR(255),
                              delivery_id VARCHAR(255),
                              user_id UUID,
                              shipment_id UUID,
                              org_id UUID,
                              CONSTRAINT fk_order_user
                                  FOREIGN KEY (user_id)
                                      REFERENCES auth.users(id),
                              CONSTRAINT fk_order_shipment
                                  FOREIGN KEY (shipment_id)
                                      REFERENCES core.shipment(id),
                              CONSTRAINT fk_order_org
                                  FOREIGN KEY (org_id)
                                      REFERENCES auth.organization(id)
);

CREATE TABLE invoicing.invoice (
                                   id UUID PRIMARY KEY,
                                   total_price NUMERIC(14,2) NOT NULL,
                                   subtotal NUMERIC(14,2) NOT NULL,
                                   discount NUMERIC(14,2),
                                   invoice_date TIMESTAMP NOT NULL,
                                   void_due_date TIMESTAMP,
                                   invoice_number VARCHAR(255),
                                   invoice_url VARCHAR(500),
                                   tax1 NUMERIC(14,2) NOT NULL,
                                   tax2 NUMERIC(14,2),
                                   tax3 NUMERIC(14,2),
                                   user_id UUID,
                                   org_id UUID,
                                   shipping_id UUID,
                                   order_id VARCHAR(64),
                                   CONSTRAINT fk_invoice_user
                                       FOREIGN KEY (user_id)
                                           REFERENCES auth.users(id),
                                   CONSTRAINT fk_invoice_org
                                       FOREIGN KEY (org_id)
                                           REFERENCES auth.organization(id),
                                   CONSTRAINT fk_invoice_shipping
                                       FOREIGN KEY (shipping_id)
                                           REFERENCES core.shipment(id),
                                   CONSTRAINT fk_invoice_order
                                       FOREIGN KEY (order_id)
                                           REFERENCES core.orders(id)
);

CREATE TABLE core.item (
                           id UUID PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           description TEXT NOT NULL,
                           unit_quantity INTEGER NOT NULL,
                           unit_price NUMERIC(12,2) NOT NULL,
                           in_stock BOOLEAN NOT NULL,
                           image VARCHAR(255),
                           org_id UUID,
                           category_id UUID,
                           inventory_id UUID,
                           shipping_id UUID,
                           CONSTRAINT fk_item_org
                               FOREIGN KEY (org_id)
                                   REFERENCES auth.organization(id),
                           CONSTRAINT fk_item_category
                               FOREIGN KEY (category_id)
                                   REFERENCES core.category(id),
                           CONSTRAINT fk_item_inventory
                               FOREIGN KEY (inventory_id)
                                   REFERENCES core.inventory(id),
                           CONSTRAINT fk_shipment_id_fk
                               FOREIGN KEY(shipping_id)
                                   REFERENCES core.shipment(id)
);

CREATE TABLE core.order_items (
                                  item_id UUID NOT NULL,
                                  order_id VARCHAR(64) NOT NULL,
                                  quantity INTEGER,
                                  PRIMARY KEY (item_id, order_id),
                                  CONSTRAINT fk_order_item_item
                                      FOREIGN KEY (item_id)
                                          REFERENCES core.item(id),
                                  CONSTRAINT fk_order_item_order
                                      FOREIGN KEY (order_id)
                                          REFERENCES core.orders(id)
);

CREATE TABLE core.item_branch_availability (
                                               item_id UUID NOT NULL,
                                               branch_id UUID NOT NULL,
                                               PRIMARY KEY (item_id, branch_id),
                                               CONSTRAINT fk_item_branch_item
                                                   FOREIGN KEY (item_id)
                                                       REFERENCES core.item(id),
                                               CONSTRAINT fk_item_branch_branch
                                                   FOREIGN KEY (branch_id)
                                                       REFERENCES auth.branch(id)
);

CREATE TABLE core.service (
                              id UUID PRIMARY KEY,
                              name VARCHAR(255) NOT NULL,
                              description TEXT NOT NULL,
                              service_category_id UUID NOT NULL,
                              org_id UUID NOT NULL,
                              CONSTRAINT fk_service_category
                                  FOREIGN KEY (service_category_id)
                                      REFERENCES core.service_category(id),
                              CONSTRAINT fk_service_org
                                  FOREIGN KEY (org_id)
                                      REFERENCES auth.organization(id)
);

CREATE TABLE core.service_branch_availability (
                                                  service_id UUID NOT NULL,
                                                  branch_id UUID NOT NULL,
                                                  PRIMARY KEY (service_id, branch_id),
                                                  CONSTRAINT fk_service_branch_service
                                                      FOREIGN KEY (service_id)
                                                          REFERENCES core.service(id),
                                                  CONSTRAINT fk_service_branch_branch
                                                      FOREIGN KEY (branch_id)
                                                          REFERENCES auth.branch(id)
);

CREATE TABLE core.appointment (
                                  id UUID PRIMARY KEY,
                                  service_id UUID,
                                  user_id UUID,
                                  branch_id UUID,
                                  CONSTRAINT fk_appointment_service
                                      FOREIGN KEY (service_id)
                                          REFERENCES core.service(id),
                                  CONSTRAINT fk_appointment_user
                                      FOREIGN KEY (user_id)
                                          REFERENCES auth.users(id),
                                  CONSTRAINT fk_appointment_branch
                                      FOREIGN KEY (branch_id)
                                          REFERENCES auth.branch(id)
);

CREATE TABLE core.notification_template (
                                            id BIGINT DEFAULT (NEXT VALUE FOR core.notification_template_id_seq) PRIMARY KEY,
                                            template_key VARCHAR(100) NOT NULL,
                                            channel VARCHAR(50) NOT NULL,
                                            language_code VARCHAR(10) NOT NULL,
                                            org_id UUID NOT NULL,
                                            subject VARCHAR(255),
                                            whatsapp_template_name VARCHAR(100),
                                            body_html TEXT,
                                            active BOOLEAN,
                                            system_template BOOLEAN,
                                            created_at TIMESTAMP,
                                            updated_at TIMESTAMP,
                                            CONSTRAINT fk_notification_template_org
                                                FOREIGN KEY (org_id)
                                                    REFERENCES auth.organization(id),
                                            CONSTRAINT uq_template_unique
                                                UNIQUE(org_id, template_key, channel, language_code)
);

CREATE TABLE core.notification_template_variables (
                                                      notification_template_id BIGINT NOT NULL,
                                                      variable VARCHAR(255),
                                                      variable_order INT NOT NULL,
                                                      CONSTRAINT pk_template_vars
                                                          PRIMARY KEY (notification_template_id, variable_order),
                                                      CONSTRAINT fk_template_vars_template
                                                          FOREIGN KEY (notification_template_id)
                                                              REFERENCES core.notification_template(id)
);

CREATE TABLE core.leads (
                            id UUID PRIMARY KEY,
                            lead_status VARCHAR(50) NOT NULL,
                            user_name VARCHAR(255) NOT NULL,
                            org_name VARCHAR(255) NOT NULL,
                            email VARCHAR(255) NOT NULL,
                            phone_number VARCHAR(50),
                            message TEXT
);

/* =========================================================
   INVOICING SCHEMA
   ========================================================= */

CREATE TABLE invoicing.invoice_range (
                                         id UUID PRIMARY KEY,
                                         approved_range_prefix VARCHAR(50),
                                         range_due_date TIMESTAMP,
                                         range_start INTEGER NOT NULL,
                                         range_current INTEGER NOT NULL,
                                         range_end INTEGER NOT NULL,
                                         branch_id UUID,
                                         CONSTRAINT fk_invoice_range_branch
                                             FOREIGN KEY (branch_id)
                                                 REFERENCES auth.branch(id)
);

CREATE TABLE invoicing.payment (
                                   id UUID PRIMARY KEY,
                                   payment_status VARCHAR(50) NOT NULL,
                                   org_id UUID NOT NULL,
                                   user_id UUID NOT NULL,
                                   item_id UUID,
                                   invoice_id UUID,
                                   CONSTRAINT fk_payment_org
                                       FOREIGN KEY (org_id)
                                           REFERENCES auth.organization(id),
                                   CONSTRAINT fk_payment_user
                                       FOREIGN KEY (user_id)
                                           REFERENCES auth.users(id),
                                   CONSTRAINT fk_payment_item
                                       FOREIGN KEY (item_id)
                                           REFERENCES core.item(id),
                                   CONSTRAINT fk_payment_invoice
                                       FOREIGN KEY (invoice_id)
                                           REFERENCES invoicing.invoice(id)
);