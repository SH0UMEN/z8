package org.zenframework.z8.server.base.form.action;

import java.util.Collection;

import org.zenframework.z8.server.db.FieldType;
import org.zenframework.z8.server.json.Json;
import org.zenframework.z8.server.json.JsonWriter;
import org.zenframework.z8.server.runtime.IObject;
import org.zenframework.z8.server.runtime.OBJECT;
import org.zenframework.z8.server.runtime.RCollection;
import org.zenframework.z8.server.types.bool;
import org.zenframework.z8.server.types.date;
import org.zenframework.z8.server.types.datespan;
import org.zenframework.z8.server.types.decimal;
import org.zenframework.z8.server.types.file;
import org.zenframework.z8.server.types.guid;
import org.zenframework.z8.server.types.integer;
import org.zenframework.z8.server.types.primary;
import org.zenframework.z8.server.types.string;

public class Parameter extends OBJECT {
	public static class CLASS<T extends Parameter> extends OBJECT.CLASS<T> {
		public CLASS() {
			this(null);
		}

		public CLASS(IObject container) {
			super(container);
			setJavaClass(Parameter.class);
		}

		@Override
		public Object newObject(IObject container) {
			return new Parameter(container);
		}
	}

	public string text = new string();

	protected FieldType type = FieldType.None;
	protected Object value = new string(); // primary or RCollection<primary>

	public Parameter(IObject container) {
		super(container);
	}

	@Override
	public String id() {
		return text.get();
	}

	@Override
	public String displayName() {
		return text.get();
	}

	public FieldType getType() {
		if(type != FieldType.None)
			return type;

		if(value instanceof bool)
			return type = FieldType.Boolean;
		else if(value instanceof date)
			return type = FieldType.Date;
		else if(value instanceof decimal)
			return type = FieldType.Decimal;
		else if(value instanceof integer)
			return type = FieldType.Integer;
		else if(value instanceof guid)
			return type = FieldType.Guid;
		else
			return type = FieldType.String;
	}

	public Object get() {
		return value;
	}

	public void set(Object value) {
		this.value = value;
	}

	public void parse(String json) {
		set(parse(json, getType()));
	}

	public primary parse(String value, FieldType type) {
		switch(type) {
		case String:
			return new string(value);
		case Integer:
			return new integer(value);
		case Decimal:
			return new decimal(value);
		case Boolean:
			return new bool(value);
		case Date:
		case Datetime:
			return value.isEmpty() ? new date() : new date(value);
		case Datespan:
			return value.isEmpty() ? new datespan() : new datespan(value);
		case Guid:
			return new guid(value);
		default:
			throw new RuntimeException("Unsupported parameter type: '" + type + "'");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void write(JsonWriter writer) {
		writer.writeProperty(Json.id, text);
		writer.writeProperty(Json.text, text);
		writer.writeProperty(Json.type, getType().toString());

		if(value instanceof RCollection) {
			writer.startArray(Json.value);
			for(primary item : (Collection<primary>)value)
				writer.write(item);
			writer.finishArray();
		} else
			writer.writeProperty(Json.value, (primary)value);
	}

	public bool bool() {
		return (bool)value;
	}

	public guid guid() {
		return (guid)value;
	}

	public integer integer() {
		return (integer)value;
	}

	public date date() {
		return (date)value;
	}

	public decimal decimal() {
		return (decimal)value;
	}

	public file file() {
		return (file)value;
	}

	public string string() {
		return (string)value;
	}

	static public Parameter.CLASS<? extends Parameter> z8_create(string name, primary value) {
		Parameter.CLASS<Parameter> parameter = new Parameter.CLASS<Parameter>();
		parameter.get().text = name;
		parameter.get().value = value;
		return parameter;
	}

	static public Parameter.CLASS<? extends Parameter> z8_create(string name, FieldType type) {
		Parameter.CLASS<Parameter> parameter = new Parameter.CLASS<Parameter>();
		parameter.get().text = name;
		parameter.get().type = type;
		return parameter;
	}
}
