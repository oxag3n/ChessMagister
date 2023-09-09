package com.chessmagister.io;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CMXmlMapNode
{
	String id();
	Class keyClass();
	Class objClass();
}
