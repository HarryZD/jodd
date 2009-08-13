// Copyright (c) 2003-2009, Jodd Team (jodd.org). All Rights Reserved.

package jodd.jtx.db;

import jodd.db.DbSessionProvider;
import jodd.db.DbSession;
import jodd.db.DbSqlException;
import jodd.jtx.JtxTransactionManager;
import jodd.jtx.JtxTransactionMode;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Returns session from the db transaction manager.
 * This session provider is made for {@link DbJtxTransactionManager}.
 */
public class DbJtxSessionProvider implements DbSessionProvider {

	protected static final Logger log = LoggerFactory.getLogger(DbJtxSessionProvider.class);

	protected final JtxTransactionManager jtxTxManager;
	protected final JtxTransactionMode defaultTxMode;

	public DbJtxSessionProvider(JtxTransactionManager txManager) {
		this(txManager, null);
	}

	public DbJtxSessionProvider(JtxTransactionManager txManager, JtxTransactionMode defaultTxMode) {
		this.jtxTxManager = txManager;
		this.defaultTxMode = defaultTxMode;
	}

	/**
	 * {@inheritDoc}
	 */
	public DbSession getDbSession() {
		log.debug("Requesting db TX manager session");
		DbJtxTransaction jtx = (DbJtxTransaction) jtxTxManager.getTransaction();
		if (jtx == null) {
			if (defaultTxMode != null) {
				jtx = (DbJtxTransaction) jtxTxManager.requestTransaction(defaultTxMode, null);
				return jtx.requestResource();
			}
			throw new DbSqlException("No transaction is in progress and DbSession can't be provided. It seems that transaction manager is not used to begin a transaction.");
		}
		return jtx.requestResource();
	}

	/**
	 * {@inheritDoc}
	 */
	public void closeDbSession() {
		log.debug("Closing db TX manager session");
		DbJtxTransaction jtx = (DbJtxTransaction) jtxTxManager.getTransaction();
		if (jtx != null) {
			jtx.commit();
		}
	}
}