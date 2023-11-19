package com.bible.app.creator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

import com.bible.app.text.Book;
import com.bible.app.text.Chapter;

public class Elberfelder extends Bible {

	public Elberfelder() throws IOException {
		readBible();
		readIgnore();

		booksAsList = new ArrayList<String>(bookMap.keySet());
		chaptersAsList = new ArrayList<String>();
		versesAsListOfLists = new ArrayList<ArrayList<String>>();

		for (Book b : bookMap.values()) {
			chaptersAsList.add("" + b.getChapter().size());
			ArrayList<String> versesOfEachChapter = new ArrayList<String>();
			for (Chapter c : b.getChapter().values()) {
				versesOfEachChapter.add("" + c.getVerses().size());
			}
			versesAsListOfLists.add(versesOfEachChapter);
		}
	}

	@Override
	public void readBible() throws IOException {
		bookMap = new LinkedHashMap<String, Book>();
	}

	public void readIgnore() throws IOException {
		ignore = new HashSet<String>();
	}
}
