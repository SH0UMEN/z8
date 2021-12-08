package org.zenframework.z8.server.db;

import java.sql.SQLException;

import org.zenframework.z8.server.db.sql.FormatOptions;

public class CountingSelect extends Select {
	public CountingSelect() {
		super();
	}

	public CountingSelect(Select select) {
		super(select);
	}

	@Override
	protected String getSql(FormatOptions options) {
		setFields(null);
		setOrderBy(null);

		if(isGrouped()) {
			setSubselect(new Select(this));

			setRootQuery(null);
			setLinks(null);
			setWhere(null);
			setHaving(null);

			setGroupBy(null);
		}

		return super.getSql(options);
	}

	public int count() {
		try {
			open();
			int result = next() ? getCursor().getInteger(1).getInt() : 0;
			close();
			return result;
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
