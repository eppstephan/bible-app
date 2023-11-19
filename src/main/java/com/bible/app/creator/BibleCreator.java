package com.bible.app.creator;

import java.io.IOException;

public class BibleCreator {
	public static Bible getBible(String name) throws IOException {
		if ("Luther1912".equalsIgnoreCase(name))
			return new Luther1912();
		else if ("Elberfelder1905".equalsIgnoreCase(name))
			return new Elberfelder1905();
		else if ("Menge1939".equalsIgnoreCase(name))
			return new Menge1939();
		else if ("Schlachter1951".equalsIgnoreCase(name))
			return new Schlachter1951();

		return null;
	}
}
