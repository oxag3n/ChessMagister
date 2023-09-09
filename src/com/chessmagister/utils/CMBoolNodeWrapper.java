package com.chessmagister.utils;

import java.io.Serializable;

import com.chessmagister.io.CMSerializableTextData;

public class CMBoolNodeWrapper implements CMSerializableTextData, Serializable
{
	protected String m_id = null;
	protected boolean m_value = false;

	public CMBoolNodeWrapper(String id)
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
		m_value = Boolean.parseBoolean(value);
	}
	public void setValue(boolean value)
	{
		m_value = value;
	}

	@Override
	public String getValue()
	{
		return Boolean.toString(m_value);
	}
	public boolean getBoolValue()
	{
		return m_value;
	}
}
