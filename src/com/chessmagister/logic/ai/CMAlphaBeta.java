package com.chessmagister.logic.ai;

import java.util.Random;


public class CMAlphaBeta
{
	protected static Random m_randomizer;
	static
	{
		m_randomizer = new Random();
	}
	public static final int INFINITY = 0xFFFF;
	public static int alphaBeta(CMPosition node,
								CMPosition result,
								int depth,
								int recursionLevel,
								int alpha,
								int beta,
								boolean maxWhite,
								boolean maxPlayer) throws Exception
	{
		if(depth == 0 || node.isTerminal())
		{
			return node.value(maxWhite == maxPlayer).m_value;
		}
		
		for(CMPosition child: node.getChildren(maxWhite, maxPlayer))
        {
//			logMove(child.m_move, depth, alpha, beta);
			if(alpha >= beta)
			{
				break;
			}

            int value = -alphaBeta(	child, result, depth - 1, recursionLevel + 1,
            						-beta, -alpha, maxWhite, !maxPlayer);

//            System.out.println(recursionLevel + ": Value: " + value + " Start[ " + child.m_move.m_start.xCoordinateAsInt() +  "]" +
//					"[" + child.m_move.m_start.yCoordinateAsInt() +  "]" +
//					"\tTarget[" + child.m_move.m_end.xCoordinateAsInt() +  "]" +
//					"[" + child.m_move.m_end.yCoordinateAsInt() +  "]");
            
            if(alpha < value || (alpha == value && binaryRandom()))
            {
//            	System.out.println("Value is chosen");
            	alpha = value;
//            	System.out.println("Choose value level " + recursionLevel + ": " + alpha);
            	if(recursionLevel == 0)
            	{
//            		System.out.println("Choose value level 0: " + alpha);
            		result.m_move = child.m_move;
            	}
            }

//            System.out.println("Comp::Depth " + depth + " alpha " + alpha.m_value + " beta " + beta.m_value );
        }

		return alpha;
	}

	protected static boolean binaryRandom()
	{
		return m_randomizer.nextBoolean();
	}
	
	private static void logMove(CMMove move, int depth, int alpha, int beta)
	{
		System.out.println(	"a:" + alpha + " b:" + beta + "\tDepth[" + depth + "]" +
							"\tStart[" + move.m_start.xCoordinateAsInt() +  "]" +
							"[" +move.m_start.yCoordinateAsInt() +  "]" +
							"\tTarget[" + move.m_end.xCoordinateAsInt() +  "]" +
							"[" +move.m_end.yCoordinateAsInt() +  "]");
	}
}
