package com.chessmagister.logic;

import java.io.Serializable;

import com.chessmagister.gui.lesson.common.CMChessBoard.XCoordinates;
import com.chessmagister.gui.lesson.common.CMChessBoard.YCoordinates;
import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMXmlConstants;
import com.chessmagister.io.CMXmlNode;

public class BoardCoordinates implements Serializable, CMSerializableComplex
{
	@CMXmlNode public XCoordinates x;
	@CMXmlNode public YCoordinates y;

	private static final long serialVersionUID = -703655760598129352L;
	
	public BoardCoordinates()
	{
		this.x = XCoordinates.A;
		this.y = YCoordinates.Y1;
	}
	
	public BoardCoordinates(XCoordinates x, YCoordinates y)
	{
		this.x = x;
		this.y = y;
	}

	public boolean equals(Object second)
	{
		return (x == ((BoardCoordinates)second).x)&&(y == ((BoardCoordinates)second).y);
	}
	
	public int hashCode()
	{
		return Integer.parseInt(Integer.toString(x.ordinal()) + Integer.toString(y.ordinal()));
	}

	@Override
	public String getID()
	{
		return CMXmlConstants.BOARD_COORDINATES_ID;
	}

	@Override
	public void fireUpdatedEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void performSave() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
