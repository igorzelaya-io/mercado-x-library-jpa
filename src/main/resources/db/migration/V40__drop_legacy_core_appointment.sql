-- The scheduling bounded context is now owned by mercado-x-appointments.
-- Do not use CASCADE: unexpected dependants should stop the migration for review.
DROP TABLE IF EXISTS core.appointment;
