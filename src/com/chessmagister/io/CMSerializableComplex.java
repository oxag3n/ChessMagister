package com.chessmagister.io;

/**
 * Interface makes classes to have serializable part and be built from Serializable
 * 
 * @author Oxana C.
 */
public interface CMSerializableComplex
{
	public String getID();
	public void fireUpdatedEvent() throws Exception;
	public void performSave() throws Exception;
}
