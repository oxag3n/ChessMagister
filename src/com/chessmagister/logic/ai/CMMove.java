package com.chessmagister.logic.ai;

public class CMMove
{
	public static final char FORWARD	= 1; // 0001
	public static final char BACK		= 2; // 0010
	public static final char RIGHT		= 4; // 0100
	public static final char LEFT		= 8; // 1000
	
	public CMCoordinate m_start;
	public CMCoordinate m_end;

	public int m_value = 0;
	
	public CMCoordinate getStartCoordinate()
	{
		return m_start;
	}

	public CMCoordinate getEndCoordinate()
	{
		return m_end;
	}

	public CMMove(CMCoordinate start, CMCoordinate end)
	{
		m_start = start;
		m_end = end;
	}
	
	public void assignValue(CMMove another)
	{
		this.m_value = another.m_value;
	}
	
	public void assign(CMMove another)
	{
		this.m_start = another.m_start;
		this.m_end = another.m_end;
		this.m_value = another.m_value;
	}
	
	public void assignCoordinates(CMMove another)
	{
		this.m_start = another.m_start;
		this.m_end = another.m_end;
	}
}
