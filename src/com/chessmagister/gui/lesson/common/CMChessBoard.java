package com.chessmagister.gui.lesson.common;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import javax.imageio.ImageIO;
import javax.swing.JFrame;

import javax.swing.JPanel;

import com.chessmagister.events.CMBoardListener;
import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMSerializableEnumData;
import com.chessmagister.io.CMXmlConstants;
import com.chessmagister.io.CMXmlMapNode;
import com.chessmagister.logic.BoardCoordinates;
import com.chessmagister.logic.CMFigure;
import com.chessmagister.utils.CMConstants;

public class CMChessBoard extends JPanel implements MouseListener, CMSerializableComplex, MouseMotionListener
{
	private static final long serialVersionUID = 1L;

	public enum XCoordinates implements CMSerializableEnumData
	{
		A, B, C, D, E, F, G, H;

		@Override
		public String getID()
		{
			return CMXmlConstants.X_COORDINATES_ID;
		}

		@Override
		public String getValue()
		{
			return this.name();
		}

		@Override
		public CMSerializableEnumData valueFromString(String value)
		{
			return XCoordinates.valueOf(value);
		}
		
		public static XCoordinates valueFromInt(int value)
		{
			for ( XCoordinates current : values() )
			{
				if ( current.ordinal() == value )
				{
					return current;
				}
			}
			
			return A;
		}
	}
	public enum YCoordinates implements CMSerializableEnumData
	{
		Y1, Y2, Y3, Y4, Y5, Y6, Y7, Y8;
		
		@Override
		public String getID()
		{
			return CMXmlConstants.Y_COORDINATES_ID;
		}

		@Override
		public String getValue()
		{
			return this.name();
		}

		@Override
		public CMSerializableEnumData valueFromString(String value)
		{
			return YCoordinates.valueOf(value);
		}
		
		public static YCoordinates valueFromInt(int value)
		{
			for ( YCoordinates current : values() )
			{
				if ( current.ordinal() == value )
				{
				return current;
				}
			}
			
			return Y1;
		}
	}

	@CMXmlMapNode(	id = CMXmlConstants.BOARD_MATRIX_ID,
					keyClass = BoardCoordinates.class,
					objClass = CMFigure.class)
	public Map<BoardCoordinates, CMFigure> m_boardMatrix = new HashMap<BoardCoordinates, CMFigure>();
	protected BoardCoordinates m_lastSelectedXY = new BoardCoordinates(XCoordinates.A, YCoordinates.Y1);
	protected BoardCoordinates m_selection = null; 

	protected CMBoardListener m_boardListener = null;

	public void setBoardListener(CMBoardListener listener) throws Exception
	{
		if(m_boardListener != null)
		{
			throw new Exception("Overriding existing board listener");
		}

		m_boardListener = listener;
	}

	public BoardCoordinates getLastSelectedXY()
	{
		return m_lastSelectedXY;
	}

	public void setLastSelectedXY(BoardCoordinates mLastSelectedXY)
	{
		m_lastSelectedXY = mLastSelectedXY;
	}

	public CMChessBoard()
	{
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setBounds(0, 0, 
						CMConstants.BOARD_SIZE + CMConstants.BOARD_X_OFFSET*2,
						CMConstants.BOARD_SIZE + CMConstants.BOARD_Y_OFFSET*2);
	}

	public void paintComponent(Graphics graphics)
	{
		try {
//		drawCells(graphics);
		drawBoard(graphics);
		drawFigures(graphics);
		drawBoardLetters(graphics);
		drawBoardNumbers(graphics);
		drawSelection(graphics);
		drawBorderBooard(graphics);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void select(BoardCoordinates xy)
	{
		m_selection = xy;
		repaint();
	}
	
	public BoardCoordinates getSelectedCoordinates()
	{
		return m_selection;
	}
	
	public void unSelect()
	{
		m_selection = null;
		repaint();
	}

	public void putFigure(CMFigure figure, BoardCoordinates xy)
	{
		m_boardMatrix.put(xy, figure);
		this.repaint();
	}
	
	public CMFigure getFigure(BoardCoordinates xy)
	{
		return (CMFigure)m_boardMatrix.get(xy);
	}

	public void removeFigure(BoardCoordinates xy)
	{
		m_boardMatrix.remove(xy);
		this.repaint();
	}

	protected void drawSelection(Graphics graphics)
	{
		if(m_selection != null)
		{
			graphics.setColor(new Color(189, 0, 0));
			Graphics2D graphics2D = (Graphics2D)graphics;
			graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
			graphics2D.fillRect(CMConstants.BOARD_X_OFFSET + CMConstants.CELL_SIZE*m_selection.x.ordinal(),
								CMConstants.BOARD_Y_OFFSET + CMConstants.CELL_SIZE*(7 - m_selection.y.ordinal()),
								CMConstants.CELL_SIZE,
								CMConstants.CELL_SIZE);
		}
	}
	
	protected void drawFigures(Graphics graphics) throws IOException
	{
		for(Object key : m_boardMatrix.keySet())
		{
			BoardCoordinates xy = (BoardCoordinates)key;
			CMFigure figure = (CMFigure)m_boardMatrix.get(key);
			graphics.drawImage(	figure.getImage(),
								CMConstants.BOARD_X_OFFSET + CMConstants.CELL_SIZE*xy.x.ordinal(),
								CMConstants.BOARD_Y_OFFSET + CMConstants.CELL_SIZE*(7 - xy.y.ordinal()),
								CMConstants.CELL_SIZE,
								CMConstants.CELL_SIZE,
								null);
		}
	}

	protected void drawCells(Graphics graphics)
	{
		graphics.setColor(new Color(189, 127, 76));
		graphics.drawRect(	CMConstants.BOARD_X_OFFSET, CMConstants.BOARD_X_OFFSET,
							CMConstants.BOARD_SIZE, CMConstants.BOARD_SIZE);

		boolean black = false;
		
		for(int x = CMConstants.BOARD_X_OFFSET;
			x <= CMConstants.BOARD_SIZE;
			x += CMConstants.CELL_SIZE)
		{
			for(int y = CMConstants.BOARD_X_OFFSET;
			y <= CMConstants.BOARD_SIZE;
			y += CMConstants.CELL_SIZE)
			{
				if(black)
				{
					graphics.setColor(new Color(189, 127, 76));				
				}
				else
				{
					graphics.setColor(Color.WHITE);		
				}
				graphics.fillRect(x, y, CMConstants.CELL_SIZE, CMConstants.CELL_SIZE);
				black = !black;
			}
			black = !black;
		}
	}
	
	protected void drawBoard(Graphics graphics) throws IOException
	{
		graphics.setColor(new Color(189, 127, 76));
		graphics.drawRect(	CMConstants.BOARD_X_OFFSET, CMConstants.BOARD_X_OFFSET,
							CMConstants.BOARD_SIZE, CMConstants.BOARD_SIZE);

		graphics.drawImage(	ImageIO.read(new File("res/board.gif")),
				CMConstants.BOARD_X_OFFSET,
				CMConstants.BOARD_Y_OFFSET,
				CMConstants.CELL_SIZE*8,
				CMConstants.CELL_SIZE*8,
				null);

		boolean black = false;
	}

	protected void drawBoardLetters(Graphics graphics)
	{
		graphics.setColor(Color.BLACK);

		for(int x = CMConstants.BOARD_X_OFFSET*3, letter = 'A';
		x <= CMConstants.BOARD_SIZE;
		x += CMConstants.CELL_SIZE, letter++)
		{
			char letters[] = {(char) letter};
			graphics.setFont(new Font("Times New Roman Bold", Font.BOLD, 15));
			graphics.drawChars(letters, 0, 1, x, CMConstants.BOARD_X_OFFSET*3 + CMConstants.BOARD_SIZE);
			
			
		}
	}

	protected void drawBoardNumbers(Graphics graphics)
	{
		for(int y = CMConstants.BOARD_X_OFFSET*3, number = 8;
		number > 0;
		y += CMConstants.CELL_SIZE, number--)
		{
			graphics.setFont(new Font("Times New Roman Bold", Font.BOLD, 14));
			graphics.drawChars(Integer.toString(number).toCharArray(), 0, 1, CMConstants.BOARD_X_OFFSET*2 + CMConstants.BOARD_SIZE, y);
		}
	}

	protected void drawBorderBooard(Graphics graphics)
	{
		((Graphics2D) graphics).setStroke(new BasicStroke(3));
		graphics.setColor(new Color(189, 127, 76));
		graphics.drawRect(CMConstants.BOARD_X_OFFSET, CMConstants.BOARD_Y_OFFSET,CMConstants.CELL_SIZE*8, CMConstants.CELL_SIZE*8);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getPoint().x;
		int y = e.getPoint().y;

		if((x > CMConstants.BOARD_X_OFFSET + CMConstants.BOARD_SIZE ||
			y > CMConstants.BOARD_Y_OFFSET + CMConstants.BOARD_SIZE) ||
		   (x <= CMConstants.BOARD_X_OFFSET||
			y <= CMConstants.BOARD_Y_OFFSET))
		{
			return;
		}
		int xCell = ((x - CMConstants.BOARD_X_OFFSET) / CMConstants.CELL_SIZE);
		int yCell = ((y - CMConstants.BOARD_X_OFFSET) / CMConstants.CELL_SIZE);

		if(xCell > 7 || yCell > 7)
		{
			return;
		}

		XCoordinates boardXCoord = XCoordinates.values()[xCell];
		YCoordinates boardYCoord = YCoordinates.values()[7 - yCell];

		m_lastSelectedXY = new BoardCoordinates(boardXCoord, boardYCoord);

		m_boardListener.onCellClick(m_lastSelectedXY);
	}

	public Map<BoardCoordinates, CMFigure> getBoardMatrix()
	{
		return m_boardMatrix;
	}

	public void setBoardMatrix(Map<BoardCoordinates, CMFigure> mBoardMatrix)
	{
		m_boardMatrix = mBoardMatrix;
		this.repaint();
	}
	

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getID()
	{
		return CMXmlConstants.BOARD_ID;
	}

	@Override
	public void fireUpdatedEvent()
	{
		repaint();
	}

	@Override
	public void performSave() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		this.mouseClicked(e);
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
}
