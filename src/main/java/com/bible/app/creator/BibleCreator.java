package com.bible.app.creator;

import java.io.IOException;

public class BibleCreator {
	public static Bible getBible(String name) throws IOException {
		if ("Luther1912".equalsIgnoreCase(name))
			return new Luther1912();
		else if ("Elberfelder".equalsIgnoreCase(name))
			return new Elberfelder();

		return null;
	}
}
