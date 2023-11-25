package com.bible.app.creator;

import java.io.IOException;

public class BibleCreator {
	public static Bible getBible(String name) throws IOException {
		if ("Luther 1912".equalsIgnoreCase(name))
			return new Luther1912(name);
		else if ("Elberfelder 1905".equalsIgnoreCase(name))
			return new Elberfelder1905(name);
		else if ("Menge 1939".equalsIgnoreCase(name))
			return new Menge1939(name);
		else if ("Schlachter 1951".equalsIgnoreCase(name))
			return new Schlachter1951(name);
		else if ("World English Bible".equalsIgnoreCase(name))
			return new WorldEnglishBible("World English");
		else if ("American Standard Version".equalsIgnoreCase(name))
			return new AmericanStandardVersion("American Std");

		return null;
	}
}
