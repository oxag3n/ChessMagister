package com.chessmagister.utils;

import java.io.Serializable;

import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMSerializableData;
import com.chessmagister.io.CMXmlConstants;
import com.chessmagister.io.CMXmlNode;

public class CMAnswer implements CMSerializableComplex, Serializable
{
	@CMXmlNode public CMStringNodeWrapper m_answer = new CMStringNodeWrapper(CMXmlConstants.ANSWER_ID);
	@CMXmlNode public CMBoolNodeWrapper m_correct = new CMBoolNodeWrapper(CMXmlConstants.ANSWER_IS_CORRECT_ID);

	public CMAnswer()
	{
		m_answer.setValue("");
		m_correct.setValue(false);
	}
	
	public CMAnswer(String answer, boolean correct)
	{
		m_answer.setValue(answer);
		m_correct.setValue(correct);
	}

	@Override
	public void fireUpdatedEvent() throws Exception
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getID()
	{
		return CMXmlConstants.ANSWER_TABLE_PAIR_ID;
	}

	@Override
	public void performSave() throws Exception
	{
	}
}
