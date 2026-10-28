# Crossroads VN Engine & Visual Novel

A custom-built visual novel engine and narrative game developed entirely from scratch using Java and JavaFX.

This project serves as a practical application of core data structures and software architecture. Rather than relying on pre-built visual novel frameworks like Ren'Py, this engine implements a strict Model-View-Controller (MVC) architecture to manage game state, dialogue progression, character staging, and UI rendering independently.

---

## Current Build Status (Feature-Complete Engine & Narrative Beta)

The engine has advanced significantly beyond the initial architecture, transitioning from a basic prototype into a feature-complete visual novel runtime with fully integrated narrative routes:

### 1. Core Architecture & MVC Foundation
* **MVC Separation:** Strict decoupling between data models (`model`), game loop and script management (`controller`), and presentation layer (`view`).
* **Virtual Coordinate Canvas:** 1280x720 virtual coordinate space with automatic letterbox scaling, aspect ratio preservation across any window resolution, and dynamic ambient backdrop blur.
* **Queue-Based Dialogue Engine:** FIFO `LinkedList` dialogue queues supporting dynamic branch injections and history tracking.

### 2. Visual Staging & Dynamic Asset Layer
* **Background System (`Background_Manager`):**
  * High-DPI cover scaling for 16:9 backgrounds with aspect ratio preservation.
  * Smooth cross-fade scene transitions and ambient zoom motion.
  * Script-driven background directives (`@bg`, `@background`, `[BG: ...]`, `[BACKGROUND: ...]`) with graceful fallback to placeholder backgrounds.
* **Multi-Character Staging (`CharacterManager`):**
  * Three dedicated stage slots (`Left`, `Middle`, `Right`) supporting up to three simultaneous characters on screen.
  * Character opacity and speaker focus/dimming controls.
  * Silent staging (staging characters on screen prior to or during narration without empty text boxes).
  * Smooth cross-fading when swapping expressions in-place.
  * 30+ high-resolution transparent character sprites across Jules, Maya, and Nora with full emotion/pose sets.
* **Asset Tooling (`SpriteBackgroundRemover`):**
  * Custom internal tool for batch background removal and transparent alpha generation for raw character sprites.

### 3. Custom Scripting Parser (`JSCParser`)
* Dedicated `.jsc` (Jadiel Santiago Crossroads) screenplay parser separating narrative scripts from compiled Java code.
* Screenplay syntax specification:
  * **Section definitions & jumps:** `:: section_name` and `-> target_section`.
  * **Player choices & branching:** `? [Speaker:] Choice prompt` and `> Option text`.
  * **Story metadata directives:** `@title`, `@character`, `@music`, `@bg`.
  * **Speaker & Staging tags:** `Character_Expression(slot)` and `Character(expression, slot)`.
  * **Mid-dialogue inline directives:** `[SPRITE: ...]`, `[HIDE: ...]`, and `[BG: ...]`.

### 4. Complete Core Narrative Routes
* **Julian "Jules" Rivera – *The Weight of Expectations* (`jules_story.jsc`):**
  * Senior engineering capstone, academic burnout, perfectionism, and RTX offer pressure.
* **Maya Sterling – *The Breaking Point* (`mayas_story.jsc`):**
  * Pre-vet animal science, academic probation comeback, dairy barn responsibilities, and the Ansel Center fellowship interview.
* **Nora Vance – *The Metronome & The Airlock* (`noras_story.jsc`):**
  * Biotech lab isolation, sepsis biosensor research, the Callahan Prize, and emotional barriers.
* Intersecting campus routes with reciprocal scenes and meaningful dialogue choices.

---

## Features Implemented Beyond Original Specs

The following systems have been designed and integrated beyond the initial roadmap:

### 1. Modern Material 3 Expressive & Squircle HUD UI
* **Squircle HUD Dock (`MainMenuController`):** Horizontal floating dock inspired by visual novel and anime game aesthetics, featuring micro-interactions and smooth entrance transitions.
* **Route Selection Hub:** Interactive character cards with category accents, theme tags, route synopses, and completion badges.
* **In-Game Bento Dialogue Card:** Material 3 Expressive style dialogue box with dynamic speaker pill badges, blinking continue indicator, and a quick-action toolbar.
* **Quick HUD Toolbar:** Instant access to `Auto`, `History`, `Save`, `Load`, `Hide` (toggle UI for full CG viewing), and `Menu`.

### 2. Full-Featured Settings System (`SettingsManager` & `SettingsView`)
* **Audio Controls:** Master and BGM volume sliders, instant full-silence mute toggle.
* **Video Options:** Fullscreen mode, native display resolution matching, and virtual scaling controls.
* **Text Animation & Typing SFX:** Typewriter text speed adjustment (`Instant`, `Fast`, `Normal`, `Slow`) with toggleable typewriter audio tick sound effects.
* **How to Play Guide:** Built-in modal keyboard shortcut reference (Spacebar, Escape, H, A, S, L).
* **Persistence:** All preferences automatically saved to `saves/game_settings.properties`.

### 3. Save & Load Archive System (`SaveManager` & `SaveLoadView`)
* 54 save slots across 9 pages plus an dedicated Auto-Save slot.
* Visual screenshot thumbnail capture (`SwingFXUtils`), timestamps, active scene headings, and character stage state restoration.
* Interactive slot cards with overwrite/delete confirmation dialogs and toast notifications.

### 4. Dialogue History / Backlog Overlay
* Queue-based FIFO dialogue history viewer accessible in-game and from the pause menu, allowing players to review previous dialogue and choices.

### 5. Auto-Play Mode
* Hands-free dialogue progression with auto-advance timers, automatically pausing at decision points.

### 6. Persistent Progress Tracking & Unlock System (`ProgressManager`)
* Persistent tracking of story completions, playthrough counts, and total game completions.
* Human-readable audit log saved to `saves/story_completions.log`.
* Unlock verification architecture (`canUnlockSecretStory()`) for the upcoming secret 4th route.

---

## Roadmap to 1.0 Release & Shipment (What Is Missing)

To transition Project Crossroads from its current Beta state to a public, standalone 1.0 release, the following tasks remain:

### 1. Standalone Distribution & Packaging (Critical)
* **Native Executable Bundle:** Create standalone Windows `.exe` / installer packages using `jpackage` and `jlink` to bundle a custom lightweight JRE and JavaFX modules (removing the requirement for players to install Java or configure JavaFX paths manually).
* **Application Icon & Metadata:** Design and embed native `.ico` / `.png` application icons for window titlebar, taskbar, and installer executables.
* **Distribution Archives:** Automated zip/installer packaging scripts for clean release distribution.

### 2. Environmental Background Art Pipeline
* **Custom Background Illustrations:** Replace placeholder backgrounds (`Test_Background.jpg`) with finalized 16:9 illustrations for key narrative locations:
  * Jules's Dorm Room & Engineering Capstone Lab
  * Campus Quad & Brutalist Walkways
  * The Mill (24-hour campus coffee shop)
  * University Dairy Barn & Ansel Center
  * Biotechnology Clean Room Lab & Callahan Symposium Hall

### 3. Final Soundtrack & Soundscape Audio
* **Original Soundtrack (OST):** Replace temporary testing tracks (`MainMenu_Test.mp3`, `Jules_Test.mp3`, `Maya_Test.mp3`, `Nora_Test.mp3`) with mastered original or royalty-free BGM tracks.
* **Contextual BGM & Mood Tracks:** Add secondary music tracks for climactic, comedic, and somber scenes.
* **Ambient Soundscapes & UI SFX:** Add background ambient audio (rain, coffee shop chatter, barn livestock, lab hum) and UI button click/hover audio feedback.

### 4. Secret 4th Route ("Crossroads")
* Implement the climactic 4th route script unlocked when Jules, Maya, and Nora's stories are completed (`ProgressManager.canUnlockSecretStory()`).
* Add the 4th route card and unlock animation to the Route Selection screen.

### 5. Onboarding & Production Polish
* **Closed Beta / First-Time Experience:** Add a welcome/disclaimer modal detailing beta controls and feedback channels.
* **In-Game Credits Modal:** Attribution screen crediting developers, writers, asset creators, and CC BY-NC 4.0 license notices.
* **Multi-Platform Verification:** Verify high-DPI scaling across different operating systems (Windows 10/11, macOS, Linux).

---

## Building and Running Locally

### Prerequisites
* **Java Development Kit (JDK):** Version 17 or higher.
* **JavaFX SDK:** Version 17 or higher (configured at `C:/JavaFX/javafx-sdk-17.0.20/lib` or specified via module path).

### Compilation
```powershell
javac --release 17 --module-path "C:/JavaFX/javafx-sdk-17.0.20/lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName
Copy-Item -Path src/* -Destination bin -Recurse -Exclude *.java -Force
```

### Running
```powershell
java --module-path "C:/JavaFX/javafx-sdk-17.0.20/lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web --enable-native-access=javafx.graphics -cp bin com.jadielsantiago\crossroadsvn\view\Launcher
```

---

## License & Copyright

Crossroads is a free-to-play visual novel. The custom engine code is open source under the **MIT License**.

All game assets (narrative storylines, scripts, character concepts, artwork, and audio) are strictly **Non-Commercial (No Monetary Gain)** under **Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)**. Under no circumstances may any assets be used for commercial purposes or monetization. See [COPYRIGHT.txt](COPYRIGHT.txt) for full license terms and attribution requirements.