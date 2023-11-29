CREATE SCHEMA IF NOT EXISTS bitsbids;

CREATE TABLE bitsbids.users
(
    id           INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY NOT NULL,
    email        VARCHAR(255) UNIQUE                              NOT NULL,
    name         VARCHAR(255)                                     NOT NULL,
    batch        INTEGER                                          NOT NULL,
    room         VARCHAR(255)                                     NOT NULL,
    phone_number BIGINT                                           NOT NULL,
    balance      INTEGER                                          NOT NULL
);

CREATE TABLE bitsbids.products
(
    id              INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY NOT NULL,
    name            VARCHAR(255)                                     NOT NULL,
    description     VARCHAR(255)                                     NOT NULL,
    seller_id       INTEGER                                          NOT NULL,
    media           VARCHAR(255)[]                                   NOT NULL,
    base_price      INTEGER                                          NOT NULL,
    auto_sell_price INTEGER,
    price           INTEGER                                          NOT NULL,
    sold            BOOLEAN                                          NOT NULL,
    created_at      TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    closed_at       TIMESTAMPTZ                                      NOT NULL,
    current_bid_id  INTEGER                                          NULL,
    CONSTRAINT fk_products_seller_id FOREIGN KEY (seller_id) REFERENCES bitsbids.users (id) ON DELETE CASCADE,
    CONSTRAINT check_media_too_long CHECK (ARRAY_LENGTH(media, 1) <= 10)
);

CREATE TABLE bitsbids.categories
(
    id   INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY NOT NULL,
    name VARCHAR(50) UNIQUE                               NOT NULL
);

CREATE TABLE bitsbids.category_products
(
    id          INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY NOT NULL,
    category_id INTEGER                                          NOT NULL,
    product_id  INTEGER                                          NOT NULL,
    CONSTRAINT fk_category_products_category_id FOREIGN KEY (category_id) REFERENCES bitsbids.categories (id),
    CONSTRAINT fk_category_products_product_id FOREIGN KEY (product_id) REFERENCES bitsbids.products (id) ON DELETE CASCADE
);

CREATE TABLE bitsbids.bids
(
    id         INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY NOT NULL,
    product_id INTEGER                                          NOT NULL,
    bidder_id  INTEGER                                          NOT NULL,
    price      INTEGER                                          NOT NULL,
    placed_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_bids_product_id FOREIGN KEY (product_id) REFERENCES bitsbids.products (id) ON DELETE CASCADE,
    CONSTRAINT fk_bids_bidder_id FOREIGN KEY (bidder_id) REFERENCES bitsbids.users (id) ON DELETE CASCADE
);

CREATE TABLE bitsbids.messages
(
    id              INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY NOT NULL,
    conversation_id INTEGER                                          NOT NULL,
    from_buyer      BOOLEAN                                          NOT NULL,
    text            TEXT                                             NULL
        CONSTRAINT text_limit CHECK (LENGTH(text) <= 4000),
    media           VARCHAR(255)[]                                   NULL,
    sent_at         TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_messages_not_empty CHECK (((LENGTH(text) > 0) OR (ARRAY_LENGTH(media, 1) > 0))),
    CONSTRAINT check_media_too_long CHECK (ARRAY_LENGTH(media, 1) <= 10)
);

CREATE TABLE bitsbids.conversations
(
    id                     INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY NOT NULL,
    seller_id              INTEGER                                          NOT NULL,
    buyer_id               INTEGER                                          NOT NULL,
    product_id             INTEGER                                          NOT NULL,
    last_read_by_seller_id INTEGER                                          NULL,
    last_read_by_buyer_id  INTEGER                                          NULL,
    CONSTRAINT fk_conversations_seller_id FOREIGN KEY (seller_id) REFERENCES bitsbids.users (id) ON DELETE CASCADE,
    CONSTRAINT fk_conversations_buyer_id FOREIGN KEY (buyer_id) REFERENCES bitsbids.users (id) ON DELETE CASCADE,
    CONSTRAINT fk_conversations_product_id FOREIGN KEY (product_id) REFERENCES bitsbids.products (id) ON DELETE CASCADE,
    CONSTRAINT fk_conversations_last_read_by_seller_id FOREIGN KEY (last_read_by_seller_id) REFERENCES bitsbids.messages (id),
    CONSTRAINT fk_conversations_last_read_by_buyer_id FOREIGN KEY (last_read_by_buyer_id) REFERENCES bitsbids.messages (id)
);

ALTER TABLE bitsbids.messages
    ADD CONSTRAINT fk_messages_conversation_id FOREIGN KEY (conversation_id) REFERENCES bitsbids.conversations (id) ON DELETE CASCADE;
ALTER TABLE bitsbids.products
    ADD CONSTRAINT fk_products_current_bid_id FOREIGN KEY (current_bid_id) REFERENCES bitsbids.bids (id) ON DELETE SET NULL;

CREATE INDEX idx_products_price_created_at_closed_at ON bitsbids.products USING BTREE (price, created_at, closed_at);
CREATE INDEX idx_messages_sent_at ON bitsbids.messages USING BTREE (sent_at);
CREATE INDEX idx_bids_placed_at ON bitsbids.bids USING BTREE (placed_at);

CREATE INDEX idx_conversations_seller_id ON bitsbids.conversations (seller_id);
CREATE INDEX idx_conversations_buyer_id ON bitsbids.conversations (buyer_id);
CREATE INDEX idx_conversations_last_read_by_seller_id ON bitsbids.conversations (last_read_by_seller_id);
CREATE INDEX idx_conversations_last_read_by_buyer_id ON bitsbids.conversations (last_read_by_buyer_id);

CREATE INDEX idx_bids_product_id ON bitsbids.bids (product_id);
CREATE INDEX idx_bids_bidder_id ON bitsbids.bids (bidder_id);

CREATE INDEX idx_products_seller_id ON bitsbids.products (seller_id);
CREATE INDEX idx_user_email ON bitsbids.users (email);
