package com.chessmagister.logic;

public class CMFigureFactory
{
	public static CMFigure createFigure(CMFigure.Type type, CMFigure.Color color)
	{
		switch(type)
		{
		case BISHOP:
			return new CMBishop(color);			
		case PAWN:
			return new CMPawn(color);
		case KING:
			return new CMKing(color);
		case KNIGHT:
			return new CMKnight(color);
		case ROOK:
			return new CMRook(color);
		case QUEEN:
			return new CMQueen(color);
		default:
			return null;
		}
	}
}
