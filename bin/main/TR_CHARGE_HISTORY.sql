CREATE TABLE TR_CHARGE_HISTORY (

	SALES_HISTORY_ID BIGINT NOT NULL,

	STRIPE_CHARGE_ID VARCHAR(32) NOT NULL,
	STRIPE_BALANCE_TRANSACTION_ID VARCHAR(32) NOT NULL,
	STRIPE_CARD_ID VARCHAR(32) NOT NULL,

	BILLING_NAME VARCHAR(256) NOT NULL,
	BILLING_ZIPCODE VARCHAR(7) NOT NULL,
	BILLING_ADDRESS VARCHAR(256) NOT NULL,
	BILLING_E_MAIL VARCHAR(512) NOT NULL
);