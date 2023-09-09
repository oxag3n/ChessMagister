package com.chessmagister.logic.ai;

public class CMCoordinate implements Cloneable
{
	public static final char A = 0;
	public static final char B = 1;
	public static final char C = 2;
	public static final char D = 3;
	public static final char E = 4;
	public static final char F = 5;
	public static final char G = 6;
	public static final char H = 7;

	public static final char Y1 = 0;
	public static final char Y2 = 8;
	public static final char Y3 = 16;
	public static final char Y4 = 24;
	public static final char Y5 = 32;
	public static final char Y6 = 40;
	public static final char Y7 = 48;
	public static final char Y8 = 56;

	public static final char INVALID = 64;


	protected char m_coordinate = 0;

	public CMCoordinate clone()
	{
		return new CMCoordinate(m_coordinate);
	}
	
	protected CMCoordinate(char coordinate)
	{
		m_coordinate = coordinate;
	}
	
	public CMCoordinate(int x, int y)
	{
		setAbsoluteCoordinates(x, y);
	}

	protected boolean is(char item, char value)
	{
		return (item & value) == value;
	}

	public void increaseX()
	{
		setAbsoluteCoordinates(	xCoordinateAsInt() + 1,
								yCoordinateAsInt());
	}

	public void increaseY()
	{
		setAbsoluteCoordinates(	xCoordinateAsInt(),
								yCoordinateAsInt() + 1);
	}

	public int xCoordinateAsInt()
	{
		return (char)((char)(m_coordinate << 13) >> 13);
	}
	
	public int yCoordinateAsInt()
	{
		return (char)((char)((char)(m_coordinate << 10) >> 13) << 3) / 8;
	}
	
	public void setAbsoluteCoordinates(int x, int y)
	{
		if (x >= 0 && x < 8 && y >= 0 && y < 8)
		{
			m_coordinate = (char) x;
			m_coordinate |= (char) (y * 8);
		} else
		{
			m_coordinate |= INVALID;
		}
	}
	
	public boolean isValid()
	{
		return !is(m_coordinate, INVALID);
	}
	
	public void makeValid()
	{
		m_coordinate ^= INVALID;
	}
	
	public void change(char moveType, int steps, boolean maxPlayer)
			throws Exception
	{
		if(!isValid())
		{
			return;
		}

		int moveSign = maxPlayer ? -1 : 1;

		int x = xCoordinateAsInt();
		int y = yCoordinateAsInt();

		if (is(moveType, CMMove.FORWARD))
		{
			y = y + moveSign * steps;
		}
		if (is(moveType, CMMove.BACK))
		{
			y = y - moveSign * steps;
		}
		if (is(moveType, CMMove.RIGHT))
		{
			x = x + moveSign * steps;
		}
		if (is(moveType, CMMove.LEFT))
		{
			x = x - moveSign * steps;
		}

		setAbsoluteCoordinates(x, y);
	}
}
