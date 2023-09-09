package com.chessmagister.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import com.chessmagister.gui.lesson.creator.CMLessonCreator;
import com.chessmagister.gui.lesson.player.CMLessonPlayer;
import com.chessmagister.io.CMXmlIo;
import com.chessmagister.utils.CMConstants;

public class CMMainWindow extends JFrame implements ActionListener
{

	JDesktopPane desktop = null;

	// DUMY
	private static final long serialVersionUID = 1L;

	/**
	 * Main method of the class
	 * @param args
	 */
	public static void main(String[] args)
	{
	    try
		{
			new CMMainWindow();
		}
	    catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	  public CMMainWindow() throws IOException
	  {
	    super("Chess Magister");

	    JFrame.setDefaultLookAndFeelDecorated( true );
	    desktop = new JDesktopPane();
	    setSize(1200, 800);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    JMenuBar mainMenu = new JMenuBar();

	    JMenu jmStart = new JMenu("Start");
	    JMenuItem jmiCreateLesson = new JMenuItem(CMConstants.MENU_ITEM_CREATE_LESSON_ID);
	    JMenuItem jmiOpenLesson = new JMenuItem(CMConstants.MENU_ITEM_OPEN_LESSON_ID);
	    JMenuItem jmiStartLesson = new JMenuItem(CMConstants.MENU_ITEM_START_LESSON_ID);
	    JMenuItem jmiSaveLesson = new JMenuItem(CMConstants.MENU_ITEM_SAVE_ID);
	    JMenuItem jmiPlay = new JMenuItem(CMConstants.MENU_ITEM_PLAY_ID);
	    JMenuItem jmiExit = new JMenuItem("Exit");
	    jmStart.add(jmiCreateLesson);
	    jmStart.add(jmiOpenLesson);
	    jmStart.add(jmiStartLesson);
	    jmStart.add(jmiPlay);
	    jmStart.add(jmiSaveLesson);
	    jmStart.addSeparator();
	    jmStart.add(jmiExit);
	    mainMenu.add(jmStart);

	    JMenu jmHelp = new JMenu("Help");
	    JMenuItem jmiAbout = new JMenuItem("About");
	    jmHelp.add(jmiAbout);
	    mainMenu.add(jmHelp);

	    jmiCreateLesson.addActionListener(this);
	    jmiOpenLesson.addActionListener(this);
	    jmiStartLesson.addActionListener(this);
	    jmiSaveLesson.addActionListener(this);
	    jmiPlay.addActionListener(this);
	    jmiExit.addActionListener(this);
	    jmiAbout.addActionListener(this);

	    this.setJMenuBar(mainMenu);
	    
	    JToolBar toolBar = new JToolBar();
	    Image createLessonImg = ImageIO.read(new File("res/newLessonButton.gif"));
		JButton createLesson = new JButton("New", new ImageIcon(createLessonImg.getScaledInstance(	CMConstants.CELL_SIZE,
																					CMConstants.CELL_SIZE, Image.SCALE_FAST)));
	    createLesson.setActionCommand(CMConstants.MENU_ITEM_CREATE_LESSON_ID);
	    createLesson.addActionListener(this);
	    toolBar.add(createLesson);
	    
	    Image openLessonImg = ImageIO.read(new File("res/openLessonButton.gif"));
		JButton openLesson = new JButton("Open", new ImageIcon(openLessonImg.getScaledInstance(	CMConstants.CELL_SIZE,
																					CMConstants.CELL_SIZE, Image.SCALE_FAST)));
		openLesson.setActionCommand(CMConstants.MENU_ITEM_OPEN_LESSON_ID);
		openLesson.addActionListener(this);
	    toolBar.add(openLesson);
	    
	    Image startLessonImg = ImageIO.read(new File("res/startLessonButton.gif"));
		JButton startLesson = new JButton("Start", new ImageIcon(startLessonImg.getScaledInstance(	CMConstants.CELL_SIZE,
																					CMConstants.CELL_SIZE, Image.SCALE_FAST)));
		startLesson.setActionCommand(CMConstants.MENU_ITEM_START_LESSON_ID);
		startLesson.addActionListener(this);
	    toolBar.add(startLesson);
	    
	    Image playImg = ImageIO.read(new File("res/PlayGameButton.gif"));
		JButton play = new JButton("Play Chess", new ImageIcon(playImg.getScaledInstance(	CMConstants.CELL_SIZE,
																					CMConstants.CELL_SIZE, Image.SCALE_FAST)));
		play.setActionCommand(CMConstants.MENU_ITEM_PLAY_ID);
		play.addActionListener(this);
	    toolBar.add(play);

	    this.add(toolBar, BorderLayout.NORTH);
	    this.add(desktop);
	    this.setVisible(true);

	    setVisible(true);
	  }
	  
	  public void actionPerformed(ActionEvent ae)
	  {
		  if(ae.getActionCommand().equals(CMConstants.MENU_ITEM_CREATE_LESSON_ID))
		  {
			try {
			  CMLessonCreator frame;
			  frame = new CMLessonCreator();

			  // set frame params
		      frame.setLocation(30, 30);
		      frame.setSize(CMConstants.LESSON_CREATOR_WIDTH,
		    		  		CMConstants.LESSON_CREATOR_HEIGHT);
		      frame.setBackground(Color.white);
		      
		      // add frame to desktop and make it visible
		      desktop.add(frame);
		      frame.moveToFront();
		      frame.setVisible(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		  else if(ae.getActionCommand().equals(CMConstants.MENU_ITEM_START_LESSON_ID))
		  {
			try
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogType(JFileChooser.OPEN_DIALOG);
				int returnVal = chooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					System.out.println("You chose to open this file: "
							+ chooser.getSelectedFile().getPath());

					CMLessonPlayer frame;
					frame = new CMLessonPlayer(chooser.getSelectedFile()
							.getPath());

					CMXmlIo.readFromXml(frame, chooser.getSelectedFile()
							.getPath());

					// set frame params
					frame.setLocation(30, 30);
					frame.setSize(CMConstants.LESSON_CREATOR_WIDTH,
							CMConstants.LESSON_CREATOR_HEIGHT);
					frame.setBackground(Color.white);

					// add frame to desktop and make it visible
					desktop.add(frame);
					frame.moveToFront();
					frame.setVisible(true);
				}
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		  else if(ae.getActionCommand().equals(CMConstants.MENU_ITEM_PLAY_ID))
		  {
			try {
			  CMGame frame;
			  frame = new CMGame();

			  // set frame params
		      frame.setLocation(30, 30);
		      frame.setSize(CMConstants.LESSON_CREATOR_WIDTH,
		    		  		CMConstants.LESSON_CREATOR_HEIGHT);
		      frame.setBackground(Color.white);
		      
		      // add frame to desktop and make it visible
		      desktop.add(frame);
		      frame.moveToFront();
		      frame.setVisible(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		  else if(ae.getActionCommand().equals(CMConstants.MENU_ITEM_OPEN_LESSON_ID))
		  {
			  try {
				  JFileChooser chooser = new JFileChooser();
					chooser.setDialogType(JFileChooser.OPEN_DIALOG);
				    int returnVal = chooser.showOpenDialog(this);
				    if(returnVal == JFileChooser.APPROVE_OPTION)
				    {
				       System.out.println("You chose to open this file: " +
				            chooser.getSelectedFile().getPath());

				       CMLessonCreator frame;
				       frame = new CMLessonCreator(chooser.getSelectedFile().getPath());

				       CMXmlIo.readFromXml(frame, chooser.getSelectedFile().getPath());

						// set frame params
						frame.setLocation(30, 30);
						frame.setSize(CMConstants.LESSON_CREATOR_WIDTH,
						CMConstants.LESSON_CREATOR_HEIGHT);
						frame.setBackground(Color.white);
						  
						// add frame to desktop and make it visible
						desktop.add(frame);
						frame.moveToFront();
						frame.setVisible(true);
				    }
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  }
		  else if(ae.getActionCommand().equals(CMConstants.MENU_ITEM_SAVE_ID))
		  {
			  try
			  {
				  JInternalFrame selectedFrame = desktop.getSelectedFrame();
				  if(selectedFrame instanceof CMLessonCreator)
				  {
					  CMLessonCreator selectedWindow = (CMLessonCreator)selectedFrame;
					  selectedWindow.save();
				  }
			  }
			  catch(Exception e)
			  {
				  e.printStackTrace();
			  }
		  }
	  }
}