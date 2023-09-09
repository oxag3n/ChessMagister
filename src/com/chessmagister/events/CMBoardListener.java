package com.chessmagister.events;

import com.chessmagister.logic.BoardCoordinates;

public interface CMBoardListener
{
	public void onCellClick(BoardCoordinates coordinates);
}
