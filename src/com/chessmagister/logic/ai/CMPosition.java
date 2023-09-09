package com.chessmagister.logic.ai;

import java.util.List;
import java.util.Vector;

public class CMPosition
{
	public static char PAWN = 1;
	public static char KNIGHT = 2;
	public static char KING = 4;
	public static char QUEEN = 8;
	public static char ROOK = 16;
	public static char BISHOP = 32;
	public static char WHITE = 64;
	public static char PAWN_INITIAL_POSITION = 128;
	public static char EMPTY = 256;

	public static int PAWN_PRICE = 100;
	public static int KNIGHT_PRICE = 300;
	public static int KING_PRICE = 6000;
	public static int QUEEN_PRICE = 1200;
	public static int ROOK_PRICE = 600;
	public static int BISHOP_PRICE = 300;
	public static int MAX_PRICE = 0xFFFF - 1;
	public static int MIN_PRICE = -0xFFFF + 1;
	
	public char m_cells[][] = new char[8][8];
	// Move that generated this position
	public CMMove m_move = null;

	public CMPosition()
	{
		for(int x = 0; x < 8; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				m_cells[x][y] = CMPosition.EMPTY;
			}
		}
	}
	
	public boolean isTerminal()
	{
		int kingCount = 0;
		for(int x = 0; x < 8; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				char cell = m_cells[x][y];

				if(is(cell, KING))
				{
					kingCount++;
				}
			}
		}
		return kingCount < 2;
	}

	protected int figureValue(char figure)
	{
		if(is(figure, PAWN))
		{
			return PAWN_PRICE;
		}
		if(is(figure, BISHOP))
		{
			return BISHOP_PRICE;
		}
		if(is(figure, QUEEN))
		{
			return QUEEN_PRICE;
		}
		if(is(figure, KING))
		{
			return KING_PRICE;
		}
		if(is(figure, ROOK))
		{
			return ROOK_PRICE;
		}
		if(is(figure, KNIGHT))
		{
			return KNIGHT_PRICE;
		}

		return 0;
	}
	
	public CMMove value(boolean white)
	{
		int value = 0;
		boolean whiteKing = false;
		boolean blackKing = false;
		for(int x = 0; x < 8; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				char cell = m_cells[x][y];

				if(	!is(cell, EMPTY))
				{
					if(is(cell, WHITE) == white)
					{
						value += figureValue(cell);
					}
					else
					{
						value -= figureValue(cell);
					}
				}

				if(is(cell, KING))
				{
					if(is(cell, WHITE))
					{
						whiteKing = true;
					}
					else
					{
						blackKing = true;
					}
				}
			}
		}

		if(white)
		{
			if(!whiteKing)
			{
				value = MIN_PRICE;
			}
			if(!blackKing)
			{
				value = MAX_PRICE;
			}
		}
		else
		{
			if(!blackKing)
			{
				value = MIN_PRICE;
			}
			if(!whiteKing)
			{
				value = MAX_PRICE;
			}
		}
		m_move.m_value = value;
		return m_move;
	}

	protected static boolean is(char cell, char value)
	{
		return (cell & value) == value;
	}
	
	protected void setFlag(char cell, char value)
	{
		cell |= value;
	}

	protected void unSetFlag(char cell, char value)
	{
		cell ^= value;
	}

	protected boolean isOpositeColor(char cell, boolean maxWhite, boolean maxPlayer)
	{
		return ((maxWhite == maxPlayer) != is(cell, WHITE));
	}
	
	private void calcPawnMoves(char cell, CMCoordinate coordinate, boolean maxWhite, boolean maxPlayer, List<CMMove> moves) throws Exception
	{
		// Create coordinates for move
		CMCoordinate oneMoveForward = coordinate.clone();
		CMCoordinate doubleMoveForward = coordinate.clone();
		CMCoordinate oneMoveForwardRight = coordinate.clone();
		CMCoordinate oneMoveForwardLeft = coordinate.clone();
		
		// Make moves of coordinates
		oneMoveForward.change(CMMove.FORWARD, 1, maxPlayer);
		doubleMoveForward.change(CMMove.FORWARD, 2, maxPlayer);
		oneMoveForwardRight.change((char)(CMMove.FORWARD | CMMove.RIGHT), 1, maxPlayer);
		oneMoveForwardLeft.change((char)(CMMove.FORWARD | CMMove.LEFT), 1, maxPlayer);

		char destinationCell = EMPTY;
		
		if(oneMoveForward.isValid())
		{
			// Valid coordinate
			destinationCell = m_cells[oneMoveForward.xCoordinateAsInt()][oneMoveForward.yCoordinateAsInt()];
			if(is(destinationCell, EMPTY))
			{
				// Empty cell, we can move here
				moves.add(new CMMove(coordinate.clone(), oneMoveForward.clone()));
			}
		}
		if(doubleMoveForward.isValid())
		{
			// Check if this is initial position
			if(is(cell, PAWN_INITIAL_POSITION))
			{
				// Valid coordinate and figure
				destinationCell = m_cells[oneMoveForward.xCoordinateAsInt()][oneMoveForward.yCoordinateAsInt()];
				if(is(destinationCell, EMPTY))
				{
					// Empty cell, we can move here
					moves.add(new CMMove(coordinate.clone(), oneMoveForward.clone()));
				}
			}
		}
		if(oneMoveForwardRight.isValid())
		{
			// Valid coordinate
			destinationCell = m_cells[oneMoveForwardRight.xCoordinateAsInt()][oneMoveForwardRight.yCoordinateAsInt()];
			if(	!is(destinationCell, EMPTY) &&
				isOpositeColor(destinationCell, maxWhite, maxPlayer))
			{
				// Cell with opponent figure, we can move here
				moves.add(new CMMove(coordinate.clone(), oneMoveForwardRight.clone()));
			}
		}
		if(oneMoveForwardLeft.isValid())
		{
			// Valid coordinate
			destinationCell = m_cells[oneMoveForwardLeft.xCoordinateAsInt()][oneMoveForwardLeft.yCoordinateAsInt()];
			if(	!is(destinationCell, EMPTY) &&
				isOpositeColor(destinationCell, maxWhite, maxPlayer))
			{
				// Cell with opponent figure, we can move here
				moves.add(new CMMove(coordinate.clone(), oneMoveForwardLeft.clone()));
			}
		}
	}
	
	private void calcKnightMoves(char cell, CMCoordinate coordinate, boolean maxWhite, boolean maxPlayer, List<CMMove> moves) throws Exception
	{
		// Create coordinates for move
		CMCoordinate move1oClock = coordinate.clone();
		CMCoordinate move2oClock = coordinate.clone();
		CMCoordinate move4oClock = coordinate.clone();
		CMCoordinate move5oClock = coordinate.clone();
		CMCoordinate move7oClock = coordinate.clone();
		CMCoordinate move8oClock = coordinate.clone();
		CMCoordinate move10oClock = coordinate.clone();
		CMCoordinate move11oClock = coordinate.clone();
		
		// Make moves of coordinates
		move1oClock.change(CMMove.FORWARD, 2, maxPlayer);
		move1oClock.change(CMMove.RIGHT, 1, maxPlayer);
		//--
		move2oClock.change(CMMove.FORWARD, 1, maxPlayer);
		move2oClock.change(CMMove.RIGHT, 2, maxPlayer);
		//--
		move4oClock.change(CMMove.BACK, 1, maxPlayer);
		move4oClock.change(CMMove.RIGHT, 2, maxPlayer);
		//--
		move5oClock.change(CMMove.BACK, 2, maxPlayer);
		move5oClock.change(CMMove.RIGHT, 1, maxPlayer);
		//--
		move7oClock.change(CMMove.BACK, 2, maxPlayer);
		move7oClock.change(CMMove.LEFT, 1, maxPlayer);
		//--
		move8oClock.change(CMMove.BACK, 1, maxPlayer);
		move8oClock.change(CMMove.LEFT, 2, maxPlayer);
		//--
		move10oClock.change(CMMove.FORWARD, 1, maxPlayer);
		move10oClock.change(CMMove.LEFT, 2, maxPlayer);
		//--
		move11oClock.change(CMMove.FORWARD, 2, maxPlayer);
		move11oClock.change(CMMove.LEFT, 1, maxPlayer);
		
		CMCoordinate destinationMoves[] = {	move1oClock,
											move2oClock,
											move4oClock,
											move5oClock,
											move7oClock,
											move8oClock,
											move10oClock,
											move11oClock};

		char destinationCell = EMPTY;

		for(CMCoordinate destination : destinationMoves)
		{
			if(destination.isValid())
			{
				// Valid coordinate
				destinationCell = m_cells[destination.xCoordinateAsInt()][destination.yCoordinateAsInt()];
				if(	is(destinationCell, EMPTY) ||
					isOpositeColor(destinationCell, maxWhite, maxPlayer))
				{
					// Empty cell or with opponent figure, we can move here
					moves.add(new CMMove(coordinate.clone(), destination.clone()));
				}
			}
		}
	}
	
	private void calcQueenMoves(char cell, CMCoordinate coordinate, boolean maxWhite, boolean maxPlayer, List<CMMove> moves) throws Exception
	{
		// Create coordinates for move
		CMCoordinate moveForward = coordinate.clone();
		CMCoordinate moveForwardRight = coordinate.clone();
		CMCoordinate moveForwardLeft = coordinate.clone();
		CMCoordinate moveRight = coordinate.clone();
		CMCoordinate moveLeft = coordinate.clone();
		CMCoordinate moveBack = coordinate.clone();
		CMCoordinate moveBackRight = coordinate.clone();
		CMCoordinate moveBackLeft = coordinate.clone();

		CMCoordinate destinationCoordinates[] = {	moveForward,
													moveForwardRight,
													moveForwardLeft,
													moveRight,
													moveLeft,
													moveBack,
													moveBackRight,
													moveBackLeft};

		char moveTypes[] = {CMMove.FORWARD,
							CMMove.FORWARD | CMMove.RIGHT,
							CMMove.FORWARD | CMMove.LEFT,
							CMMove.RIGHT,
							CMMove.LEFT,
							CMMove.BACK,
							CMMove.BACK | CMMove.RIGHT,
							CMMove.BACK | CMMove.LEFT};

		char destinationCell = EMPTY;

		for(int moveTypeIndex = 0; moveTypeIndex < moveTypes.length; moveTypeIndex++)
		{
			CMCoordinate destination = destinationCoordinates[moveTypeIndex];
			char type = moveTypes[moveTypeIndex];

			for(int step = 1; step < 8; step++)
			{
				destination.change(type, 1, maxPlayer);
				if(destination.isValid())
				{
					// Valid coordinate
					destinationCell = m_cells[destination.xCoordinateAsInt()][destination.yCoordinateAsInt()];
					if(	!is(destinationCell, EMPTY))
					{
						if(isOpositeColor(destinationCell, maxWhite, maxPlayer))
						{
							// Empty cell or with opponent figure, we can move here
							moves.add(new CMMove(coordinate.clone(), destination.clone()));
						}
						break;
					}
					else
					{
						moves.add(new CMMove(coordinate.clone(), destination.clone()));
					}
				}
				else {break;}
			}
		}
	}
	
	private void calcKingMoves(char cell, CMCoordinate coordinate, boolean maxWhite, boolean maxPlayer, List<CMMove> moves) throws Exception
	{
		// Create coordinates for move
		CMCoordinate moveForward = coordinate.clone();
		CMCoordinate moveForwardRight = coordinate.clone();
		CMCoordinate moveForwardLeft = coordinate.clone();
		CMCoordinate moveRight = coordinate.clone();
		CMCoordinate moveLeft = coordinate.clone();
		CMCoordinate moveBack = coordinate.clone();
		CMCoordinate moveBackRight = coordinate.clone();
		CMCoordinate moveBackLeft = coordinate.clone();

		CMCoordinate destinationCoordinates[] = {	moveForward,
													moveForwardRight,
													moveForwardLeft,
													moveRight,
													moveLeft,
													moveBack,
													moveBackRight,
													moveBackLeft};

		char moveTypes[] = {CMMove.FORWARD,
							CMMove.FORWARD | CMMove.RIGHT,
							CMMove.FORWARD | CMMove.LEFT,
							CMMove.RIGHT,
							CMMove.LEFT,
							CMMove.BACK,
							CMMove.BACK | CMMove.RIGHT,
							CMMove.BACK | CMMove.LEFT};

		char destinationCell = EMPTY;

		for(int moveTypeIndex = 0; moveTypeIndex < moveTypes.length; moveTypeIndex++)
		{
			CMCoordinate destination = destinationCoordinates[moveTypeIndex];
			char type = moveTypes[moveTypeIndex];

			destination.change(type, 1, maxPlayer);
			if(destination.isValid())
			{
				// Valid coordinate
				destinationCell = m_cells[destination.xCoordinateAsInt()][destination.yCoordinateAsInt()];
				if(	is(destinationCell, EMPTY) ||
					isOpositeColor(destinationCell, maxWhite, maxPlayer))
				{
					// Empty cell or with opponent figure, we can move here
					moves.add(new CMMove(coordinate.clone(), destination.clone()));
				}
				else {break;}
			}
			else {break;}
		}
	}

	private void calcBishopMoves(char cell, CMCoordinate coordinate, boolean maxWhite, boolean maxPlayer, List<CMMove> moves) throws Exception
	{
		// Create coordinates for move
		CMCoordinate moveForwardRight = coordinate.clone();
		CMCoordinate moveForwardLeft = coordinate.clone();
		CMCoordinate moveBackRight = coordinate.clone();
		CMCoordinate moveBackLeft = coordinate.clone();

		CMCoordinate destinationCoordinates[] = {	moveForwardRight,
													moveForwardLeft,
													moveBackRight,
													moveBackLeft};

		char moveTypes[] = {CMMove.FORWARD | CMMove.RIGHT,
							CMMove.FORWARD | CMMove.LEFT,
							CMMove.BACK | CMMove.RIGHT,
							CMMove.BACK | CMMove.LEFT};

		char destinationCell = EMPTY;

		for(int moveTypeIndex = 0; moveTypeIndex < moveTypes.length; moveTypeIndex++)
		{
			CMCoordinate destination = destinationCoordinates[moveTypeIndex];
			char type = moveTypes[moveTypeIndex];

			for(int step = 1; step < 8; step++)
			{
				destination.change(type, 1, maxPlayer);
				if(destination.isValid())
				{
					// Valid coordinate
					destinationCell = m_cells[destination.xCoordinateAsInt()][destination.yCoordinateAsInt()];
					if(	!is(destinationCell, EMPTY))
					{
						if(isOpositeColor(destinationCell, maxWhite, maxPlayer))
						{
							// Empty cell or with opponent figure, we can move here
							moves.add(new CMMove(coordinate.clone(), destination.clone()));
						}
						break;
					}
					else
					{
						moves.add(new CMMove(coordinate.clone(), destination.clone()));
					}
				}
				else {break;}
			}
		}
	}
	
	private void calcRookMoves(char cell, CMCoordinate coordinate, boolean maxWhite, boolean maxPlayer, List<CMMove> moves) throws Exception
	{
		// Create coordinates for move
		CMCoordinate moveForward = coordinate.clone();
		CMCoordinate moveRight = coordinate.clone();
		CMCoordinate moveLeft = coordinate.clone();
		CMCoordinate moveBack = coordinate.clone();

		CMCoordinate destinationCoordinates[] = {	moveForward,
													moveRight,
													moveLeft,
													moveBack};

		char moveTypes[] = {CMMove.FORWARD,
							CMMove.RIGHT,
							CMMove.LEFT,
							CMMove.BACK};

		char destinationCell = EMPTY;

		for(int moveTypeIndex = 0; moveTypeIndex < moveTypes.length; moveTypeIndex++)
		{
			CMCoordinate destination = destinationCoordinates[moveTypeIndex];
			char type = moveTypes[moveTypeIndex];

			for(int step = 1; step < 8; step++)
			{
				destination.change(type, 1, maxPlayer);
				if(destination.isValid())
				{
					// Valid coordinate
					destinationCell = m_cells[destination.xCoordinateAsInt()][destination.yCoordinateAsInt()];
					if(	!is(destinationCell, EMPTY))
					{
						if(isOpositeColor(destinationCell, maxWhite, maxPlayer))
						{
							// Empty cell or with opponent figure, we can move here
							moves.add(new CMMove(coordinate.clone(), destination.clone()));
						}
						break;
					}
					else
					{
						moves.add(new CMMove(coordinate.clone(), destination.clone()));
					}
				}
				else {break;}
			}
		}
	}
	
	public List<CMPosition> getChildren(boolean maxWhite, boolean maxPlayer) throws Exception
	{
		List<CMPosition> children = new Vector<CMPosition>();
		List<CMMove> moves = new Vector<CMMove>();
		
		CMCoordinate coordinate = new CMCoordinate(0, 0);
		
		for(; coordinate.isValid(); coordinate.increaseX())
		{
			for(coordinate.setAbsoluteCoordinates(coordinate.xCoordinateAsInt(), 0);
				coordinate.isValid();
				coordinate.increaseY())
			{
				char cell = m_cells[coordinate.xCoordinateAsInt()][coordinate.yCoordinateAsInt()];

				if(	is(cell, EMPTY) ||
					((maxWhite == maxPlayer) != is(cell, WHITE)))
				{
					// empty or other color
					continue;
				}

				if(is(cell, PAWN))
				{
					calcPawnMoves(cell, coordinate, maxWhite, maxPlayer, moves);
				}
				else if(is(cell, QUEEN))
				{
					calcQueenMoves(cell, coordinate, maxWhite, maxPlayer, moves);
				}
				else if(is(cell, KING))
				{
					calcKingMoves(cell, coordinate, maxWhite, maxPlayer, moves);
				}
				else if(is(cell, BISHOP))
				{
					calcBishopMoves(cell, coordinate, maxWhite, maxPlayer, moves);
				}
				else if(is(cell, KNIGHT))
				{
					calcKnightMoves(cell, coordinate, maxWhite, maxPlayer, moves);
				}
				else if(is(cell, ROOK))
				{
					calcRookMoves(cell, coordinate, maxWhite, maxPlayer, moves);
				}
			}
			
			coordinate.makeValid();
		}
		
		for(CMMove move : moves)
		{
			CMPosition newPosition = new CMPosition();
			newPosition.m_cells = this.m_cells.clone();
			for(int i = 0; i < 8; i++)
			{
				newPosition.m_cells[i] = this.m_cells[i].clone();
			}
			newPosition.m_move = move;

			// Get move start and end positions
			CMCoordinate start = move.getStartCoordinate();
			CMCoordinate end = move.getEndCoordinate();

			// Extract figure from old position
			char figure = newPosition.m_cells[start.xCoordinateAsInt()][start.yCoordinateAsInt()];
			newPosition.m_cells[start.xCoordinateAsInt()][start.yCoordinateAsInt()] = EMPTY;

			// Check if pawn in initial posistion and remove the flag
			if(is(figure, PAWN) && is(figure, PAWN_INITIAL_POSITION))
			{
				unSetFlag(figure, PAWN_INITIAL_POSITION);
			}

			// Assign figure to destination
			newPosition.m_cells[end.xCoordinateAsInt()][end.yCoordinateAsInt()] = figure;

			children.add(newPosition);
		}
		
		return children;
	}
}
