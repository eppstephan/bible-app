package com.bible.app.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.stereotype.Service;

import com.bible.app.model.Finding;
import com.bible.app.model.Passage;
import com.bible.app.model.Search;
import com.bible.app.model.Section;
import com.bible.app.model.Word;
import com.bible.app.text.Book;
import com.bible.app.text.Chapter;
import com.bible.app.text.Verse;

@Service
public class BibleService {

	/**
	 * Hash map containing key value pairs with the book name of type String as key
	 * and the {@link Book} object as value.
	 */
	private Map<String, Book> bookMap;

	/**
	 * Hash set containing string elements which will be ignored during
	 * {@link #countWords(Section)}.
	 */
	private HashSet<String> ignore;

	public BibleService() throws FileNotFoundException, IOException {
		readBibleLuther1912();
		readIgnore();
	}

	public ArrayList<String> getBooksAsList() {
		return new ArrayList<String>(bookMap.keySet());
	}

	public ArrayList<String> getChaptersAsList() {
		ArrayList<String> chapters = new ArrayList<String>();
		for (Book b : bookMap.values()) {
			chapters.add("" + b.getChapter().size());
		}
		return chapters;
	}

	public ArrayList<ArrayList<String>> getVersesAsListOfLists() {
		ArrayList<ArrayList<String>> verses = new ArrayList<ArrayList<String>>();
		for (Book b : bookMap.values()) {
			ArrayList<String> versesOfEachChapter = new ArrayList<String>();
			for (Chapter c : b.getChapter().values()) {
				versesOfEachChapter.add("" + c.getVerses().size());
			}
			verses.add(versesOfEachChapter);
		}
		return verses;
	}

	/**
	 * Returns an array list of verses according to the specified passage.
	 * 
	 * @param passage of type {@link Passage} to return the list of verses for
	 * @return array list of verses of type {@link Verse}
	 */
	public ArrayList<Verse> getVerses(Passage passage) {
		return new ArrayList<Verse>(
				bookMap.get(passage.getBook()).getChapter().get(passage.getChapter()).getVerses().values());
	}

	/**
	 * Searches the specified search string {@link Search#search} which will be
	 * searched in the specified search section {@link Search#section}.
	 * 
	 * @param search object specifying the search string {@link Search#search} and
	 *               the search section {@link Search#section}
	 * @return list of findings of type {@link Finding}
	 */
	public List<Finding> search(Search search) {
		List<Finding> findings = new ArrayList<Finding>();

		Section section = getCurrentSection(search);

		Passage currentPassage = new Passage();
		currentPassage.setBook(section.getBookFrom());
		currentPassage.setChapter(section.getChapterFrom());
		currentPassage.setVerse(section.getVerseFrom());

		String searchText = getSearchText(search.getSearch());
		do {
			String verseText = bookMap.get(currentPassage.getBook()).getChapter().get(currentPassage.getChapter())
					.getVerses().get(currentPassage.getVerse()).getText();

			List<Integer> indices = getIndexOfIndices(verseText, searchText);
			if (indices.size() > 0) {
				Finding finding = new Finding();
				finding.setPassage(
						new Passage(currentPassage.getBook(), currentPassage.getChapter(), currentPassage.getVerse()));
				finding.setVerseText(getFormattedVerseText(indices, verseText, searchText));
				findings.add(finding);
			}
			goToNextPassage(currentPassage);
		} while (!toPassageReached(currentPassage, section));

		return findings;
	}

	private String getFormattedVerseText(List<Integer> indices, String verseText, String searchText) {
		for (int i = 0; i < indices.size(); i++) {
			verseText = insertString(verseText, "<b>", indices.get(i) + (i * 7));
			verseText = insertString(verseText, "</b>", indices.get(i) + (i * 7) + searchText.length() + 3);
		}
		return verseText;
	}

	private String insertString(String originalString, String stringToBeInserted, int index) {
		return originalString.substring(0, index) + stringToBeInserted + originalString.substring(index);
	}

	private List<Integer> getIndexOfIndices(String verseText, String searchText) {
		List<Integer> indices = new ArrayList<Integer>();
		int index = verseText.toLowerCase().indexOf(searchText.toLowerCase());
		while (index >= 0) {
			indices.add(index);
			index = verseText.toLowerCase().indexOf(searchText.toLowerCase(), index + 1);
		}
		return indices;
	}

	private String getSearchText(String search) {
		String searchText = new String();

		StringTokenizer tokenizer = new StringTokenizer(search);
		while (tokenizer.hasMoreElements())
			searchText += tokenizer.nextToken() + " ";

		return searchText.substring(0, searchText.length() - 1);
	}

	private Section getCurrentSection(Search search) {
		Section section = new Section();
		if (bookMap.containsKey(search.getSection())) {
			section = getSection(bookMap.get(search.getSection()));
		} else {
			switch (search.getSection()) {
			case "Alle":
				section.setBookFrom("1. Mose");
				section.setChapterFrom(1);
				section.setVerseFrom(1);
				section.setBookTo("Offenbarung");
				section.setChapterTo(22);
				section.setVerseTo(21);
				break;
			case "AT":
				section.setBookFrom("1. Mose");
				section.setChapterFrom(1);
				section.setVerseFrom(1);
				section.setBookTo("Maleachi");
				section.setChapterTo(3);
				section.setVerseTo(18);
				break;
			case "NT":
				section.setBookFrom("Matthäus");
				section.setChapterFrom(1);
				section.setVerseFrom(1);
				section.setBookTo("Offenbarung");
				section.setChapterTo(22);
				section.setVerseTo(21);
				break;
			default:
				section.setBookFrom("1. Mose");
				section.setChapterFrom(1);
				section.setVerseFrom(1);
				section.setBookTo("Offenbarung");
				section.setChapterTo(22);
				section.setVerseTo(21);
				break;
			}
		}
		return section;
	}

	private Section getSection(Book book) {
		Section section = new Section();
		section.setBookFrom(book.getName());
		section.setChapterFrom(1);
		section.setVerseFrom(1);
		section.setBookTo(book.getName());
		section.setChapterTo(getLastChapter(book).getChapter());
		section.setVerseTo(getLastVerse(getLastChapter(book)).getNumber());
		return section;
	}

	private Chapter getLastChapter(Book book) {
		Chapter lastChapter = null;
		for (Chapter c : book.getChapter().values())
			lastChapter = c;
		return lastChapter;
	}

	private Verse getLastVerse(Chapter chapter) {
		Verse lastVerse = null;
		for (Verse v : chapter.getVerses().values())
			lastVerse = v;
		return lastVerse;
	}

	/**
	 * Counts the words in the specified {@link Section} and returns a list of words
	 * sorted in descending order with respect to {@link Word#count}.
	 * 
	 * @param section for which words are counted
	 * @return list of words of type {@link Word} sorted in descending order with
	 *         respect to {@link Word#count}
	 */
	public List<Word> countWords(Section section) {
		HashMap<String, Word> words = new HashMap<String, Word>();

		Passage currentPassage = new Passage();
		currentPassage.setBook(section.getBookFrom());
		currentPassage.setChapter(section.getChapterFrom());
		currentPassage.setVerse(section.getVerseFrom());
		// The UI ensures that words are only counted for sections with at least one
		// verse. Meaning, the "from" passage of the section is before (or equal, i.e.,
		// words are counted only for one verse) to the "to" passage of the section.
		do {
			String[] arrWords = getSplittedVerseText(currentPassage);
			for (String s : arrWords) {
				if (!ignore(s)) {
					if (words.containsKey(s)) {
						words.get(s).setCount(words.get(s).getCount() + 1);
					} else {
						words.put(s, new Word(s, 1));
					}
				}
			}
			goToNextPassage(currentPassage);
		} while (!toPassageReached(currentPassage, section));

		List<Word> wordList = new ArrayList<Word>(words.values());
		Collections.sort(wordList);
		return wordList;
	}

	private void goToNextPassage(Passage currentPassage) {
		if (getNextVerse(currentPassage) != null) {
			currentPassage.setVerse(getNextVerse(currentPassage).getNumber());
		} else if (getNextChapter(currentPassage) != null) {
			currentPassage.setChapter(getNextChapter(currentPassage).getChapter());
			currentPassage.setVerse(1);
		} else if (getNextBook(currentPassage) != null) {
			currentPassage.setBook(getNextBook(currentPassage));
			currentPassage.setChapter(1);
			currentPassage.setVerse(1);
		} else {
		}
	}

	private boolean ignore(String s) {
		return s.length() == 0 || ignore.contains(s.toLowerCase());
	}

	private String getNextBook(Passage currentPassage) {
		if (bookMap.get(currentPassage.getBook()).getNextBook() != null)
			return bookMap.get(currentPassage.getBook()).getNextBook().getName();
		return null;
	}

	private Chapter getNextChapter(Passage currentPassage) {
		return bookMap.get(currentPassage.getBook()).getChapter().get(currentPassage.getChapter() + 1);
	}

	private Verse getNextVerse(Passage currentPassage) {
		return bookMap.get(currentPassage.getBook()).getChapter().get(currentPassage.getChapter()).getVerses()
				.get(currentPassage.getVerse() + 1);
	}

	private String[] getSplittedVerseText(Passage currentPassage) {
		String verseText = bookMap.get(currentPassage.getBook()).getChapter().get(currentPassage.getChapter())
				.getVerses().get(currentPassage.getVerse()).getText();
		return verseText.replaceAll("[\\(\\)\\.\\;\\:\\,\\!\\?\\\"\\“\\”\\d]", "").split(" ");
	}

	private boolean toPassageReached(Passage currentPassage, Section section) {
		return (getBookPosition(currentPassage.getBook()) == getBookPosition(section.getBookTo())
				&& currentPassage.getChapter() == section.getChapterTo()
				&& currentPassage.getVerse() > section.getVerseTo())
				|| (getBookPosition(currentPassage.getBook()) == getBookPosition(section.getBookTo())
						&& currentPassage.getChapter() > section.getChapterTo())
				|| (getBookPosition(currentPassage.getBook()) > getBookPosition(section.getBookTo())
						|| (getBookPosition(currentPassage.getBook()) == 65 && currentPassage.getChapter() == 22
								&& currentPassage.getVerse() == 21));
	}

	private int getBookPosition(String book) {
		return bookMap.get(book).getPosition();
	}

	/**
	 * Reads the bible Luther 1912 and stores the books containing all chapters and
	 * verses.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void readBibleLuther1912() throws FileNotFoundException, IOException {
		bookMap = new LinkedHashMap<String, Book>();
		try (InputStream inputStream = getClass().getResourceAsStream("/Luther1912.txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			Book oldBook = null, newBook = null;
			while ((line = br.readLine()) != null) {
				// Read book position
				int bookPosition = Integer.parseInt(line.substring(0, line.indexOf("#")));
				// Read books and store them.
				line = line.substring(line.indexOf("#") + 1);
				String book = line.substring(0, line.indexOf("#"));
				if (!bookMap.containsKey(book)) {
					newBook = new Book(book, bookPosition);
					bookMap.put(book, newBook);
					if (oldBook != null) {
						oldBook.setNextBook(newBook);
					}
				} else {
					oldBook = newBook;
				}
				// Read chapters and store them.
				line = line.substring(line.indexOf("#") + 1);
				int chapter = Integer.parseInt(line.substring(0, line.indexOf("#")));
				if (!bookMap.get(book).getChapter().containsKey(chapter)) {
					bookMap.get(book).getChapter().put(chapter, new Chapter(chapter));
				}
				// Read verses and store them.
				line = line.substring(line.indexOf("#") + 1);
				int verseNumber = Integer.parseInt(line.substring(0, line.indexOf("#")));
				if (!bookMap.get(book).getChapter().get(chapter).getVerses().containsKey(verseNumber)) {
					bookMap.get(book).getChapter().get(chapter).getVerses().put(verseNumber, new Verse(verseNumber));
					String verseText = line.substring(line.indexOf("#") + 1);
					bookMap.get(book).getChapter().get(chapter).getVerses().get(verseNumber).setText(verseText);
				}
			}
		}
	}

	private void readIgnore() throws IOException {
		ignore = new HashSet<String>();
		try (InputStream inputStream = getClass().getResourceAsStream("/ignore.txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.replaceAll("\\s", "");
				if (!ignore.contains(line.toLowerCase())) {
					ignore.add(line.toLowerCase());
				}
			}
		}
	}
}