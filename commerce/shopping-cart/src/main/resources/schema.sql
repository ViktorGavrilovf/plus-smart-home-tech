CREATE SCHEMA IF NOT EXISTS shopping_cart;

CREATE TABLE IF NOT EXISTS shopping_cart.carts (
    shopping_cart_id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    state VARCHAR(20) NOT NULL CHECK (state IN ('ACTIVE', 'DEACTIVATED')) DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS shopping_cart.cart_items (
    id UUID PRIMARY KEY,
    shopping_cart_id UUID NOT NULL REFERENCES shopping_cart.carts (shopping_cart_id) ON DELETE CASCADE,
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL CHECK ( quantity >= 0 ),
    CONSTRAINT unique_cart_product UNIQUE (shopping_cart_id, product_id)
)