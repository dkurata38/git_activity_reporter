
ALTER TABLE user_account ADD COLUMN registration_status INTEGER;

UPDATE user_account SET registration_status = 1;

ALTER TABLE user_account ALTER COLUMN registration_status SET NOT NULL;