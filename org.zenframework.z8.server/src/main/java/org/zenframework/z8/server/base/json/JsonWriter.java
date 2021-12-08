package org.zenframework.z8.server.base.json;

import org.zenframework.z8.server.base.table.value.Field;
import org.zenframework.z8.server.runtime.IObject;
import org.zenframework.z8.server.runtime.OBJECT;
import org.zenframework.z8.server.runtime.RCollection;
import org.zenframework.z8.server.types.primary;
import org.zenframework.z8.server.types.string;

@SuppressWarnings("unchecked")
public class JsonWriter extends OBJECT {

	public static class CLASS<T extends JsonWriter> extends OBJECT.CLASS<T> {
		public CLASS(IObject container) {
			super(container);
			setJavaClass(JsonWriter.class);
		}

		@Override
		public Object newObject(IObject container) {
			return new JsonWriter(container);
		}
	}

	private org.zenframework.z8.server.json.JsonWriter writer = new org.zenframework.z8.server.json.JsonWriter(true);

	public JsonWriter() {
		super(null);
	}

	public JsonWriter(IObject container) {
		super(container);
	}

	public org.zenframework.z8.server.json.JsonWriter get() {
		return writer;
	}

	public void set(org.zenframework.z8.server.json.JsonWriter writer) {
		this.writer = writer;
	}

	public CLASS<JsonWriter> z8_startObject(string name) {
		writer.startObject(name.get());
		return (CLASS<JsonWriter>) getCLASS();
	}

	public CLASS<JsonWriter> z8_startObject() {
		writer.startObject();
		return (CLASS<JsonWriter>) getCLASS();
	}

	public CLASS<JsonWriter> z8_finishObject() {
		writer.finishObject();
		return (CLASS<JsonWriter>) getCLASS();
	}

	public CLASS<JsonWriter> z8_startArray(string name) {
		writer.startArray(name.get());
		return (CLASS<JsonWriter>) getCLASS();
	}

	public CLASS<JsonWriter> z8_startArray() {
		writer.startArray();
		return (CLASS<JsonWriter>) getCLASS();
	}

	public CLASS<JsonWriter> z8_finishArray() {
		writer.finishArray();
		return (CLASS<JsonWriter>) getCLASS();
	}

	public CLASS<JsonWriter> z8_write(primary value) {
		writer.write(value);
		return (CLASS<JsonWriter>) getCLASS();
	}

	@SuppressWarnings("rawtypes")
	public CLASS<JsonWriter> z8_write(RCollection value) {
		writer.startArray();
		for(Object v : value) {
			if (v instanceof primary)
				writer.write((primary) v);
			else if (v instanceof OBJECT.CLASS)
				z8_write((OBJECT.CLASS<? extends OBJECT>) v);
		}
		writer.finishArray();
		return (CLASS<JsonWriter>) getCLASS();
	}

	public CLASS<JsonWriter> z8_write(OBJECT.CLASS<? extends OBJECT> value) {
		writer.write(value);
		return (CLASS<JsonWriter>) getCLASS();
	}

	public CLASS<JsonWriter> z8_writeProperty(string name, primary value) {
		writer.writeProperty(name.get(), value);
		return (CLASS<JsonWriter>) getCLASS();
	}

	public CLASS<JsonWriter> z8_writeProperty(string name, OBJECT.CLASS<? extends OBJECT> value) {
		writer.writeProperty(name.get(), value);
		return (CLASS<JsonWriter>) getCLASS();
	}

	@SuppressWarnings("rawtypes")
	public CLASS<JsonWriter> z8_writeProperty(string name, RCollection value) {
		writer.writeProperty(name.get(), value);
		return (CLASS<JsonWriter>) getCLASS();
	}

	public CLASS<JsonWriter> z8_writeProperty(Field.CLASS<? extends Field> field) {
		writer.writeProperty(field.id(), field.get().get());
		return (CLASS<JsonWriter>) getCLASS();
	}

	@Override
	public string z8_toString() {
		return new string(writer.toString());
	}

}
