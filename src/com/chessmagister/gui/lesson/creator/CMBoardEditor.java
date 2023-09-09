package com.chessmagister.gui.lesson.creator;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JComponent;

import com.chessmagister.events.CMBoardListener;
import com.chessmagister.gui.lesson.common.CMChessBoard;
import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMXmlComplexNode;
import com.chessmagister.io.CMXmlConstants;
import com.chessmagister.logic.BoardCoordinates;
import com.chessmagister.logic.CMBishop;
import com.chessmagister.logic.CMFigure;
import com.chessmagister.logic.CMFigureFactory;
import com.chessmagister.logic.CMKing;
import com.chessmagister.logic.CMKnight;
import com.chessmagister.logic.CMPawn;
import com.chessmagister.logic.CMQueen;
import com.chessmagister.logic.CMRook;
import com.chessmagister.logic.CMFigure.Color;
import com.chessmagister.utils.CMConstants;

public class CMBoardEditor extends JPanel implements MouseListener, CMBoardListener, CMSerializableComplex
{
	protected enum EditorAction
	{
		NO_ACTION,
		PUT_FIGURE,
		SELECT_HAND,
		ERASE
	}

	private static final long serialVersionUID = 1L;

	@CMXmlComplexNode public CMChessBoard m_chessBoard = null;
	protected JEditorPane m_editor = null; 

	protected EditorAction m_editorAction = EditorAction.NO_ACTION;
	
	protected CMTaskEditor m_taskEditor = null;
	protected JScrollPane m_taskEditorScroller = null;
	
	protected static final int m_whiteFiguresY = CMConstants.BOARD_Y_OFFSET;
	protected static final int m_blackFiguresY = CMConstants.BOARD_Y_OFFSET*2 + CMConstants.CELL_SIZE;
	protected static final int[][] m_figureCoordinates = {{CMConstants.BOARD_X_OFFSET, m_whiteFiguresY},
													{CMConstants.BOARD_X_OFFSET + CMConstants.CELL_SIZE, m_whiteFiguresY},
													{CMConstants.BOARD_X_OFFSET + CMConstants.CELL_SIZE*2, m_whiteFiguresY},
													{CMConstants.BOARD_X_OFFSET + CMConstants.CELL_SIZE*3, m_whiteFiguresY},
													{CMConstants.BOARD_X_OFFSET + CMConstants.CELL_SIZE*4, m_whiteFiguresY},
													{CMConstants.BOARD_X_OFFSET + CMConstants.CELL_SIZE*5, m_whiteFiguresY},
													// blacks
													{CMConstants.BOARD_X_OFFSET, m_blackFiguresY},
													{CMConstants.BOARD_X_OFFSET + CMConstants.CELL_SIZE, m_blackFiguresY},
													{CMConstants.BOARD_X_OFFSET + CMConstants.CELL_SIZE*2, m_blackFiguresY},
													{CMConstants.BOARD_X_OFFSET + CMConstants.CELL_SIZE*3, m_blackFiguresY},
													{CMConstants.BOARD_X_OFFSET + CMConstants.CELL_SIZE*4, m_blackFiguresY},
													{CMConstants.BOARD_X_OFFSET + CMConstants.CELL_SIZE*5, m_blackFiguresY}};

	protected static final CMFigure.Type[] m_FiguresIndex = {	CMFigure.Type.PAWN,
																CMFigure.Type.BISHOP,
																CMFigure.Type.KING,
																CMFigure.Type.ROOK,
																CMFigure.Type.KNIGHT,
																CMFigure.Type.QUEEN};
	
	protected CMFigure.Type m_selectedType = null;
	protected CMFigure.Color m_selectedColor = null;

	protected CMFigure m_selectedFigure = null;
	
	public CMBoardEditor(CMChessBoard board) throws Exception
	{
		this.setLayout(null);
		if(board == null)
		{
			throw new ExceptionInInitializerError("Board cannot be null");
		}
		m_chessBoard = board;
		board.setBoardListener(this);
		this.addMouseListener(this);

		Image hand = ImageIO.read(new File("res/hand.gif"));
		JButton handSelector = new JButton(new ImageIcon(hand.getScaledInstance(	CMConstants.CELL_SIZE,
																					CMConstants.CELL_SIZE, Image.SCALE_FAST)));
		handSelector.setBounds(	CMConstants.BOARD_X_OFFSET + CMConstants.CELL_SIZE*6,
								m_whiteFiguresY,
								CMConstants.CELL_SIZE,
								CMConstants.CELL_SIZE);
		handSelector.setVisible(true);
		handSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				m_editorAction = EditorAction.SELECT_HAND;	
			}
		});
		this.add(handSelector);

		Image eraser = ImageIO.read(new File("res/eraser.gif"));
		JButton eraserSelector = new JButton(new ImageIcon(eraser.getScaledInstance(	CMConstants.CELL_SIZE,
																					CMConstants.CELL_SIZE, Image.SCALE_FAST)));
		eraserSelector.setBounds(	CMConstants.BOARD_X_OFFSET + CMConstants.CELL_SIZE*6,
									m_blackFiguresY,
									CMConstants.CELL_SIZE,
									CMConstants.CELL_SIZE);
		eraserSelector.setVisible(true);
		eraserSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				m_editorAction = EditorAction.ERASE;	
			}
		});
		this.add(eraserSelector);

		m_editor = new JEditorPane();
		JScrollPane editorScroller = new JScrollPane(m_editor);
		m_editor.setVisible(true);
		editorScroller.setVisible(true);
		editorScroller.setBounds(CMConstants.BOARD_X_OFFSET,
								CMConstants.BOARD_Y_OFFSET*4 + CMConstants.CELL_SIZE*2,
								CMConstants.BOARD_SIZE,
								CMConstants.CELL_SIZE);

		this.add(editorScroller);

		m_taskEditor = new CMTaskEditor();
		m_taskEditorScroller = new JScrollPane(m_taskEditor);
		m_taskEditor.setVisible(true);
		m_taskEditorScroller.setVisible(true);
		m_taskEditorScroller.setBounds(CMConstants.BOARD_X_OFFSET,
								CMConstants.BOARD_Y_OFFSET*5 + CMConstants.CELL_SIZE*3,
								CMConstants.BOARD_SIZE,
								CMConstants.CELL_SIZE*4);

		this.add(m_taskEditorScroller);
	}
	
	public JEditorPane getEditor()
	{
		return m_editor;
	}

	public void setEditor(JEditorPane mEditor)
	{
		m_editor = mEditor;
	}

	public void paintComponent(Graphics graphics)
	{
		CMFigure whiteFigures[] = {	new CMPawn(CMFigure.Color.WHITE),
								new CMBishop(CMFigure.Color.WHITE),
								new CMKing(CMFigure.Color.WHITE),
								new CMRook(CMFigure.Color.WHITE),
								new CMKnight(CMFigure.Color.WHITE),
								new CMQueen(CMFigure.Color.WHITE),
								// blacks
								new CMPawn(CMFigure.Color.BLACK),
								new CMBishop(CMFigure.Color.BLACK),
								new CMKing(CMFigure.Color.BLACK),
								new CMRook(CMFigure.Color.BLACK),
								new CMKnight(CMFigure.Color.BLACK),
								new CMQueen(CMFigure.Color.BLACK)};
		try
		{
			int coordinatesIndex = 0;
			JComponent component;
			for(CMFigure figure : whiteFigures)
			{
				int x = m_figureCoordinates[coordinatesIndex][0];
				int y = m_figureCoordinates[coordinatesIndex][1];
				graphics.drawImage(	figure.getImage(),
									x,
									y,
									CMConstants.CELL_SIZE,
									CMConstants.CELL_SIZE,
									null);

				coordinatesIndex++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseClicked(MouseEvent event){
		int x = event.getPoint().x;
		int y = event.getPoint().y;
		
		if(selectEditorFigure(x, y))
		{
			m_editorAction = EditorAction.PUT_FIGURE;
		}
		else
		{
			m_editorAction = EditorAction.NO_ACTION;
		}
	}

	protected boolean selectEditorFigure(int x, int y)
	{
		CMFigure.Color color;
		
		if(y > m_whiteFiguresY && y < m_whiteFiguresY + CMConstants.CELL_SIZE)
		{
			color = CMFigure.Color.WHITE;
		}
		else if(y > m_blackFiguresY && y < m_blackFiguresY + CMConstants.CELL_SIZE)
		{
			color = CMFigure.Color.BLACK;
		}
		else
		{
			return false;
		}

		int figureIndex = (x - CMConstants.BOARD_X_OFFSET) / CMConstants.CELL_SIZE;
		if(figureIndex < 0 || figureIndex > 5)
		{
			return false;
		}

		CMFigure.Type type = m_FiguresIndex[figureIndex];
		
		m_selectedType = type;
		m_selectedColor = color;
		
		return true;
	}
	
	@Override
	public void onCellClick(BoardCoordinates coordinates)
	{
		if(m_editorAction == EditorAction.PUT_FIGURE &&
			m_selectedType != null && m_selectedColor != null)
		{
			m_chessBoard.putFigure(CMFigureFactory.createFigure(m_selectedType, m_selectedColor), coordinates);
		}
		else if(m_editorAction == EditorAction.ERASE)
		{
			m_chessBoard.removeFigure(coordinates);
		}
		else if(m_editorAction == EditorAction.SELECT_HAND)
		{
			if(m_selectedFigure != null)
			{
				m_chessBoard.removeFigure(m_chessBoard.getSelectedCoordinates());
				m_chessBoard.putFigure(m_selectedFigure, coordinates);
				m_selectedFigure = null;
			}
			else
			{
				m_selectedFigure = m_chessBoard.getFigure(coordinates);
			}
			
			m_chessBoard.select(coordinates);
		}
	}

	public CMChessBoard getChessBoard()
	{
		return m_chessBoard;
	}

	public void setChessBoard(CMChessBoard mChessBoard)
	{
		m_chessBoard = mChessBoard;
	}
	


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getID()
	{
		return CMXmlConstants.BOARD_EDITOR_ID;
	}

	@Override
	public void fireUpdatedEvent()
	{
		
	}

	@Override
	public void performSave() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public CMTaskEditor getTaskEditor()
	{
		return m_taskEditor;
	}

	public void setTaskEditor(CMTaskEditor taskEditor)
	{
		m_taskEditorScroller.getViewport().remove(m_taskEditor);
//		m_taskEditorScroller.add(taskEditor);
		m_taskEditorScroller.invalidate();
		m_taskEditorScroller.getViewport().add(taskEditor);
		m_taskEditorScroller.revalidate();
		m_taskEditor = taskEditor;
	}
}
