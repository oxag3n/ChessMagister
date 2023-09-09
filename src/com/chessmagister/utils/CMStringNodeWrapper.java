package com.chessmagister.utils;

import java.io.Serializable;

import com.chessmagister.io.CMSerializableTextData;

public class CMStringNodeWrapper implements CMSerializableTextData, Serializable
{
	protected String m_id = null;
	protected String m_value = new String();

	public CMStringNodeWrapper(String id)
	{
		m_id = id;
	}

	@Override
	public String getID()
	{
		return m_id;
	}

	@Override
	public void setValue(String value)
	{
		m_value = value;
	}

	@Override
	public String getValue()
	{
		return m_value;
	}
}
