/*
 * Copyright (c) 2026 Jadiel Santiago. All rights reserved.
 *
 * This software is licensed under the MIT License.
 * See COPYRIGHT.txt in the project root for full license details.
 */
package com.jadielsantiago.crossroadsvn.controller;


import com.jadielsantiago.crossroadsvn.model.Choice;
import com.jadielsantiago.crossroadsvn.model.ChoiceOption;
import com.jadielsantiago.crossroadsvn.model.DialogueLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * JSCParser - Custom Script Parser & Story Tracker for Project Crossroads.
 *
 * Reads and interprets .jsc (Jadiel Santiago Crossroads) screenplay scripts,
 * translating human-readable text into DialogueLine and Choice queues.
 *
 * Syntax specification:
 * # or // - Comments (ignored)
 * :: section_name - Defines a named story section or branch
 * -> section_name - Jump / continuation to a named section
 * ? [Speaker:] Prompt - Defines a choice prompt (defaults Speaker to Narrator)
 * > Option text - Defines a choice option
 * Speaker: Text - Regular dialogue line (\n supported for line breaks)
 * 
 * @command args - Metadata and scene directives (@background, @music, etc.)
 */
public class JSCParser {

    /**
     * Metadata tracking information for a story.
     */
    public static class StoryMetadata {
        private final int id;
        private String title;
        private String characterName;
        private String tagline;
        private final String scriptPath;
        private String defaultMusic;

        public StoryMetadata(int id, String title, String characterName, String tagline, String scriptPath,
                String defaultMusic) {
            this.id = id;
            this.title = title;
            this.characterName = characterName;
            this.tagline = tagline;
            this.scriptPath = scriptPath;
            this.defaultMusic = defaultMusic;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getCharacterName() {
            return characterName;
        }

        public String getTagline() {
            return tagline;
        }

        public String getScriptPath() {
            return scriptPath;
        }

        public String getDefaultMusic() {
            return defaultMusic;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setCharacterName(String characterName) {
            this.characterName = characterName;
        }

        public void setTagline(String tagline) {
            this.tagline = tagline;
        }

        public void setDefaultMusic(String defaultMusic) {
            this.defaultMusic = defaultMusic;
        }

        @Override
        public String toString() {
            return String.format("[%d] %s (%s) - %s", id, title, characterName, tagline);
        }
    }

    // ==========================================
    // Story Registry
    // ==========================================
    private static final Map<Integer, StoryMetadata> STORY_REGISTRY = new LinkedHashMap<>();
    private static final Map<Integer, LinkedList<DialogueLine>> STORY_CACHE = new LinkedHashMap<>();

    static {
        registerStory(new StoryMetadata(
                1,
                "Jules's Story",
                "Julian \"Jules\" Rivera",
                "The Weight of Expectations",
                "/com/jadielsantiago/crossroadsvn/media/scripts/jules_story.jsc",
                "/com/jadielsantiago/crossroadsvn/media/music/Jules_Test.mp3"));
        registerStory(new StoryMetadata(
                2,
                "Maya's Story",
                "Maya Sterling",
                "The Breaking Point",
                "/com/jadielsantiago/crossroadsvn/media/scripts/mayas_story.jsc",
                "/com/jadielsantiago/crossroadsvn/media/music/Maya_Test.mp3"));
        registerStory(new StoryMetadata(
                3,
                "Nora's Story",
                "Nora Vance",
                "The Metronome & The Airlock",
                "/com/jadielsantiago/crossroadsvn/media/scripts/noras_story.jsc",
                "/com/jadielsantiago/crossroadsvn/media/music/Nora_Test.mp3"));
    }

    public static void registerStory(StoryMetadata metadata) {
        STORY_REGISTRY.put(metadata.getId(), metadata);
    }

    public static List<StoryMetadata> getAvailableStories() {
        return Collections.unmodifiableList(new ArrayList<>(STORY_REGISTRY.values()));
    }

    public static StoryMetadata getStoryMetadata(int storyId) {
        return STORY_REGISTRY.get(storyId);
    }

    /**
     * Loads a registered story by its numerical ID (1=Jules, 2=Maya, 3=Nora).
     * Returns a fresh copy of the DialogueLine queue.
     */
    public static Queue<DialogueLine> loadStory(int storyId) {
        if (STORY_CACHE.containsKey(storyId)) {
            return new LinkedList<>(STORY_CACHE.get(storyId));
        }

        StoryMetadata meta = STORY_REGISTRY.get(storyId);
        if (meta == null) {
            System.err.println("[JSCParser] Unknown story ID: " + storyId);
            return new LinkedList<>();
        }

        Queue<DialogueLine> parsed = parse(meta.getScriptPath(), meta);
        if (!parsed.isEmpty()) {
            STORY_CACHE.put(storyId, new LinkedList<>(parsed));
        }
        return new LinkedList<>(parsed);
    }

    /**
     * Clears all cached parsed story queues.
     */
    public static void clearCache() {
        STORY_CACHE.clear();
    }

    /**
     * Returns the total count of dialogue lines and choices in a story's main
     * track.
     */
    public static int getStoryLineCount(int storyId) {
        Queue<DialogueLine> queue = loadStory(storyId);
        return queue.size();
    }

    /**
     * Extracts all scene headings (e.g., "[Scene 1: ...]") present in a story.
     */
    public static List<String> getSceneHeadings(int storyId) {
        Queue<DialogueLine> queue = loadStory(storyId);
        List<String> headings = new ArrayList<>();
        for (DialogueLine line : queue) {
            String text = line.getText();
            if (text != null && text.startsWith("[Scene")) {
                int closing = text.indexOf(']');
                if (closing != -1) {
                    headings.add(text.substring(1, closing));
                }
            }
        }
        return headings;
    }

    // ==========================================
    // Parser Core
    // ==========================================

    /**
     * Parses a .jsc script from a classpath resource or filesystem path.
     */
    public static Queue<DialogueLine> parse(String resourceOrFilePath) {
        return parse(resourceOrFilePath, null);
    }

    /**
     * Parses a .jsc script from a classpath resource or filesystem path with optional metadata binding.
     */
    public static Queue<DialogueLine> parse(String resourceOrFilePath, StoryMetadata meta) {
        if (resourceOrFilePath == null || resourceOrFilePath.trim().isEmpty()) {
            return new LinkedList<>();
        }

        InputStream in = null;
        // 1. Try classpath resource
        String resPath = resourceOrFilePath.startsWith("/") ? resourceOrFilePath : "/" + resourceOrFilePath;
        in = JSCParser.class.getResourceAsStream(resPath);

        // 2. If not found in classpath, try direct file path
        if (in == null) {
            File f = new File(resourceOrFilePath);
            if (f.exists() && f.isFile()) {
                try {
                    in = new FileInputStream(f);
                } catch (IOException ignored) {
                }
            }
        }

        // 3. Try src relative path
        if (in == null) {
            String clean = resourceOrFilePath.startsWith("/") ? resourceOrFilePath.substring(1) : resourceOrFilePath;
            File f = new File("src/" + clean);
            if (f.exists() && f.isFile()) {
                try {
                    in = new FileInputStream(f);
                } catch (IOException ignored) {
                }
            }
        }

        // 4. Try scripts directory relative path
        if (in == null) {
            File f = new File(
                    "src/com/jadielsantiago/crossroadsvn/media/scripts/" + new File(resourceOrFilePath).getName());
            if (f.exists() && f.isFile()) {
                try {
                    in = new FileInputStream(f);
                } catch (IOException ignored) {
                }
            }
        }

        if (in == null) {
            System.err.println("[JSCParser] Failed to find script file: " + resourceOrFilePath);
            return new LinkedList<>();
        }

        try (InputStream finalIn = in) {
            return parse(finalIn, meta);
        } catch (IOException e) {
            System.err.println("[JSCParser] IO error reading script " + resourceOrFilePath + ": " + e.getMessage());
            return new LinkedList<>();
        }
    }

    /**
     * Parses a .jsc script from an InputStream.
     */
    public static Queue<DialogueLine> parse(InputStream inputStream) {
        return parse(inputStream, null);
    }

    /**
     * Parses a .jsc script from an InputStream with optional metadata binding.
     */
    public static Queue<DialogueLine> parse(InputStream inputStream, StoryMetadata meta) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return parse(reader, meta);
        } catch (IOException e) {
            System.err.println("[JSCParser] Error reading stream: " + e.getMessage());
            return new LinkedList<>();
        }
    }

    /**
     * Parses a .jsc script from a raw String.
     */
    public static Queue<DialogueLine> parseString(String scriptContent) {
        return parseString(scriptContent, null);
    }

    /**
     * Parses a .jsc script from a raw String with optional metadata binding.
     */
    public static Queue<DialogueLine> parseString(String scriptContent, StoryMetadata meta) {
        try (BufferedReader reader = new BufferedReader(new StringReader(scriptContent))) {
            return parse(reader, meta);
        } catch (IOException e) {
            System.err.println("[JSCParser] Error parsing string content: " + e.getMessage());
            return new LinkedList<>();
        }
    }

    /**
     * Internal reader-based parser that builds AST sections and resolves branches.
     */
    public static Queue<DialogueLine> parse(BufferedReader reader) throws IOException {
        return parse(reader, null);
    }

    /**
     * Internal reader-based parser that binds metadata directives without adding them to player dialogue.
     */
    public static Queue<DialogueLine> parse(BufferedReader reader, StoryMetadata meta) throws IOException {
        Map<String, List<ASTNode>> sections = new LinkedHashMap<>();
        String currentSection = "main";
        sections.put(currentSection, new ArrayList<>());

        ChoiceNode activeChoice = null;
        ChoiceOptionNode activeOption = null;
        int choiceIndent = 0;
        int optionIndent = 0;

        String rawLine;
        while ((rawLine = reader.readLine()) != null) {
            // Remove UTF-8 BOM if present
            if (rawLine.startsWith("\uFEFF")) {
                rawLine = rawLine.substring(1);
            }

            int indent = countIndent(rawLine);
            String trimmed = rawLine.trim();

            // Skip empty lines and comments
            if (trimmed.isEmpty() || trimmed.startsWith("#") || trimmed.startsWith("//")) {
                continue;
            }

            // Check for Section Header: :: section_name
            if (trimmed.startsWith("::")) {
                currentSection = trimmed.substring(2).trim().toLowerCase();
                sections.computeIfAbsent(currentSection, k -> new ArrayList<>());
                activeChoice = null;
                activeOption = null;
                continue;
            }

            // Check if choice block has ended due to an un-indented line
            if (activeChoice != null && indent <= choiceIndent && !trimmed.startsWith(">")
                    && !trimmed.startsWith("?")) {
                activeChoice = null;
                activeOption = null;
            }

            // Check for Jump: -> section_name
            if (trimmed.startsWith("->")) {
                String target = trimmed.substring(2).trim().toLowerCase();
                if (activeOption != null && indent > choiceIndent) {
                    activeOption.targetJump = target;
                } else {
                    sections.get(currentSection).add(new JumpNode(target));
                }
                continue;
            }

            // Check for Choice Prompt: ? [Speaker:] Prompt text
            if (trimmed.startsWith("?")) {
                String promptBody = trimmed.substring(1).trim();
                String speaker = "Narrator";
                String promptText = promptBody;

                int colon = promptBody.indexOf(':');
                if (colon != -1) {
                    String candidate = promptBody.substring(0, colon).trim();
                    // If candidate doesn't have spaces or is a short speaker name
                    if (!candidate.contains(" ") || candidate.startsWith("Dr.") || candidate.equals("Julian Rivera")) {
                        speaker = candidate;
                        promptText = promptBody.substring(colon + 1).trim();
                    }
                }

                ChoiceNode newChoice = new ChoiceNode(speaker, promptText);

                // Add choice to either parent option or current section
                if (activeOption != null && indent > optionIndent) {
                    activeOption.inlineNodes.add(newChoice);
                } else {
                    sections.get(currentSection).add(newChoice);
                }

                choiceIndent = indent;
                activeChoice = newChoice;
                activeOption = null;
                continue;
            }

            // Check for Choice Option: > Option text
            if (trimmed.startsWith(">")) {
                if (activeChoice == null) {
                    System.err.println(
                            "[JSCParser] Warning: Option '>' found without an active choice prompt: " + trimmed);
                    continue;
                }
                String optionText = trimmed.substring(1).trim();
                ChoiceOptionNode optionNode = new ChoiceOptionNode(optionText);
                activeChoice.options.add(optionNode);
                activeOption = optionNode;
                optionIndent = indent;
                continue;
            }

            // Directives / Metadata: @command args
            // Captured as engine configuration and never added to player dialogue queue!
            if (trimmed.startsWith("@")) {
                applyDirective(trimmed, meta);
                continue;
            }

            // Regular Dialogue Line: Speaker: Text
            DialogueNode dialogueNode = parseDialogue(trimmed);
            if (activeOption != null && indent > optionIndent) {
                activeOption.inlineNodes.add(dialogueNode);
            } else {
                activeChoice = null;
                activeOption = null;
                sections.get(currentSection).add(dialogueNode);
            }
        }

        // Resolve AST sections starting from "main"
        LinkedList<DialogueLine> result = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        visited.add("main");
        result.addAll(resolveSection("main", sections, visited));
        return result;
    }

    private static int countIndent(String line) {
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == ' ')
                count++;
            else if (c == '\t')
                count += 4;
            else
                break;
        }
        return count;
    }

    private static DialogueNode parseDialogue(String trimmed) {
        String speaker;
        String text;
        int colonIdx = trimmed.indexOf(':');

        if (colonIdx != -1) {
            speaker = trimmed.substring(0, colonIdx).trim();
            text = trimmed.substring(colonIdx + 1).trim();
        } else {
            speaker = "Narrator";
            text = trimmed;
        }

        text = text.replace("\\n", "\n");
        return new DialogueNode(speaker, text);
    }

    private static void applyDirective(String trimmed, StoryMetadata meta) {
        String body = trimmed.substring(1).trim();
        int spaceIdx = body.indexOf(' ');
        String command = (spaceIdx != -1 ? body.substring(0, spaceIdx) : body).toLowerCase();
        String arg = spaceIdx != -1 ? body.substring(spaceIdx + 1).trim() : "";

        if (meta != null) {
            switch (command) {
                case "music", "audio" -> meta.setDefaultMusic(arg);
                case "title" -> meta.setTitle(arg);
                case "character" -> meta.setCharacterName(arg);
                case "tagline" -> meta.setTagline(arg);
            }
        }
    }

    private static List<DialogueLine> resolveSection(String sectionName, Map<String, List<ASTNode>> sections,
            Set<String> visited) {
        List<DialogueLine> result = new ArrayList<>();
        List<ASTNode> nodes = sections.get(sectionName);
        if (nodes == null) {
            return result;
        }

        for (ASTNode node : nodes) {
            if (node instanceof DialogueNode d) {
                result.add(new DialogueLine(d.speaker, d.text));
            } else if (node instanceof JumpNode j) {
                String target = j.target;
                if (!visited.contains(target) && sections.containsKey(target)) {
                    Set<String> nextVisited = new HashSet<>(visited);
                    nextVisited.add(target);
                    result.addAll(resolveSection(target, sections, nextVisited));
                }
            } else if (node instanceof ChoiceNode c) {
                result.add(compileChoice(c, sections, visited));
            }
        }

        return result;
    }

    private static Choice compileChoice(ChoiceNode c, Map<String, List<ASTNode>> sections, Set<String> visited) {
        List<ChoiceOption> options = new ArrayList<>();

        for (ChoiceOptionNode opt : c.options) {
            Queue<DialogueLine> branchQueue = new LinkedList<>();

            // 1. Resolve inline nodes
            for (ASTNode inline : opt.inlineNodes) {
                if (inline instanceof DialogueNode d) {
                    branchQueue.add(new DialogueLine(d.speaker, d.text));
                } else if (inline instanceof ChoiceNode nestedChoice) {
                    branchQueue.add(compileChoice(nestedChoice, sections, new HashSet<>(visited)));
                } else if (inline instanceof JumpNode j) {
                    String target = j.target;
                    if (sections.containsKey(target)) {
                        Set<String> branchVisited = new HashSet<>(visited);
                        branchVisited.add(target);
                        branchQueue.addAll(resolveSection(target, sections, branchVisited));
                    }
                }
            }

            // 2. Resolve jump target if specified
            if (opt.targetJump != null && sections.containsKey(opt.targetJump)) {
                Set<String> branchVisited = new HashSet<>(visited);
                branchVisited.add(opt.targetJump);
                branchQueue.addAll(resolveSection(opt.targetJump, sections, branchVisited));
            }

            options.add(new ChoiceOption(opt.text, branchQueue));
        }

        return new Choice(c.speaker, c.prompt, options);
    }

    // ==========================================
    // AST Representation
    // ==========================================

    private interface ASTNode {
    }

    private static class DialogueNode implements ASTNode {
        final String speaker;
        final String text;

        DialogueNode(String speaker, String text) {
            this.speaker = speaker;
            this.text = text;
        }
    }

    private static class JumpNode implements ASTNode {
        final String target;

        JumpNode(String target) {
            this.target = target;
        }
    }

    private static class ChoiceNode implements ASTNode {
        final String speaker;
        final String prompt;
        final List<ChoiceOptionNode> options = new ArrayList<>();

        ChoiceNode(String speaker, String prompt) {
            this.speaker = speaker;
            this.prompt = prompt;
        }
    }

    private static class ChoiceOptionNode {
        final String text;
        String targetJump;
        final List<ASTNode> inlineNodes = new ArrayList<>();

        ChoiceOptionNode(String text) {
            this.text = text;
        }
    }
}
