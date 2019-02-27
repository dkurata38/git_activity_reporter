CREATE TABLE "user_account" (
                     "id" VARCHAR NOT NULL PRIMARY KEY
);

CREATE TABLE "git_account" (
                      "user_account_id" VARCHAR NOT NULL,
                      "client_id" VARCHAR NOT NULL,
                      "user_name" VARCHAR NOT NULL,
                      "access_token" VARCHAR NOT NULL,
                      PRIMARY KEY (client_id, user_name),
                      FOREIGN KEY (user_account_id) REFERENCES "user_account"(id)
);

CREATE TABLE "social_account" (
                             "user_account_id" VARCHAR NOT NULL,
                             "client_id" VARCHAR NOT NULL,
                             "user_name" VARCHAR NOT NULL,
                             "access_token" VARCHAR NOT NULL,
                             "access_token_secret" VARCHAR NOT NULL,
                             PRIMARY KEY (client_id, user_name),
                             FOREIGN KEY (user_account_id) REFERENCES "user_account"(id)
);
