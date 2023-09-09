package com.chessmagister.utils;

import com.chessmagister.io.CMSerializableComplex;

public abstract class CMComplexNodeReader implements CMSerializableComplex
{
	protected String m_id = null;

	public CMComplexNodeReader(String id)
	{
		m_id = id;
	}

	@Override
	public void fireUpdatedEvent() throws Exception
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getID()
	{
		return m_id;
	}

	@Override
	public void performSave() throws Exception
	{}
	
}
