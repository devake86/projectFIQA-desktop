# projectFIQA (Desktop)

A Java-based quiz application built for learning and preparing for the IHK final exam (AP1), developed with JavaFX and JSON-based question handling.

---

## Project Overview

This project was created as an additional learning tool during retraining as a *Fachinformatiker*.

Motivation:
- Java and OOP concepts were not deeply covered in the official learning fields
- Need for an additional, more interactive way of learning
- Traditional studying (e.g. books while commuting) felt inefficient and slow

Solution:
- A desktop quiz application for Windows
- Designed to run in a small “snap” window next to other tasks
- Allows easy repetition and quick learning sessions

A matching Android version was developed with similar structure and logic to simplify maintenance and further development.

---

## Current Status

- Working “20 Questions Mode”
- Randomized (shuffled) selection of 20 questions from a chosen learning field
- Question format: True/False or 1 out of 4

---

## Goals

### Minimum Goals

- Implement an AP1 mode:
  - Combines all topics from learning fields 1–6
  - Selects one question from each subtopic
  - Simulates a realistic exam round

- Add repetition mode:
  - 20 Questions Mode for individual learning fields

- Integrate user feedback from testing phase

---

### Optional Goals

- AP2 mode (learning fields 1–12)
  - intended for both FIAE and FISI tracks
  - only if time permits (significantly higher workload)

- Possible implementation of multiple choice questions depending on user feedback

---

## Learning Objectives

- Improve understanding of OOP
- Deepen Java knowledge
- Learn JavaFX GUI development
- Work with JSON data structures
- Build a practical, usable learning tool for real exam preparation

---

## Changelog

### v1.0-dev (desktop & android)

Priority (based on user feedback):
- Immediate feedback on answer buttons (green/red)
- Remove extra result screen after answering
- Optional review of incorrectly answered questions after a round

General:
- Refactor desktop version to match Android structure
- Separate main menu from quiz flow for better parity
- Implement AP1 mode with JSON-based question logic
- Add at least 30 high-quality exam-oriented questions per subtopic (LF1–LF6)
- Add repetition mode for individual learning fields
- Track correctly answered questions using IDs and timestamps
- Reintroduce questions into the pool after 2–3 days (learning curve)
- Consider adding statistics / correctness percentage display

---

### v1.0-beta (desktop & android)

- Implemented GUI versions for Desktop (JavaFX) and Android
- Android version already separates menu and quiz flow
- Created Windows desktop build (.exe)
- Created Android debug build (.apk)

User testing:
- Tested with classmates (learning field 9 questions)
- Overall positive feedback
- UI improvements requested

---

### v0.1-prototype (desktop)

- Implemented core quiz classes
- Created JSON structure and loader
- Implemented quiz logic in engine
- Basic console output for testing in main class
