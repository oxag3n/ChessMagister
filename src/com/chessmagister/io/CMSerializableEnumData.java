package com.chessmagister.io;

/**
 * Interface makes classes to have serializable part and be built from Serializable
 * 
 * @author Oxana C.
 */
public interface CMSerializableEnumData extends CMSerializableData
{
	public CMSerializableEnumData valueFromString(String value);
}
