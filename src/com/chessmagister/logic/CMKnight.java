package com.chessmagister.logic;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.chessmagister.io.CMXmlNode;

public class CMKnight extends CMFigure
{
	@CMXmlNode public static CMFigure.Type m_type = CMFigure.Type.PAWN;

	public CMKnight()
	{
		
	}
	
	public CMKnight(CMFigure.Color color)
	{
		m_color = color;
	}

	@Override
	public Image getImage() throws IOException {
		return ImageIO.read(new File(m_color == CMFigure.Color.WHITE ?
									"res/whiteknightonlight.gif" :
									"res/blackknightonlight.gif"));
	}

}
