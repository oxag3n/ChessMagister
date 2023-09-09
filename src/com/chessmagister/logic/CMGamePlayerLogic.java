package com.chessmagister.logic;

import com.chessmagister.events.CMBoardListener;
import com.chessmagister.gui.lesson.common.CMChessBoard;
import com.chessmagister.gui.lesson.common.CMChessBoard.XCoordinates;
import com.chessmagister.gui.lesson.common.CMChessBoard.YCoordinates;
import com.chessmagister.logic.CMFigure.Color;
import com.chessmagister.logic.ai.CMAlphaBeta;
import com.chessmagister.logic.ai.CMCoordinate;
import com.chessmagister.logic.ai.CMMove;
import com.chessmagister.logic.ai.CMPosition;

public class CMGamePlayerLogic implements CMBoardListener
{
	protected CMFigure m_selectedFigure = null;
	public CMChessBoard m_chessBoard = null;
	
	public CMGamePlayerLogic(CMChessBoard board)
	{
		m_chessBoard = board;
	}

	@Override
	public void onCellClick(BoardCoordinates coordinates)
	{
		if(m_selectedFigure != null)
		{
			m_chessBoard.removeFigure(m_chessBoard.getSelectedCoordinates());
			m_chessBoard.putFigure(m_selectedFigure, coordinates);
			m_selectedFigure = null;

			CMPosition position = new CMPosition();
			
			for(Object key : m_chessBoard.m_boardMatrix.keySet())
			{
				BoardCoordinates xy = (BoardCoordinates)key;
				CMFigure figure = (CMFigure)m_chessBoard.m_boardMatrix.get(key);
				char cellFigure = CMPosition.EMPTY;
				
				if(figure instanceof CMPawn)
				{
					cellFigure = CMPosition.PAWN;
				}
				else if(figure instanceof CMRook)
				{
					cellFigure = CMPosition.ROOK;
				}
				else if(figure instanceof CMQueen)
				{
					cellFigure = CMPosition.QUEEN;
				}
				else if(figure instanceof CMKing)
				{
					cellFigure = CMPosition.KING;
				}
				else if(figure instanceof CMKnight)
				{
					cellFigure = CMPosition.KNIGHT;
				}
				else if(figure instanceof CMBishop)
				{
					cellFigure = CMPosition.BISHOP;
				}
				
				if(figure.m_color == Color.WHITE)
				{
					cellFigure |= CMPosition.WHITE;
				}
				
				position.m_cells[xy.x.ordinal()][xy.y.ordinal()] = cellFigure;
			}
			
			CMMove alpha = new CMMove(null, null);
			CMMove beta = new CMMove(null, null);
			alpha.m_value = -1000000;
			beta.m_value = 1000000;
			CMMove bestMove = null;
			
			try
			{
				CMPosition bestPosition = new CMPosition();
//				CMChessInteligence.alphabeta(position, bestPosition, 4, alpha, beta, false, true);
				CMAlphaBeta.alphaBeta(	position, bestPosition, 4, 0, -CMAlphaBeta.INFINITY,
										CMAlphaBeta.INFINITY, false, true);
				if(bestPosition != null)
				{
					bestMove = bestPosition.m_move;
					CMCoordinate start = bestMove.m_start;
					CMCoordinate end = bestMove.m_end;

					int startX = start.xCoordinateAsInt();
					int startY = start.yCoordinateAsInt();
					
					int endX = end.xCoordinateAsInt();
					int endY = end.yCoordinateAsInt();
					
					BoardCoordinates startCoordinates = new BoardCoordinates(	XCoordinates.valueFromInt(startX),
																				YCoordinates.valueFromInt(startY));
					BoardCoordinates endCoordinates = new BoardCoordinates(	XCoordinates.valueFromInt(endX),
																			YCoordinates.valueFromInt(endY));
					
					CMFigure movingFigure = m_chessBoard.getFigure(startCoordinates);
					
					if(movingFigure == null)
					{
						/*System.out.println("Start[" + bestMove.m_start.xCoordinateAsInt() +  "]" +
						"[" + bestMove.m_start.yCoordinateAsInt() +  "]" +
						"\tTarget[" + bestMove.m_end.xCoordinateAsInt() +  "]" +
						"[" + bestMove.m_end.yCoordinateAsInt() +  "]");*/
						throw new Exception("moving null figure");
					}
					m_chessBoard.removeFigure(startCoordinates);
					m_chessBoard.putFigure(movingFigure, endCoordinates);
				}
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			m_selectedFigure = m_chessBoard.getFigure(coordinates);
		}
		
		m_chessBoard.select(coordinates);
	}
}
