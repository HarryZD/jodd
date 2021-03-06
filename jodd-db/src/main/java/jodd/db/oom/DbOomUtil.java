// Copyright (c) 2003-2012, Jodd Team (jodd.org). All Rights Reserved.

package jodd.db.oom;

import jodd.bean.BeanUtil;

/**
 * Some utilities.
 */
public class DbOomUtil {

	/**
	 * Populates entity with generated column values from executed query.
	 */
	public static void populateGeneratedKeys(Object entity, DbOomQuery query) {
		String[] generatedColumns = query.getGeneratedColumnNames();
		if (generatedColumns == null) {
			return;
		}
		DbEntityDescriptor ded = query.getManager().lookupType(entity.getClass());

		// prepare key types
		Class[] keyTypes = new Class[generatedColumns.length];
		String[] properties = new String[generatedColumns.length];
		for (int i = 0; i < generatedColumns.length; i++) {
			String column = generatedColumns[i];
			DbEntityColumnDescriptor decd = ded.findByColumnName(column);
			if (decd != null) {
				keyTypes[i] = decd.getPropertyType();
				properties[i] = decd.getPropertyName();
			}
		}

		Object keyValues = query.findGeneratedColumns(keyTypes);
		if (keyValues.getClass().isArray() == false) {
			BeanUtil.setDeclaredProperty(entity, properties[0], keyValues);
		} else {
			for (int i = 0; i < properties.length; i++) {
				BeanUtil.setDeclaredProperty(entity, properties[i], ((Object[]) keyValues)[i]);
			}
		}
	}


	/**
	 * Returns initial collection size when max is provided.
	 */
	public static int initialCollectionSize(int max) {
		return max != 0 ? max : 10;
	}

}
