# Crossroads VN Engine (Initial Beta)

A custom-built visual novel engine developed entirely from scratch using Java and JavaFX. 

This project serves as a practical application of core data structures and software architecture. Rather than relying on pre-built visual novel frameworks like Ren'Py, this engine implements a strict Model-View-Controller (MVC) architecture to manage game state, dialogue progression, and UI rendering independently.

## Current Build Status (Beta)
The initial commit includes the foundational architecture:
* **MVC Separation:** Clean division between data models, game logic, and the JavaFX presentation layer.
* **Dialogue Engine:** A lightweight, queue-based (`LinkedList`) dialogue progression system.
* **UI Framework:** A modular JavaFX interface designed for scalable, kinetic-style storytelling.
* **Audio System:** Classpath-based background music playback integrated with scene transitions.

## Upcoming Roadmap

### 1. Visual Layer & Asset Specifications
* **Background System:**
  * **File Formats:** Supported formats include `.jpg` and `.png`.
  * **Resolution & Scaling:** Target resolution based on standard 16:9 or window scaling (e.g., 1920x1080 / 1280x720) rendered on the base z-index layer behind sprites and UI.
  * **Usage:** Managed via scene state changes to reflect campus locations, rooms, and time of day.
* **Character Sprite & Pose System (DDLC-Style):**
  * **File Format:** High-resolution `.png` files with transparent backgrounds.
  * **Pose & Expression States:** Each character (Jules, Maya, Nora) possesses a baseline default model along with interchangeable poses and expressions (e.g., neutral, speaking, thinking, distressed, smiling) called dynamically per dialogue line or scene action.
  * **Screen Positioning & Layering:** Rendered in front of the background layer and behind the dialogue/choice UI, supporting screen slot positioning (e.g., Left, Center, Right) for multiple characters.
  * **Engine Integration:** Script commands and dialogue model nodes will specify `[Character] [Pose] [Position]` tags to update visible sprites seamlessly during conversation progression.

### 2. Custom Script Parser
* Implementation of a custom text-file parser to separate narrative scripts and visual cues from compiled Java code.

### 3. Save-State Management
* Persistent game-state tracking via Java File I/O.

### 4. Complete Narrative Integration
* Expanding all character storylines (Jules, Maya, Nora) and weaving their intersecting campus routes.

## License & Copyright
Crossroads is a free-to-play visual novel. The custom engine code is open source under the MIT License. All game assets (narrative storylines, scripts, character concepts, artwork, and audio) are strictly **Non-Commercial (No Monetary Gain)** under Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0). Under no circumstances may any assets be used for commercial purposes or monetization. See [COPYRIGHT.txt](COPYRIGHT.txt) for full license terms and attribution requirements.