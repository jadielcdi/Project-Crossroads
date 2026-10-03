package com.jadielsantiago.crossroadsvn.model;

import static com.jadielsantiago.crossroadsvn.model.Choice.branch;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Jules_Story {

    public static Queue<DialogueLine> getScene() {
        Queue<DialogueLine> scene = new LinkedList<>();

        // Scene 1: Monday - The Weight of Expectations
        scene.add(new DialogueLine("Narrator", "[Scene 1: Monday Morning - The Weight of Expectations]"));
        scene.add(new DialogueLine("Narrator", "[BACKGROUND: The Engineering Quad, 8:10 a.m. The crisp autumn air bites at Jules's face. Cold morning mist clings to the brutalist concrete facades of the science buildings.]"));
        scene.add(new DialogueLine("Narrator", "[AUDIO: The distant chime of the campus clock tower, accompanied by the muffled shuffle of student foot traffic.]"));
        scene.add(new DialogueLine("Narrator", "Jules crosses the quad with his laptop bag digging a groove into his shoulder that has been there so long he's stopped registering it as pain."));
        scene.add(new DialogueLine("Narrator", "The whiteboard in his dorm room still reads: LOCKHEED -- DISCUSSION SECTION, FINAL PASS in his own handwriting, underlined twice, like pressing harder into the dry-erase marker might make it true faster."));
        scene.add(new DialogueLine("Jules", "(V.O.) Six rewrites. Six. Dr. Okoye hasn't even seen draft four yet and I'm already picking apart five. If I don't nail this section, the whole senior design project falls apart."));
        scene.add(new DialogueLine("Narrator", "Office hours. Dr. Okoye's door is propped open, a ceramic mug that reads 'WORLD'S OKAYEST ADVISOR' sitting cold beside a stack of ungraded midterms."));
        scene.add(new DialogueLine("Dr. Okoye", "Julian. Sit. You look like you're auditioning for the part of a man who hasn't slept."));
        scene.add(new DialogueLine("Jules", "I'm fine, Dr. Okoye. I just wanted to walk through the discussion section again before I--"));
        scene.add(new DialogueLine("Dr. Okoye", "You walked through it with me on Thursday. And Tuesday. It was good on Thursday, Julian."));
        scene.add(new DialogueLine("Jules", "Good isn't the interview. Good is the callback before the actual callback."));
        scene.add(new DialogueLine("Narrator", "[Dr. Okoye pausing for a beat, studying him with quiet concern]"));
        scene.add(new DialogueLine("Dr. Okoye", "Lockheed is a real offer chasing a real you. Not a hologram of you that never sleeps. Go eat something between now and your two o'clock."));
        scene.add(new DialogueLine("Narrator", "He doesn't answer that directly. He never does."));
        scene.add(new DialogueLine("Narrator", "That evening, sitting alone at his dorm desk, his phone buzzes with an incoming call from his father."));
        scene.add(new DialogueLine("Dad", "(through phone, warm and loud) Three point nine, Julian. You keep that three point nine and Lockheed doesn't have a single reason to say no. You understand me? This is the whole thing. This is fourteen years of the whole thing."));
        scene.add(new DialogueLine("Jules", "I know, Pa."));
        scene.add(new DialogueLine("Dad", "I'm not worried. My son doesn't drop a three point nine."));
        scene.add(new DialogueLine("Narrator", "Jules doesn't correct the plural certainty in that sentence. He's carrying a B+ on a midterm that hasn't posted to the student portal yet, and he has decided, privately, that if he doesn't look at it, it isn't real."));

        // Scene 2: Tuesday - The 24/7 Café & The Code
        scene.add(new DialogueLine("Narrator", "[Scene 2: Tuesday Night - The 24/7 Café & The Code]"));
        scene.add(new DialogueLine("Narrator", "[BACKGROUND: The Mill, campus 24-hour café, 11:40 p.m. Neon signage casts a dim amber glow across sticky laminate tables. The air smells of burnt dark roast and stale adrenaline.]"));
        scene.add(new DialogueLine("Narrator", "[AUDIO: The steady, low drone of espresso grinders and the quiet, furious clatter of laptop keyboards.]"));
        scene.add(new DialogueLine("System", "[STATS: Vulnerability 0 | Health 0 | Burnout 0]"));
        scene.add(new DialogueLine("Narrator", "Jules has colonized the corner table like a small occupying army -- three portable monitors fanned out in a defensive perimeter, a laptop stand, an external mechanical keyboard, and a fourth espresso sweating a dark ring onto a napkin that reads 'GO BEARCATS'."));
        scene.add(new DialogueLine("Narrator", "The compiler progress bar has been frozen at 94% for six agonizing minutes."));
        scene.add(new DialogueLine("Jules", "(V.O.) If this fails, I lose the whole evening. If I lose the whole evening, I don't have discussion-section time tomorrow. If I don't have discussion-section time tomorrow, Okoye sees draft five instead of draft seven, and draft seven was going to be the good one."));
        scene.add(new DialogueLine("Narrator", "A wooden chair scrapes loudly at the adjacent table. Maya Sterling drops into it like she's been standing since noon, her laptop open to a fellowship application with a word count highlighted in bright red: 847 / 750."));
        scene.add(new DialogueLine("Maya", "(to herself, staring at her screen) Cool, cool. I'll just -- invent a way to say the exact same thing in fewer words. Very normal skill to need at midnight."));
        scene.add(new DialogueLine("Jules", "(without looking up from his code) Cut your second paragraph. It's restating your first one with better adjectives."));
        scene.add(new DialogueLine("Narrator", "[Maya blinking in surprise, turning to stare at him]"));
        scene.add(new DialogueLine("Maya", "Wow. Rude. Accurate, but rude."));
        scene.add(new DialogueLine("Jules", "It's why you have a hundred extra words. You're building a case twice because you don't trust the first version to land."));
        scene.add(new DialogueLine("Maya", "You built a lot of cases twice, or is this a you-specific diagnosis you're handing out for free tonight?"));

        // Choice 1
        scene.add(new Choice("Narrator", "How does Jules manage the rising anxiety in his chest right now?", List.of(
            new ChoiceOption("Mask it. Go full flawless-machine. (Preach ruthless survival)", branch(
                new DialogueLine("Jules", "You don't leave room for anyone to find the seam. No qualifiers, no 'I think,' no apologizing for taking up space on the page. Everybody's got one oxygen mask. Put yours on first, worry about the fellowship committee's oxygen later."),
                new DialogueLine("Narrator", "Jules says it like a mantra, because it is one -- for himself, mostly."),
                new DialogueLine("System", "[STAT_UPDATE: Vulnerability -1, Burnout +1]")
            )),
            new ChoiceOption("Let the crack show, just this once. (Admit his own exhaustion)", branch(
                new DialogueLine("Narrator", "Jules lets out a long, ragged exhale, finally looking up from the 94% progress bar."),
                new DialogueLine("Jules", "Honestly? I don't know if I'm the right person to give advice on not overworking something. I've rewritten the same six paragraphs so many times I don't know which draft is even mine anymore."),
                new DialogueLine("Narrator", "He surprises himself by saying it out loud. A tiny fraction of tension eases from his shoulders."),
                new DialogueLine("System", "[STAT_UPDATE: Vulnerability +1, Burnout -1]")
            ))
        )));

        scene.add(new DialogueLine("Narrator", "Maya highlights her second paragraph, pauses for a breath, and hits delete. The fellowship word count drops to 751. She still needs one more word gone."));
        scene.add(new DialogueLine("Narrator", "[Maya closing her laptop halfway, studying Jules's hollow eyes]"));
        scene.add(new DialogueLine("Maya", "For what it's worth -- whichever version of that advice you actually believe, I hope it's the one that lets you sleep tonight."));
        scene.add(new DialogueLine("Narrator", "She doesn't wait for an answer. She leaves it there on the laminate table, right next to his empty espresso cups."));

        // Scene 3: Wednesday 1:50 a.m. - The Flashcard Fortress
        scene.add(new DialogueLine("Narrator", "[Scene 3: Wednesday 1:50 a.m. - The Flashcard Fortress]"));
        scene.add(new DialogueLine("Narrator", "[BACKGROUND: The Mill, continuing into the dead of night. The ambient noise has dwindled to a lonely hum. The neon signs buzz softly against the dark windows.]"));
        scene.add(new DialogueLine("Narrator", "[AUDIO: The rhythmic, mechanical hum of an ancient laser printer churning in the back corner.]"));
        scene.add(new DialogueLine("Narrator", "The café never closed; neither did Jules. The three monitors have now been joined by a fourth combatant: a shoebox of color-coded flashcards, organized by an indexing system only Jules understands, plus a yellow highlighter tucked behind each ear like antennae."));
        scene.add(new DialogueLine("Narrator", "Nora Vance arrives in oversized sweats, hauling a cardboard tube of poster proofs toward the café's ancient laser printer, entirely unbothered by the hour."));
        scene.add(new DialogueLine("Narrator", "[Nora glancing at the sprawling flashcard fortress with an amused tilt of her head]"));
        scene.add(new DialogueLine("Nora", "Is this a study system or a hostage situation."));
        scene.add(new DialogueLine("Jules", "(a real, tired laugh escaping him) Little of both. I've rewritten my discussion section six times. I'm starting to worry the seventh time is just going to be the first time with worse handwriting."));
        scene.add(new DialogueLine("Narrator", "Nora perches on the arm of the empty chair across from him, entirely at ease in the midnight quiet."));
        scene.add(new DialogueLine("Nora", "Six. Okay. Can I ask a genuinely nosy question?"));
        scene.add(new DialogueLine("Jules", "You're printing poster proofs at two in the morning, Vance. I feel like we're well past the point of asking permission."));
        scene.add(new DialogueLine("Nora", "Does it ever feel like the whole thing -- the GPA, the interview, all of it -- is one long held breath? Like something's about to fall apart if you exhale wrong?"));
        scene.add(new DialogueLine("Jules", "(voice dropping quiet) Every single day since August."));
        scene.add(new DialogueLine("Narrator", "[Nora looking at him, not trying to fix it, but not flinching from it either]"));
        scene.add(new DialogueLine("Nora", "My biosensor project has failed eleven times. Eleven. I have a whole spreadsheet dedicated to tracking the wreckage. It's the most interesting thing I've worked on in four years, specifically because it keeps failing in brand new ways."));
        scene.add(new DialogueLine("Jules", "That's not the same as maintaining a 3.9, Nora. Engineering doesn't celebrate failure as an aesthetic."));
        scene.add(new DialogueLine("Nora", "No. It's not. I'm not saying trade your major in. I'm saying -- is there one class, just one, this semester, where you let yourself be bad at something on purpose? Not reckless-bad. Just human-bad. See what's on the other side of that."));

        // Choice 2
        scene.add(new Choice("Narrator", "How does Jules respond to Nora's radical suggestion?", List.of(
            new ChoiceOption("Write it down. Actually consider letting go. (Embrace human imperfection)", branch(
                new DialogueLine("Narrator", "Jules hesitates. Then, he reaches into the shoebox and pulls out a fresh flashcard -- blank white, no color code assigned yet."),
                new DialogueLine("Narrator", "With deliberate strokes, he writes: *one class, not perfect, see what happens.*"),
                new DialogueLine("Narrator", "He doesn't know what he'll do with it. But he doesn't throw it away; he tucks it separate from the rest."),
                new DialogueLine("System", "[STAT_UPDATE: Vulnerability +1, Health & Boundaries +1]")
            )),
            new ChoiceOption("Dismiss it, gently but completely. (Reaffirm the perfectionist standard)", branch(
                new DialogueLine("Jules", "That works for biotech, Nora. Computer engineering doesn't really have a 'be human-bad on purpose' elective. There's no partial credit on a compiler. Either it builds cleanly, or the system crashes."),
                new DialogueLine("Narrator", "He says it like a cold mathematical fact instead of a terrifying personal prison."),
                new DialogueLine("System", "[STAT_UPDATE: Vulnerability -1, Burnout +1]")
            ))
        )));

        scene.add(new DialogueLine("Narrator", "Nora stands up, retrieving her warm poster tube from the printer tray."));
        scene.add(new DialogueLine("Nora", "Symposium is Thursday, by the way. Two o'clock, Hargrove Atrium. No pressure. I just -- it would be nice if a friendly face was there that wasn't my PI staring through my soul."));
        scene.add(new DialogueLine("Narrator", "She leaves before he can answer that either."));

        // Scene 4: Wednesday Afternoon & The Breaking Point
        scene.add(new DialogueLine("Narrator", "[Scene 4: Wednesday Afternoon - The Family Echo]"));
        scene.add(new DialogueLine("Narrator", "[BACKGROUND: The exterior breezeway of the Embedded Systems building, 4:00 p.m. Jules leans against the brick wall, phone pressed to his ear.]"));
        scene.add(new DialogueLine("Narrator", "[AUDIO: The background noise of a cousin's birthday party bleeding through the line -- a festive radio, clattering dishes, children laughing.]"));
        scene.add(new DialogueLine("Dad", "Julian! I was just telling Tío Beto -- my son, senior year, already basically got Lockheed in the bag. You remember those Sunday double shifts I picked up so you could have that first laptop? Look at you now."));
        scene.add(new DialogueLine("Jules", "Yeah, Pa. I remember."));
        scene.add(new DialogueLine("Narrator", "Jules doesn't mention the compiler warnings still sitting unresolved in his terminal. It would take the shine off something his father is clearly enjoying, and Jules has gotten very good, over four years, at not being the one who takes the shine off things."));
        scene.add(new DialogueLine("Dad", "Make us proud, mijo. We're counting on you."));
        scene.add(new DialogueLine("Narrator", "The call ends. The dial tone rings hollow in Jules's ear."));

        scene.add(new DialogueLine("Narrator", "[Scene 4 Continued: Wednesday Night - The Breaking Point]"));
        scene.add(new DialogueLine("Narrator", "[BACKGROUND: The Mill, 10:30 p.m. Exactly six hours before the senior design discussion section is due. Rain lashes against the dark windows.]"));
        scene.add(new DialogueLine("Narrator", "[AUDIO: The sharp, unforgiving clack of an Enter key, followed by the sudden, deadening silence of an error report.]"));
        scene.add(new DialogueLine("Narrator", "Jules executes the final integration test on the sensor module that anchors his entire discussion section's data set. It should be routine. It has to be routine."));
        scene.add(new DialogueLine("Narrator", "The terminal flashes crimson:"));
        scene.add(new DialogueLine("System", "ERROR: calibration array mismatch -- 0 valid samples returned\nFATAL: dependency chain broken at compile-time (see: firmware_rev_9)"));
        scene.add(new DialogueLine("Narrator", "Not a typo. Not a missing semicolon. A structural, cascading failure four revisions deep, buried in code he trusted enough to stop double-checking three days ago."));
        scene.add(new DialogueLine("Narrator", "The entire data set underpinning his discussion section -- the empirical foundation Okoye is expecting on her desk at 9:00 a.m. -- does not exist."));
        scene.add(new DialogueLine("Jules", "(V.O.) (chest seizing, breath catching) Okay. Okay. I can rebuild the calibration set by six if I don't stop moving. I can rebuild it by six if I don't--"));
        scene.add(new DialogueLine("Narrator", "His hands are shaking violently on the keyboard. He knocks over his lukewarm espresso cup; the black liquid puddles across the table, soaking into his notes. He doesn't even move to wipe it up."));
        scene.add(new DialogueLine("Narrator", "Maya walks into the café on a midnight snack run. She spots him instantly from across the room -- the rigid, unnatural stillness that isn't calm, the screen overflowing with red error text, the spilled drink."));
        scene.add(new DialogueLine("Narrator", "[Maya walking over, sliding into the booth across from him, tone quiet and devoid of drama]"));
        scene.add(new DialogueLine("Maya", "Hey. What's the damage?"));

        // Choice 3
        scene.add(new Choice("Narrator", "Does Jules hide the disaster, or let her see the truth?", List.of(
            new ChoiceOption("Hold the line. \"I've got it.\" (Protect the mask of self-reliance)", branch(
                new DialogueLine("Jules", "It's fine. It's just a firmware thing. I've got it."),
                new DialogueLine("Narrator", "His voice comes out thin and cracked, directly contradicting every word of the sentence."),
                new DialogueLine("System", "[STAT_UPDATE: Vulnerability -2, Burnout +2]"),
                new DialogueLine("Narrator", "[Maya looking at him gently, not buying the lie for a second, but refusing to push him]"),
                new DialogueLine("Maya", "Okay. I'll be sitting right here if 'got it' turns into anything else."),
                new DialogueLine("Narrator", "She stays -- quietly, without asking permission, pulling up a chair at the next table to keep vigil. It doesn't fix the calibration array, but it changes, slightly, how suffocating the room feels.")
            )),
            new ChoiceOption("Let it out. \"I'm not okay.\" (Surrender the burden and ask for help)", branch(
                new DialogueLine("Narrator", "Jules grips the edges of the table, his knuckles turning white."),
                new DialogueLine("Jules", "(voice cracking) My whole data set just -- evaporated. Six hours before it's due. I don't think I can rebuild this by morning and I don't know what happens to me if I can't."),
                new DialogueLine("System", "[STAT_UPDATE: Vulnerability +2, Health & Boundaries +1, Burnout -1]"),
                new DialogueLine("Narrator", "[Maya pulling her chair around to his side of the table, calm and unhurried]"),
                new DialogueLine("Maya", "Okay. First: breathe, because your face is currently the exact color of that error text. Second -- I once got a D-minus in a class that was supposed to be my entire life plan, Jules. I promise you, the floor exists, and it did not, in fact, kill me. Let's look at what's actually broken instead of what it means if it's broken.")
            ))
        )));

        scene.add(new DialogueLine("Narrator", "Maya pulls a napkin to mop up the coffee and pulls the keyboard toward them both."));
        scene.add(new DialogueLine("Narrator", "Somewhere around 1:15 a.m., working through the raw logs line by line, they spot the culprit: a schema mismatch introduced during a rushed refactor two nights ago. It's fixable. Not catastrophic. Just buried under a panic that made it look insurmountable."));
        scene.add(new DialogueLine("Jules", "(V.O.) It wasn't unsolvable. It just felt unsolvable at eleven p.m. alone. That might be the actual finding of this whole semester."));

        // Scene 5: Thursday - Showing Up
        scene.add(new DialogueLine("Narrator", "[Scene 5: Thursday Afternoon - Showing Up]"));
        scene.add(new DialogueLine("Narrator", "[BACKGROUND: Hargrove Atrium exterior, 1:45 p.m. Polished glass reflections under an overcast sky. Students and faculty stream into the building for the Research Symposium.]"));
        scene.add(new DialogueLine("Narrator", "[AUDIO: The lively buzz of the convention atrium, punctuated by bursts of applause from the presentation halls.]"));
        scene.add(new DialogueLine("Narrator", "The calibration array is patched -- inelegantly, at 3:30 a.m., but functional. The discussion section is submitted, four hours late, accompanied by an honest, unvarnished email to Dr. Okoye that Jules rewrote exactly zero times."));
        scene.add(new DialogueLine("Narrator", "Nora's symposium presentation is in fifteen minutes. Back in his dorm room, his terminal still has two compiler warnings he hasn't chased down."));

        // Choice 4
        scene.add(new Choice("Narrator", "The symposium presentation, or chasing compiler warnings in the terminal?", List.of(
            new ChoiceOption("Skip the symposium. The warnings won't fix themselves. (Choose the machine)", branch(
                new DialogueLine("Narrator", "Jules types a hurried text to Nora: *Good luck today, stuck on something in the lab, rooting for you.*"),
                new DialogueLine("Narrator", "He turns back toward the engineering building. The warnings take forty minutes to resolve. He spends the next twenty staring blankly at a green checkmark, feeling utterly hollow inside."),
                new DialogueLine("System", "[STAT_UPDATE: Health & Boundaries -1, Burnout +1]")
            )),
            new ChoiceOption("Go to the symposium. The warnings can wait fifteen minutes. (Show up for someone else)", branch(
                new DialogueLine("Narrator", "Jules turns away from the lab and pushes open the atrium doors."),
                new DialogueLine("Narrator", "He slides into a folding chair three rows back. He pulls his phone up, recording, as Nora takes the stage to present her sepsis biosensor. She talks openly about her eleventh failure and the breakthrough that followed. She is brilliant, unhurried, and genuinely having fun."),
                new DialogueLine("Narrator", "A few rows over, Maya is holding a plastic-wrapped bouquet of gas-station carnations with a massive grin on her face."),
                new DialogueLine("System", "[STAT_UPDATE: Vulnerability +1, Health & Boundaries +1, Burnout -1]")
            ))
        )));

        scene.add(new DialogueLine("Narrator", "[Scene 5 Continued: Thursday Evening - The Shared Plate]"));
        scene.add(new DialogueLine("Narrator", "[BACKGROUND: The Mill, 7:30 p.m. A shared plate of hot french fries sits between them at the wooden booth.]"));
        scene.add(new DialogueLine("Narrator", "The three of them end up at the same corner table that evening. Nora's presentation poster is rolled up beside her, bearing a shiny second-place ribbon for the Callahan Prize."));
        scene.add(new DialogueLine("Maya", "(stealing a fry from the plate) So. Rivera. Day four of the Lockheed Death March. Status report."));
        scene.add(new DialogueLine("Jules", "(and for the first time all week, his tone is almost light) Status report: the drone -- sorry, the calibration array -- did not, in fact, kill me. I have it on good authority the floor exists and isn't fatal."));
        scene.add(new DialogueLine("Maya", "(grinning broadly) Look at you, quoting me back to me."));
        scene.add(new DialogueLine("Nora", "Anyway. It's not perfect. Neither is the semester. Turns out that's survivable too."));

        // Scene 6: Friday - The Final Decision
        scene.add(new DialogueLine("Narrator", "[Scene 6: Friday Morning - The Fork in the Road]"));
        scene.add(new DialogueLine("Narrator", "[BACKGROUND: Jules's dorm room, 10:30 a.m. Sunlight cuts through the blinds, illuminating the whiteboard above his desk.]"));
        scene.add(new DialogueLine("Narrator", "Friday morning arrives. The week's toll is tallied, but the final step is still Jules's to make. Will he let others into his struggle, or carry the crushing weight alone?"));

        // Choice 5 - Major Branch Split
        scene.add(new Choice("Narrator", "How does Jules face Friday morning? Does he let them in, or carry it alone one more time?", List.of(
            new ChoiceOption("Let them in. (Branch A: Dropping the Guard -- Reach out, plan off-campus, tell Dad the truth)", branch(
                getBranchA()
            )),
            new ChoiceOption("Carry it alone. (Branch B: Complete Burnout -- Retreat behind the fortress, protect the 3.9)", branch(
                getBranchB()
            ))
        )));

        return scene;
    }

    private static DialogueLine[] getBranchA() {
        List<DialogueLine> lines = new LinkedList<>();

        lines.add(new DialogueLine("Narrator", "[Branch A: Dropping the Guard]"));
        lines.add(new DialogueLine("Narrator", "[BACKGROUND: Jules's dorm room, 11:00 a.m. The window is cracked open, letting in the cool, crisp breeze.]"));
        lines.add(new DialogueLine("Narrator", "Jules sits at his desk. He opens a blank Google Doc, but instead of an eighth rewrite of the discussion section, he builds a color-coded itinerary."));
        lines.add(new DialogueLine("Narrator", "It's the same instinct that organizes his flashcards, but aimed at something completely different:\n*Saturday: Off Campus, Finally.*\nA hiking trailhead circled in green. A vintage diner circled in yellow. And absolutely nothing circled in red."));
        lines.add(new DialogueLine("Jules", "(V.O.) Turns out I contain multitudes outside of the discussion section. Who knew the color-coding instinct had other uses."));
        lines.add(new DialogueLine("Narrator", "He drops the link into a group chat with Maya and Nora.\nMaya immediately replies: *Finally. Please save me from bovine genetics.*"));
        
        lines.add(new DialogueLine("Narrator", "[BACKGROUND: The fire escape landing outside the apartment building, Friday, 6:30 p.m. The city skyline is turning a brilliant orange and deep purple behind the water towers.]"));
        lines.add(new DialogueLine("Narrator", "[AUDIO: The distant hum of evening traffic and the gentle gust of wind through the iron railings.]"));
        lines.add(new DialogueLine("Narrator", "Jules sits on the iron grating with his back against the brick wall. His phone is in his hand. He doesn't text. He dials his parents' number, because some things demand a human voice."));
        lines.add(new DialogueLine("Jules", "Hey, Ma. Is Pa around too? I want to tell you both something."));
        lines.add(new DialogueLine("Narrator", "His father picks up the extension, his voice booming with pride. But Jules cuts in gently before the usual script can start."));
        lines.add(new DialogueLine("Narrator", "Jules tells them everything. He tells them about the calibration failure, the four-hour late submission, the B+ on the midterm he hid, and not sleeping properly for three weeks. He tells them how terrified he was of letting them down."));
        lines.add(new DialogueLine("Narrator", "There is a long, deafening silence on the line. Long enough that Jules's stomach drops into freefall."));
        lines.add(new DialogueLine("Narrator", "Then his father speaks. His voice is quieter than Jules has ever heard it, stripped of all bravado."));
        lines.add(new DialogueLine("Dad", "Julian. The three point nine was never supposed to cost you your soul. I picked up those Sunday shifts so you'd get to choose your life. Not so you'd run yourself into the ground proving you deserved to have chosen it."));
        lines.add(new DialogueLine("Narrator", "Jules feels something held taut for four years finally snap clean. A tear slips down his cheek, followed by a shaky, genuine laugh."));
        lines.add(new DialogueLine("Jules", "I know, Pa. I'm starting to know that now."));
        lines.add(new DialogueLine("Narrator", "He doesn't fix the whole semester that night. The B+ still posts to his transcript. The Lockheed interview still looms on Monday, nerves and all."));
        lines.add(new DialogueLine("Narrator", "But for the first time in four years, the interview isn't the only thing standing between him and being allowed to exist. Tomorrow morning, there is a trailhead, a diner booth, and two people who already know the worst things that happened this week and showed up anyway."));
        lines.add(new DialogueLine("System", "[FINAL STATS: Vulnerability MAXED | Health & Boundaries HIGH | Burnout RESOLVED]"));
        lines.add(new DialogueLine("Narrator", "\"The weight didn't disappear. It just stopped being only his to carry.\""));
        lines.add(new DialogueLine("System", "*Ending A: Dropping the Guard.*"));

        return lines.toArray(new DialogueLine[0]);
    }

    private static DialogueLine[] getBranchB() {
        List<DialogueLine> lines = new LinkedList<>();

        lines.add(new DialogueLine("Narrator", "[Branch B: Complete Burnout]"));
        lines.add(new DialogueLine("Narrator", "[BACKGROUND: Jules's dorm room, 11:00 a.m. The blinds are drawn shut, keeping the room in a cold, artificial twilight.]"));
        lines.add(new DialogueLine("Narrator", "Jules's phone buzzes on the desk. A text from Maya: *Fries again tonight? Nora's in.*"));
        lines.add(new DialogueLine("Narrator", "Jules stares at the screen. He types a single thumbs-up emoji, hits send, and immediately silences all notifications."));
        lines.add(new DialogueLine("Narrator", "He opens the discussion section document for an eighth rewrite that no one requested. He logs back into his terminal and chases the two compiler warnings down to zero. Then he compiles it again, just to be sure."));
        lines.add(new DialogueLine("Jules", "(V.O.) I don't have room for a diner and a trailhead. I have room for the 3.9 and the interview. That's the whole equation. That's always been the whole equation."));
        
        lines.add(new DialogueLine("Narrator", "[BACKGROUND: Jules's dorm room, Friday, 9:00 p.m. The room is pitch black except for the blinding blue glare of the monitors.]"));
        lines.add(new DialogueLine("Narrator", "[AUDIO: The loud, lonely buzz of a vibrating cell phone against bare wood.]"));
        lines.add(new DialogueLine("Narrator", "His father calls. Jules answers on the second ring."));
        lines.add(new DialogueLine("Dad", "(delighted, shouting over music) Three point nine holds, Julian! Lockheed on Monday! I'm telling everyone at work on Monday!"));
        lines.add(new DialogueLine("Jules", "(voice completely flat, hollow, robotic) Yeah, Pa. Three point nine holds."));
        lines.add(new DialogueLine("Dad", "That's my boy. Unstoppable. Get some rest this weekend!"));
        lines.add(new DialogueLine("Narrator", "The line goes dead. Jules lowers the phone."));
        lines.add(new DialogueLine("Narrator", "He doesn't mention that he hasn't eaten a real meal since Tuesday. He doesn't mention Maya's unanswered texts, or the shoebox of flashcards now spilling onto the floor."));
        lines.add(new DialogueLine("Narrator", "He got everything he set out to get this week. The GPA held. The interview is on the calendar. By every external metric known to his family and the department, he is an unqualified success."));
        lines.add(new DialogueLine("Narrator", "He falls asleep sitting upright in his desk chair, his chest vibrating faintly from too much caffeine, his phone clutched face-up in his hand in case Lockheed emails early."));
        lines.add(new DialogueLine("Narrator", "The room is quiet. He is entirely, precisely alone."));
        lines.add(new DialogueLine("System", "[FINAL STATS: Burnout CRITICAL | Vulnerability ZERO | Health DEPLETED]"));
        lines.add(new DialogueLine("Narrator", "\"The weight was still there. It was still only his.\""));
        lines.add(new DialogueLine("System", "*Ending B: Complete Burnout.*"));

        return lines.toArray(new DialogueLine[0]);
    }
}