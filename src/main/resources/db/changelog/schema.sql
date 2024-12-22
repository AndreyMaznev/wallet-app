CREATE TABLE wallet (
                        wallet_id UUID PRIMARY KEY,
                        balance DECIMAL NOT NULL,
                        version BIGINT NOT NULL
);