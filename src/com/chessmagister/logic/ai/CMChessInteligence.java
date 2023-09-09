package com.chessmagister.logic.ai;

public class CMChessInteligence
{
	public static CMMove alphabeta(	CMPosition node,
									CMPosition result,
									int depth,
									CMMove alpha,
									CMMove beta,
									boolean maxWhite,
									boolean maxPlayer) throws Exception
	{
		if(depth == 0 || node.isTerminal())
		{
//			System.out.println("Terminal or depth 0");
			
			CMMove move = node.m_move;
			
			/*System.out.println(	"Depth[" + depth + "]" +
					"\tStart[" + move.m_start.xCoordinateAsInt() +  "]" +
					"[" +move.m_start.yCoordinateAsInt() +  "]" +
					"\tTarget[" + move.m_end.xCoordinateAsInt() +  "]" +
					"[" +move.m_end.yCoordinateAsInt() +  "]");*/
			
			return node.value(maxPlayer == maxWhite);
		}
    
		if(maxPlayer)
		{
	        for(CMPosition child: node.getChildren(maxWhite, maxPlayer))
	        {
	            CMMove value = alphabeta(	child, result, depth - 1,
            								alpha, beta, maxWhite, !maxPlayer);

	            if(alpha.m_value < value.m_value)
	            {
	            	alpha.assignValue(value);
	            	alpha.assignCoordinates(child.m_move);
	            }

//	            System.out.println("Comp::Depth " + depth + " alpha " + alpha.m_value + " beta " + beta.m_value );

	            if(beta.m_value <= alpha.m_value)
	            {
	                break;
	            }

	            result.m_move = alpha;
	        }
//	        System.out.println("Comp value: " + alpha.m_value);
	        logMove(alpha, depth, alpha.m_value, beta.m_value);
	        return alpha;
		}
		else
		{
	        for(CMPosition child: node.getChildren(maxWhite, maxPlayer))
	        {
	            CMMove value = alphabeta(child, result, depth - 1,
            			alpha, beta, maxWhite, !maxPlayer );
	            
	            if(beta.m_value > value.m_value)
	            {
	            	beta.assignValue(value);
	            	beta.assignCoordinates(child.m_move);
	            }

//	            System.out.println("Hum::Depth " + depth + " alpha " + alpha.m_value + " beta " + beta.m_value );
	            
	            if(beta.m_value <= alpha.m_value)
	            {
	                break;
	            }
	        }
//	        System.out.println("Human value: " + beta.m_value);
	        logMove(beta, depth, alpha.m_value, beta.m_value);
	        return beta;
		}
	}
	
	private static void logMove(CMMove move, int depth, int alpha, int beta)
	{
		System.out.println(	"a:" + alpha + " b:" + beta + "\tDepth[" + depth + "]" +
							"\tStart[" + move.m_start.xCoordinateAsInt() +  "]" +
							"[" +move.m_start.yCoordinateAsInt() +  "]" +
							"\tTarget[" + move.m_end.xCoordinateAsInt() +  "]" +
							"[" +move.m_end.yCoordinateAsInt() +  "]");
	}

	protected static CMMove betterMove(CMMove move1, CMMove move2)
	{
		return move1.m_value > move2.m_value ? move1 : move2;
	}
	
	protected static CMMove worseMove(CMMove move1, CMMove move2)
	{
		return move1.m_value < move2.m_value ? move1 : move2;
	}
}
