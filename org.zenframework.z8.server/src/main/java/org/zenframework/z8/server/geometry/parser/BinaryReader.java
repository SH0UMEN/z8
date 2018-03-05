package org.zenframework.z8.server.geometry.parser;

import java.util.ArrayList;
import java.util.Collection;

import org.zenframework.z8.server.types.geometry;

public class BinaryReader {
	private String bytes;
	private int position;

	static public geometry read(String bytes) {
		return new BinaryReader(bytes).read();
	}

	private BinaryReader(String bytes) {
		this.bytes = bytes;
	}

	private geometry read() {
		return readGeometry(bytes);
	}

	private geometry readGeometry(String bytes) {
		int endianType = getByte();

		if(endianType != 1)
			throw new IllegalArgumentException("Unknown Endian type: " + endianType);

		int type = getInt();

		boolean hasAltitude = (type & 0x80000000) != 0;
		boolean hasMeasure = (type & 0x40000000) != 0;

		if(hasAltitude || hasMeasure)
			throw new IllegalArgumentException("Altitute and measure are unsupported.");

		boolean hasSRS = (type & 0x20000000) != 0;

		int srs = hasSRS ? Math.max(getInt(), 0) : 0;

		switch(type & 0x1FFFFFFF) {
		case geometry.Point:
			return readPoint(srs, bytes);
		case geometry.Line:
			return readLine(srs, bytes);
		case geometry.Polygon:
			return readPolygon(srs, bytes);
		case geometry.MultiPoint:
			return readMultiPoint(srs, bytes);
		case geometry.MultiLine:
			return readMultiLine(srs, bytes);
		case geometry.MultiPolygon:
			return readMultiPolygon(srs, bytes);
		case geometry.Collection:
			return readCollection(srs, bytes);
		default:
			throw new IllegalArgumentException("Unknown Geometry Type: " + type);
		}
	}

	private int getByte() {
		int index = position * 2;
		int high = unhex(bytes.charAt(index));
		int low = unhex(bytes.charAt(index + 1));
		position++;
		return (high << 4) + low;
	}

	public byte unhex(char c) {
		if(c >= '0' && c <= '9')
			return (byte) (c - '0');
		else if(c >= 'A' && c <= 'F')
			return (byte) (c - 'A' + 10);
		else if(c >= 'a' && c <= 'f')
			return (byte) (c - 'a' + 10);
		else
			throw new IllegalArgumentException("No valid Hex char " + c);
	}

	private int getInt() {
		return getByte() + (getByte() << 8) + (getByte() << 16) + (getByte() << 24);
	}

	private long getLong() {
		return (long)getByte() + ((long)getByte() << 8) + ((long)getByte() << 16) + ((long)getByte() << 24) +
				((long)getByte() << 32) + ((long)getByte() << 40) + ((long)getByte() << 48) + ((long)getByte() << 56);
	}

	private double getDouble() {
		return Double.longBitsToDouble(getLong());
	}

	private Collection<geometry> readPoints(int srs) {
		int count = getInt();
		Collection<geometry> points = new ArrayList<geometry>(count);

		for(int i = 0; i < count; i++)
			points.add(readPoint(srs, null));

		return points;
	}

	private Collection<geometry> readGeometry(int count) {
		Collection<geometry> geometries = new ArrayList<geometry>();

		for(int i = 0; i < count; i++)
			geometries.add(readGeometry(null));

		return geometries;
	}

	private geometry readPoint(int srs, String bytes) {
		return new geometry(getDouble(), getDouble(), srs, bytes);
	}

	private geometry readMultiPoint(int srs, String bytes) {
		return new geometry(readGeometry(getInt()), geometry.MultiPoint, srs, bytes);
	}

	private geometry readLine(int srs, String bytes) {
		return new geometry(readPoints(srs), geometry.Line, srs, bytes);
	}

	private geometry readMultiLine(int srs, String bytes) {
		return new geometry(readGeometry(getInt()), geometry.MultiLine, srs, bytes);
	}

	private geometry readRing(int srs) {
		return new geometry(readPoints(srs), geometry.Ring, srs, null);
	}

	private geometry readPolygon(int srs, String bytes) {
		int ringCount = getInt();
		Collection<geometry> rings = new ArrayList<geometry>();
		for(int i = 0; i < ringCount; i++)
			rings.add(readRing(srs));
		return new geometry(rings, geometry.Polygon, srs, bytes);
	}

	private geometry readMultiPolygon(int srs, String bytes) {
		return new geometry(readGeometry(getInt()), geometry.MultiPolygon, srs, bytes);
	}

	private geometry readCollection(int srs, String bytes) {
		return new geometry(readGeometry(getInt()), geometry.Collection, srs, bytes);
	}
}
