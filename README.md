# Crossroads VN Engine (Initial Beta)

A custom-built visual novel engine developed entirely from scratch using Java and JavaFX. 

This project serves as a practical application of core data structures and software architecture. Rather than relying on pre-built visual novel frameworks like Ren'Py, this engine implements a strict Model-View-Controller (MVC) architecture to manage game state, dialogue progression, and UI rendering independently.

## Current Build Status (Beta)
The initial commit includes the foundational architecture:
* **MVC Separation:** Clean division between data models, game logic, and the JavaFX presentation layer.
* **Dialogue Engine:** A lightweight, queue-based (`LinkedList`) dialogue progression system.
* **UI Framework:** A modular JavaFX interface designed for scalable, kinetic-style storytelling.

## Upcoming Roadmap
* Implementation of a custom text-file parser to separate narrative scripts from the codebase.
* Save-state management via Java File I/O.
* Integration of the complete narrative structure, following the intersecting campus lives of three main characters (Jules, Maya, and Nora).