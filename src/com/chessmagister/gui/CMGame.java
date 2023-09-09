package com.chessmagister.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.chessmagister.gui.lesson.common.CMChessBoard;
import com.chessmagister.gui.lesson.common.CMChessBoard.XCoordinates;
import com.chessmagister.gui.lesson.common.CMChessBoard.YCoordinates;
import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMXmlConstants;
import com.chessmagister.io.CMXmlComplexNode;
import com.chessmagister.io.CMXmlIo;
import com.chessmagister.logic.BoardCoordinates;
import com.chessmagister.logic.CMFigureFactory;
import com.chessmagister.logic.CMGamePlayerLogic;
import com.chessmagister.logic.CMFigure.Color;
import com.chessmagister.logic.CMFigure.Type;
import com.chessmagister.logic.ai.CMCoordinate;
import com.sun.org.apache.xerces.internal.impl.dtd.models.CMBinOp;

public class CMGame extends JInternalFrame
{
	private static final long serialVersionUID = 1L;

	protected CMChessBoard m_chessBoard = null;

	protected void init() throws Exception
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		c.weightx = 0.5;
		c.weighty = 1.00;
		c.gridx = 0;
		c.gridy = 0;
		this.add(m_chessBoard = new CMChessBoard(), c);
		
		m_chessBoard.setBoardListener(new CMGamePlayerLogic(m_chessBoard));

		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.PAWN, Color.WHITE),
				new BoardCoordinates(XCoordinates.A, YCoordinates.Y2));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.PAWN, Color.WHITE),
				new BoardCoordinates(XCoordinates.B, YCoordinates.Y2));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.PAWN, Color.WHITE),
				new BoardCoordinates(XCoordinates.C, YCoordinates.Y2));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.PAWN, Color.WHITE),
				new BoardCoordinates(XCoordinates.D, YCoordinates.Y2));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.PAWN, Color.WHITE),
				new BoardCoordinates(XCoordinates.E, YCoordinates.Y2));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.PAWN, Color.WHITE),
				new BoardCoordinates(XCoordinates.F, YCoordinates.Y2));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.PAWN, Color.WHITE),
				new BoardCoordinates(XCoordinates.G, YCoordinates.Y2));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.PAWN, Color.WHITE),
				new BoardCoordinates(XCoordinates.H, YCoordinates.Y2));
		
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.ROOK, Color.WHITE),
				new BoardCoordinates(XCoordinates.A, YCoordinates.Y1));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.KNIGHT, Color.WHITE),
				new BoardCoordinates(XCoordinates.B, YCoordinates.Y1));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.BISHOP, Color.WHITE),
				new BoardCoordinates(XCoordinates.C, YCoordinates.Y1));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.QUEEN, Color.WHITE),
				new BoardCoordinates(XCoordinates.D, YCoordinates.Y1));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.KING, Color.WHITE),
				new BoardCoordinates(XCoordinates.E, YCoordinates.Y1));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.BISHOP, Color.WHITE),
				new BoardCoordinates(XCoordinates.F, YCoordinates.Y1));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.KNIGHT, Color.WHITE),
				new BoardCoordinates(XCoordinates.G, YCoordinates.Y1));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.ROOK, Color.WHITE),
				new BoardCoordinates(XCoordinates.H, YCoordinates.Y1));
		
		
		
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.PAWN, Color.BLACK),
				new BoardCoordinates(XCoordinates.A, YCoordinates.Y7));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.PAWN, Color.BLACK),
				new BoardCoordinates(XCoordinates.B, YCoordinates.Y7));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.PAWN, Color.BLACK),
				new BoardCoordinates(XCoordinates.C, YCoordinates.Y7));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.PAWN, Color.BLACK),
				new BoardCoordinates(XCoordinates.D, YCoordinates.Y7));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.PAWN, Color.BLACK),
				new BoardCoordinates(XCoordinates.E, YCoordinates.Y7));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.PAWN, Color.BLACK),
				new BoardCoordinates(XCoordinates.F, YCoordinates.Y7));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.PAWN, Color.BLACK),
				new BoardCoordinates(XCoordinates.G, YCoordinates.Y7));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.PAWN, Color.BLACK),
				new BoardCoordinates(XCoordinates.H, YCoordinates.Y7));
		
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.ROOK, Color.BLACK),
				new BoardCoordinates(XCoordinates.A, YCoordinates.Y8));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.KNIGHT, Color.BLACK),
				new BoardCoordinates(XCoordinates.B, YCoordinates.Y8));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.BISHOP, Color.BLACK),
				new BoardCoordinates(XCoordinates.C, YCoordinates.Y8));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.QUEEN, Color.BLACK),
				new BoardCoordinates(XCoordinates.D, YCoordinates.Y8));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.KING, Color.BLACK),
				new BoardCoordinates(XCoordinates.E, YCoordinates.Y8));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.BISHOP, Color.BLACK),
				new BoardCoordinates(XCoordinates.F, YCoordinates.Y8));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.KNIGHT, Color.BLACK),
				new BoardCoordinates(XCoordinates.G, YCoordinates.Y8));
		m_chessBoard.putFigure(CMFigureFactory.createFigure(Type.ROOK, Color.BLACK),
				new BoardCoordinates(XCoordinates.H, YCoordinates.Y8));
	}
	
	public CMGame(String filePath) throws Exception
	{
		super("Chess Magister Game");
		init();
	}
	
	public CMGame() throws Exception
	{
		super("Chess Magister Game", true, true, true, true);
		init();
	}
}
