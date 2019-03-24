CREATE TABLE user_token (
  token VARCHAR NOT NULL PRIMARY KEY,
  user_account_id VARCHAR NOT NULL,
  FOREIGN KEY (user_account_id) REFERENCES user_account(id)
);