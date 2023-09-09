package com.chessmagister.utils;

import java.io.Serializable;

import com.chessmagister.gui.lesson.common.CMChessBoard.XCoordinates;
import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMSerializableData;
import com.chessmagister.io.CMSerializableEnumData;
import com.chessmagister.io.CMXmlConstants;
import com.chessmagister.io.CMXmlNode;

public class CMBoardConfigEntry implements CMSerializableComplex, Serializable
{
	public enum ConfigType implements CMSerializableEnumData
	{
		BOOL, STRING, NUMBER;

		@Override
		public String getID()
		{
			return CMXmlConstants.ANSWER_BOARD_CONFIG_TYPE_ID;
		}

		@Override
		public String getValue()
		{
			return this.name();
		}

		@Override
		public CMSerializableEnumData valueFromString(String value)
		{
			return ConfigType.valueOf(value);
		}
	}

	@CMXmlNode public CMStringNodeWrapper m_ID = new CMStringNodeWrapper(CMXmlConstants.ANSWER_BOARD_CONFIG_ID_ID);
	@CMXmlNode public ConfigType m_type = ConfigType.STRING;
	@CMXmlNode public CMStringNodeWrapper m_value = new CMStringNodeWrapper(CMXmlConstants.ANSWER_BOARD_CONFIG_VALUE_ID);

	public CMBoardConfigEntry()
	{
		m_ID.setValue("");
		m_value.setValue("");
	}
	
	public CMBoardConfigEntry(String id, ConfigType type, String value)
	{
		m_ID.setValue(id);
		m_type = type;
		m_value.setValue(value);
	}

	public Object getValue()
	{
		if(m_type == ConfigType.BOOL)
		{
			return Boolean.parseBoolean(m_value.getValue());
		}

		return m_value.getValue();
	}
	public void setValue(Object value)
	{
		if(m_type == ConfigType.BOOL)
		{
			m_value.setValue(value.toString());
		}
		else
		{
			m_value.setValue((String)value);
		}
	}
	
	@Override
	public void fireUpdatedEvent() throws Exception
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getID()
	{
		return CMXmlConstants.ANSWER_BOARD_CONFIG_ENTRY_ID;
	}

	@Override
	public void performSave() throws Exception
	{
	}
}
