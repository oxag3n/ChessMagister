package com.chessmagister.logic;

import java.awt.Image;
import java.io.IOException;
import java.io.Serializable;

import com.chessmagister.gui.lesson.common.CMChessBoard.XCoordinates;
import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMSerializableEnumData;
import com.chessmagister.io.CMSerializableTextData;
import com.chessmagister.io.CMXmlConstants;
import com.chessmagister.io.CMXmlNode;

public abstract class CMFigure implements Serializable, CMSerializableComplex
{
	private static final long serialVersionUID = 2573446940594962966L;
	@CMXmlNode public CMFigure.Color m_color;

	public CMFigure()
	{
		m_color = Color.WHITE;
	}
	
	public enum Type implements CMSerializableEnumData
	{
		PAWN, BISHOP, KNIGHT, QUEEN, KING, ROOK;
		
		@Override
		public String getID()
		{
			return CMXmlConstants.FIGURE_TYPE_ID;
		}

		@Override
		public String getValue()
		{
			return this.name();
		}
		
		@Override
		public CMSerializableEnumData valueFromString(String value)
		{
			return Type.valueOf(value);
		}
	}

	public enum Color implements CMSerializableEnumData
	{
		WHITE, BLACK, GRAY;
		
		@Override
		public String getID()
		{
			return CMXmlConstants.FIGURE_COLOR_ID;
		}

		@Override
		public String getValue()
		{
			return this.name();
		}
		
		@Override
		public CMSerializableEnumData valueFromString(String value)
		{
			return Color.valueOf(value);
		}
	}

	public abstract Image getImage() throws IOException;
	
	@Override
	public String getID()
	{
		return CMXmlConstants.FIGURE_ID;
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
