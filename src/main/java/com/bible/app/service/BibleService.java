package com.bible.app.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bible.app.model.Finding;
import com.bible.app.model.Passage;
import com.bible.app.model.Search;
import com.bible.app.model.Section;
import com.bible.app.model.Word;
import com.bible.app.text.Bible;
import com.bible.app.text.BibleCreator;
import com.bible.app.text.Verse;

@Service
public class BibleService {

	private Bible active;

	public BibleService() throws IOException {
		Bible luther1912 = BibleCreator.getBible("Luther1912");
		Bible elberfelder = BibleCreator.getBible("Elberfelder");

		active = luther1912;
	}

	public ArrayList<String> getBooksAsList() {
		return active.getBooksAsList();
	}

	public ArrayList<String> getChaptersAsList() {
		return active.getChaptersAsList();
	}

	public ArrayList<ArrayList<String>> getVersesAsListOfLists() {
		return active.getVersesAsListOfLists();
	}

	public ArrayList<Verse> getVerses(Passage passage) {
		return active.getVerses(passage);
	}

	public List<Finding> search(Search search) {
		return active.search(search);
	}

	public List<Word> countWords(Section section) {
		return active.countWords(section);
	}
}