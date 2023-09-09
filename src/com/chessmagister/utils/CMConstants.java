package com.chessmagister.utils;

public class CMConstants {
	final public static String MENU_ITEM_CREATE_LESSON_ID = "Create Lesson";
	final public static String MENU_ITEM_OPEN_LESSON_ID = "Open Lesson";
	final public static String MENU_ITEM_START_LESSON_ID = "Start Lesson";
	final public static String MENU_ITEM_PLAY_ID = "Play";
	final public static String MENU_ITEM_SAVE_ID = "Save";

	final public static int BOARD_SIZE = 400;
	final public static int BOARD_X_OFFSET = 10;
	final public static int BOARD_Y_OFFSET = 10;
	final public static int CELL_SIZE = BOARD_SIZE / 8;
	
	final public static int LESSON_CREATOR_WIDTH = BOARD_X_OFFSET*7 + BOARD_SIZE*2;
	final public static int LESSON_CREATOR_HEIGHT = BOARD_X_OFFSET*4 + BOARD_SIZE + CELL_SIZE*4;
}
