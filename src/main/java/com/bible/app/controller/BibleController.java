package com.bible.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.bible.app.model.Finding;
import com.bible.app.model.Passage;
import com.bible.app.model.Search;
import com.bible.app.model.Section;
import com.bible.app.model.Verse;
import com.bible.app.model.Word;
import com.bible.app.service.BibleService;

@Controller
public class BibleController {

	@Autowired
	BibleService bibleService;

	@GetMapping({ "/", "/home" })
	public String home() {
		return "home";
	}

	@GetMapping("/about")
	public String about() {
		return "about";
	}

	@GetMapping("/read")
	public String read(Model model) {
		model.addAttribute("passage", new Passage());
		model.addAttribute("verses", new ArrayList<Verse>());
		return "read";
	}

	@PostMapping("/read")
	public String readPassage(@ModelAttribute("passage") Passage passage, Model model) {
		ArrayList<Verse> verses = bibleService.getVerses(passage);
		model.addAttribute("verses", verses);
		model.addAttribute("passage", passage);
		return "read";
	}

	@GetMapping("/search")
	public String search(Model model) {
		model.addAttribute("search", new Search());
		model.addAttribute("findings", new ArrayList<Finding>());
		return "search";
	}

	@PostMapping("/search")
	public String searchResult(@ModelAttribute("search") Search search, Model model) {
		List<Finding> findings = bibleService.search(search);
		model.addAttribute("findings", findings);
		return "search";
	}

	@GetMapping("/count")
	public String count(Model model) {
		model.addAttribute("section", new Section());
		model.addAttribute("words", new ArrayList<Word>());
		return "count";
	}

	@PostMapping("/count")
	public String countPassage(@ModelAttribute("Section") Section section, Model model) {
		List<Word> words = bibleService.countWords(section);
		model.addAttribute("section", section);
		model.addAttribute("words", words);
		return "count";
	}
}
