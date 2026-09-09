package com.jadielsantiago.crossroadsvn.model;

import static com.jadielsantiago.crossroadsvn.model.Choice.branch;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Mayas_Story {

    public static Queue<DialogueLine> getScene() {
        Queue<DialogueLine> scene = new LinkedList<>();

        // Scene 1
        scene.add(new DialogueLine("Narrator", "[Scene 1: Monday Dawn - The Breaking Point]"));
        scene.add(new DialogueLine("Narrator", "[BACKGROUND: The university dairy barn, 4:45 a.m. The air is frigid and thick with the smell of sweet silage, iodine, and damp straw. The harsh halogen lights overhead cast long, utilitarian shadows.]"));
        scene.add(new DialogueLine("Dr. Vasquez", "Keep the transducer steady, Maya. You’re drifting off the left horn. Find the ovary."));
        scene.add(new DialogueLine("Narrator", "Maya shifts her weight in the cold muck, adjusting her grip. Her boots are numb, but her hands are steady. She watches the grainy black-and-white monitor, adjusting the angle until a dark, circular follicle blooms on the screen."));
        scene.add(new DialogueLine("Maya", "Got it. Looks like a solid 14 millimeters. She’s responding to the CIDR."));
        scene.add(new DialogueLine("Narrator", "[Dr. Vasquez pulling back, snapping off a glove with a rare, brief nod]"));
        scene.add(new DialogueLine("Dr. Vasquez", "Good. You’ve got the touch for this, Sterling. Most undergrads are terrified of the animals or too heavy-handed with the equipment. You’re neither."));
        scene.add(new DialogueLine("Narrator", "Dr. Vasquez reaches into her canvas coat and pulls out a folded, slightly crumpled envelope. She hands it to Maya."));
        scene.add(new DialogueLine("Dr. Vasquez", "The Ansel Center Horizon Fellowship. Ten weeks, fully funded, strictly embryo transfer and reproductive genetics. They take three students nationally. I submitted your name last month. You've got an interview Friday."));
        scene.add(new DialogueLine("Narrator", "Maya’s heart seizes. The Horizon Fellowship isn't just an internship; it's a golden ticket to any veterinary or grad school in the country. It’s an impossible leap for someone who started college staring at academic probation."));
        scene.add(new DialogueLine("Maya", "Dr. Vasquez... I don't know what to say. My GPA—"));
        scene.add(new DialogueLine("Dr. Vasquez", "Is a 3.4 now, and you're the only student I trust to run a synchronized breeding protocol without hand-holding. The paper is the paper. The barn is the barn. But be ready. The department head is sitting on the panel."));
        scene.add(new DialogueLine("Maya", "Wait. Dr. Hale?"));
        scene.add(new DialogueLine("Narrator", "[Dr. Vasquez sighing, checking her watch]"));
        scene.add(new DialogueLine("Dr. Vasquez", "Hale is exacting. Don't let him rattle you. Now, wrap up the cables. Reyes is supposed to be mucking stall four, and I haven't heard a pitchfork hit concrete in ten minutes."));
        scene.add(new DialogueLine("Narrator", "Vasquez strides out. Maya stands frozen, the envelope burning a hole in her pocket. Dr. Hale. The man who handed her a D- in ANSC 101, who looked at her during office hours two years ago and asked if she was *sure* the sciences were for her."));
        scene.add(new DialogueLine("Narrator", "The heavy sound of a metal gate clanging snaps her back. Jordan Reyes, a sophomore drowning in a canvas coat three sizes too big, leans against a pitchfork in the adjacent stall, looking physically ill."));
        scene.add(new DialogueLine("Narrator", "[Jordan pale, dark circles under his eyes, gripping the pitchfork like a crutch]"));
        scene.add(new DialogueLine("Jordan", "Maya. Hale just posted the midterm grades for 101. I got a 42."));
        scene.add(new DialogueLine("Narrator", "Jordan drops the pitchfork. It clatters against the cement."));
        scene.add(new DialogueLine("Jordan", "I studied. I swear to God, I ran the flashcards until 3 a.m. If I fail 101, I lose my academic scholarship. I can't go home, Maya. My parents will kill me. Everyone says you're the only one who really gets Hale's exams. How did you pass him? What's the trick?"));
        scene.add(new DialogueLine("Narrator", "Maya feels the old shame crawling up her throat. No one in the department talks about her first year. They only know the current Maya: the 4:30 a.m. workhorse."));

        scene.add(new Choice("Narrator", "How does Maya answer Jordan’s desperate question about her history with Dr. Hale?", List.of(
            new ChoiceOption("Tell Jordan the brutal truth. Tell him about the D- and the failure.", branch(
                new DialogueLine("Maya", "There is no trick, Jordan. My first semester, I took Hale's midterm and got a 38. I finished the class with a D-."),
                new DialogueLine("Narrator", "[Jordan's eyes widening in shock]"),
                new DialogueLine("Jordan", "You? But... Vasquez literally leaves you in charge of the breeding stock."),
                new DialogueLine("Maya", "Yeah. Now. Because after that D-, I spent an entire summer reteaching myself basic biology from scratch. I failed. It sucked. But a 42 isn't a death sentence unless you let it be the end of the story."),
                new DialogueLine("System", "[STAT_UPDATE: +Confidence, +Mentorship]"),
                new DialogueLine("Narrator", "Jordan exhales, a fractional amount of tension leaving his shoulders."),
                new DialogueLine("Jordan", "Can you... can you look at my exam? Just tell me where I'm bleeding points?"),
                new DialogueLine("Maya", "Bring it to the lab later.")
            )),
            new ChoiceOption("Give practical advice but deflect the personal question. Keep the past buried.", branch(
                new DialogueLine("Narrator", "Maya tightens her grip on the ultrasound cables, looking away from Jordan’s panicked eyes."),
                new DialogueLine("Maya", "You just have to learn how he writes his questions, Jordan. He uses double-negatives, and he tests on the footnotes, not just the slides. You have to memorize the pathways, not just the definitions."),
                new DialogueLine("Narrator", "[Jordan slumping, looking utterly defeated]"),
                new DialogueLine("Jordan", "Right. The footnotes. I guess I just... don't have the brain for this. It comes so easy to you."),
                new DialogueLine("Narrator", "Maya swallows the lump in her throat, the ghost of her own D- heavy in her chest. She doesn't correct him."),
                new DialogueLine("System", "[STAT_UPDATE: -Mentorship, -Confidence]"),
                new DialogueLine("Maya", "Just... bring your exam to the lab later. I'll look at it.")
            ))
        )));

        // Scene 2
        scene.add(new DialogueLine("Narrator", "[Scene 2: Tuesday Midday - The Biotech Wing]"));
        scene.add(new DialogueLine("Narrator", "[BACKGROUND: The shared life sciences laboratory. Pristine white counters, humming -80°C freezers, and the faint scent of ethanol. Through the glass walls, the afternoon sun looks sterile and distant.]"));
        scene.add(new DialogueLine("Narrator", "Maya leans against the cold steel of the centrifuge bay, staring at the glowing screen of her phone. She has opened the PDF of her official transcript for the fifth time today."));
        scene.add(new DialogueLine("Narrator", "*Sophomore Year, Fall: ANSC 101 - D-.*\nIt sits there like a permanent stain right before the Horizon Fellowship interview."));
        scene.add(new DialogueLine("Narrator", "[Nora leaning over a micropipette rack, wearing safety goggles pushed up into her messy hair]"));
        scene.add(new DialogueLine("Nora", "If you stare at the PDF any harder, Sterling, you're going to mutate the data through sheer force of anxiety."));
        scene.add(new DialogueLine("Narrator", "Maya jumps, shoving her phone into her pocket. Nora Vance, a biotech major who shares the crossover lab space, doesn't look up from her rack of Eppendorf tubes. Nora is brilliant, chronically under-caffeinated, and carries an air of casual exhaustion."));
        scene.add(new DialogueLine("Maya", "I wasn't staring. I was reviewing."));
        scene.add(new DialogueLine("Nora", "Right. Reviewing. Like I'm just 'reviewing' this restriction enzyme."));
        scene.add(new DialogueLine("Narrator", "Nora's phone buzzes violently on the counter. The screen lights up: *Chloe <3: Are we actually going to talk tonight or are you hiding in the lab again?*\nNora’s eyes flick to the screen. Her expression goes completely blank. Without missing a beat, she reaches over, flips the phone face down, and goes back to her pipetting. The mechanical click of the pipette echoes loudly."));
        scene.add(new DialogueLine("Narrator", "[Nora's voice painfully detached]"));
        scene.add(new DialogueLine("Nora", "Institutional validation is a trap, you know. I see it on your face. You're worried the committee is going to look at some piece of paper and decide you don't belong in the barn. But they don't grade your soul, Maya. Just the paper."));

        scene.add(new Choice("Narrator", "How does Maya engage with Nora's cynical, exhausted wisdom?", List.of(
            new ChoiceOption("\"It's different when your past is sitting on the panel.\" (Vulnerable reflection)", branch(
                new DialogueLine("Maya", "It's different when the man who handed you your worst failure is the one grading your soul on Friday. Dr. Hale is on the Horizon panel. I feel like an imposter just walking into the room."),
                new DialogueLine("Narrator", "Nora finally stops pipetting. She looks at Maya, a profound, tired empathy in her eyes."),
                new DialogueLine("Narrator", "[Nora softening, leaning against the counter]"),
                new DialogueLine("Nora", "Imposter syndrome implies you tricked someone to get here. You didn't trick the cows into getting pregnant, Maya. You did the work. Hale is just a guy in a tweed jacket who probably hasn't touched actual manure in twenty years. Don't let a ghost evict you from a house you built."),
                new DialogueLine("System", "[STAT_UPDATE: +Confidence, +Energy]"),
                new DialogueLine("Maya", "Thanks, Nora. Hey... are you okay? With Chloe?"),
                new DialogueLine("Narrator", "Nora chuckles, a hollow sound."),
                new DialogueLine("Nora", "I'm surviving. We're surviving. Let's just focus on surviving Friday first.")
            )),
            new ChoiceOption("\"I just need to prep harder. No room for error.\" (Defensive workaholic)", branch(
                new DialogueLine("Maya", "I just need to prep harder. If I know the protocols backward and forward, Hale can't touch me. There's no room for error."),
                new DialogueLine("Narrator", "[Nora sighing, resuming her pipetting with sharp, jerky clicks]"),
                new DialogueLine("Nora", "Careful, Sterling. You build a wall that high, and eventually, you're the one suffocating behind it. You can't out-work past mistakes. You just have to outgrow them."),
                new DialogueLine("Narrator", "Maya watches Nora's flipped-over phone, feeling the heavy, isolating weight of her own perfectionism setting in."),
                new DialogueLine("System", "[STAT_UPDATE: -Energy]")
            ))
        )));

        // Scene 3
        scene.add(new DialogueLine("Narrator", "[Scene 3: Tuesday Midnight - The 24/7 Café]"));
        scene.add(new DialogueLine("Narrator", "[BACKGROUND: The campus library café. Neon signs buzzing outside. Inside, the lights are dim, the tables are littered with textbooks, and the air smells like burnt espresso grounds and desperation.]"));
        scene.add(new DialogueLine("Narrator", "Maya sits at a sticky table, an untouched coffee beside her. Her personal statement for the fellowship sits on her laptop, a blinking cursor mocking her."));
        scene.add(new DialogueLine("Narrator", "Across the aisle, Julian \"Jules\" Rivera is operating like a man trying to defuse a bomb. Jules, a senior computer engineering major, is surrounded by three portable monitors displaying dense code. He is intensely, almost frighteningly, focused."));
        scene.add(new DialogueLine("Maya", "Jules. If you drink that fourth espresso, your heart is going to vibrate out of your chest."));
        scene.add(new DialogueLine("Narrator", "[Jules not looking away from the screen, eyes wide and bloodshot]"));
        scene.add(new DialogueLine("Jules", "If this compiling fails, my senior project grade drops to a B+. If I get a B+, my GPA drops to a 3.9. If my GPA drops, my Dad's head explodes, and I lose the Lockheed interview. The espresso is a structural necessity, Maya."));
        scene.add(new DialogueLine("Narrator", "Maya rubs her temples. Jules’s paralyzing terror of a single failure is exhausting to witness, mostly because it reflects her own internal monologue right now."));
        scene.add(new DialogueLine("Narrator", "Jules finally hits 'Execute' and slumps back as a green progress bar begins to fill. He looks over at Maya’s empty document."));
        scene.add(new DialogueLine("Narrator", "[Jules taking a slow, shuddering breath]"));
        scene.add(new DialogueLine("Jules", "Horizon Fellowship, right? You look like you're writing a ransom note, not a personal statement."));
        scene.add(new DialogueLine("Maya", "I have to explain my academic history. I have to somehow make failing a class sound like a 'learning opportunity' and not just... me being an idiot."));
        scene.add(new DialogueLine("Narrator", "Jules laughs—a dry, humorless sound."));
        scene.add(new DialogueLine("Jules", "You can't. Committees don't want learning opportunities. They want flawless execution. They want machines. So be a machine."));
        scene.add(new DialogueLine("Narrator", "Suddenly, Maya’s phone lights up.\n*Jordan: Maya, please. I just tried to do the practice set for Thursday’s quiz. I got a 20%. If I fail this quiz, I can't recover the midterm. Can we do a three-hour session Thursday night? Please? I'm begging.*"));
        scene.add(new DialogueLine("Narrator", "Maya stares at the text. Thursday night. The night before her Friday morning Horizon interview."));
        scene.add(new DialogueLine("Narrator", "Jules leans over, unapologetically reading the text upside down."));
        scene.add(new DialogueLine("Narrator", "[Jules frowning, voice sharpening into steel]"));
        scene.add(new DialogueLine("Jules", "Tell him no. You're bleeding out for someone who didn't read the syllabus. You give them an inch, Maya, they take your GPA. You need Thursday night for yourself. Protect your own oxygen mask first."));

        scene.add(new Choice("Narrator", "Does Maya agree with Jules's ruthless self-preservation, or commit to helping Jordan?", List.of(
            new ChoiceOption("\"You're right. I can't risk my interview for his quiz.\" (Set strict boundaries with Jordan)", branch(
                new DialogueLine("Maya", "You're right. The Horizon interview is Friday morning. If I'm burned out from dragging Jordan through epistasis, I'm going to bomb."),
                new DialogueLine("Narrator", "Maya types a quick reply to Jordan: *I can't do Thursday night, Jordan. I'll send you my master notes and a study guide, but I have my interview. I'm sorry.*"),
                new DialogueLine("Narrator", "[Jules nodding in approval, turning back to his screens]"),
                new DialogueLine("Jules", "Good. Sympathy is a luxury for people who already have their degrees. Welcome to the shark tank, Sterling."),
                new DialogueLine("Narrator", "Maya locks her screen, feeling a cold, heavy knot of guilt in her stomach, but a surge of focused adrenaline."),
                new DialogueLine("System", "[STAT_UPDATE: +Energy, -Mentorship]")
            )),
            new ChoiceOption("\"He's drowning, Jules. I know what that feels like.\" (Commit to tutoring Jordan)", branch(
                new DialogueLine("Maya", "I can't do that. I know exactly what it feels like to sit in that lecture hall and realize you're sinking. Someone has to throw him a rope."),
                new DialogueLine("Narrator", "Maya types a reply to Jordan: *Barn whiteboard. 7 PM Thursday. We don't leave until you can draw the inheritance squares blindfolded.*"),
                new DialogueLine("Narrator", "[Jules shaking his head, looking at Maya like she's a tragedy unfolding]"),
                new DialogueLine("Jules", "You're going to burn yourself alive to keep someone else warm, Maya. Don't come crying to me when Hale eats you alive on Friday because you're running on two hours of sleep."),
                new DialogueLine("System", "[STAT_UPDATE: -Energy, +Mentorship]")
            ))
        )));

        // Scene 4
        scene.add(new DialogueLine("Narrator", "[Scene 4: Wednesday Afternoon - The Hallway]"));
        scene.add(new DialogueLine("Narrator", "[BACKGROUND: The main artery of the agricultural sciences building. Fluorescent lights reflect off the polished linoleum. Glass display cases hold vintage soil corers and faded ribbons.]"));
        scene.add(new DialogueLine("Narrator", "The official Horizon Fellowship interview schedule is pinned to the corkboard outside the department office. Maya approaches it, her stomach tightening."));
        scene.add(new DialogueLine("Narrator", "There it is.\n*Friday, 9:00 AM. Maya Sterling. Panel: Dr. E. Vasquez, Dr. R. Chen, Dr. T. Hale.*"));
        scene.add(new DialogueLine("Dr. Hale", "Reviewing the battlefield, Sterling?"));
        scene.add(new DialogueLine("Narrator", "Maya freezes. Dr. Hale stands behind her, holding a stack of graded lab reports. He is tall, immaculately dressed in a tweed blazer that smells of sterile alcohol wipes and old paper, his expression completely unreadable behind wire-rimmed glasses."));
        scene.add(new DialogueLine("Maya", "Just confirming my time, Dr. Hale."));
        scene.add(new DialogueLine("Narrator", "[Dr. Hale adjusting his glasses, his voice quiet but carrying a devastating weight]"));
        scene.add(new DialogueLine("Dr. Hale", "Dr. Vasquez speaks highly of your practical hours. But the Horizon center demands rigorous academic fundamentals. I'll be interested to see if your... foundation is firmer than it was when you sat in my lecture hall."));
        scene.add(new DialogueLine("Narrator", "He doesn't wait for a response. He simply walks past her into the office, the heavy wooden door clicking shut behind him."));
        scene.add(new DialogueLine("Narrator", "Maya stands alone in the hallway. The bustling noise of the students fades into a high-pitched ringing in her ears. Her hands are shaking. The impostor syndrome crashes back over her like a tidal wave. She isn't the barn expert. She's just the girl who got a D-."));

        // Scene 5
        scene.add(new DialogueLine("Narrator", "[Scene 5: Thursday Night - The Crucible]"));
        scene.add(new DialogueLine("Narrator", "[BACKGROUND: Maya's dorm room. The desk is piled high with textbooks, flashcards, and printed journal articles on bovine embryo transfer. The clock reads 6:45 PM.]"));
        scene.add(new DialogueLine("Narrator", "It's Thursday night. The eve of the interview."));
        scene.add(new DialogueLine("Narrator", "Her phone buzzes. It's Jordan.\n*Jordan: Hey. Still okay to meet? Or if you just sent the notes, did you email them? I'm so nervous.*"));
        scene.add(new DialogueLine("Narrator", "Maya stares at the mountain of prep material she needs to review for Hale’s panel. She remembers Hale’s dismissive voice in the hallway. She remembers Jules's warning. She remembers her own reflection in the barn ultrasound monitor."));

        // Choice 5 which splits the remainder of the story
        scene.add(new Choice("Narrator", "It is time to make the final call. How does Maya spend the most critical night of her academic career?", List.of(
            new ChoiceOption("Go to the barn. Teach Jordan on the whiteboard. Honor the struggle.", branch(
                getBranch5A()
            )),
            new ChoiceOption("Stay in the room. Send Jordan the curated notes. Prepare for Hale.", branch(
                getBranch5B()
            ))
        )));

        return scene;
    }

    private static DialogueLine[] getBranch5A() {
        List<DialogueLine> lines = new LinkedList<>();
        lines.add(new DialogueLine("Narrator", "Maya shoves a dry erase marker into her pocket, grabs her heaviest coat, and leaves the dorm."));
        lines.add(new DialogueLine("Narrator", "[BACKGROUND: The barn's small, drafty office. A scratched whiteboard dominates one wall. The smell of hay and old coffee fills the room.]"));
        lines.add(new DialogueLine("Narrator", "Jordan is already there, looking like he’s walking to his execution. Maya drops her bag on the desk."));
        lines.add(new DialogueLine("Maya", "Alright, Reyes. Pick up a marker. We're doing epistasis, we're doing incomplete dominance, and we are not leaving until you can teach it back to me."));
        lines.add(new DialogueLine("Narrator", "For the next four hours, they battle the material. Maya's feet ache. Her head pounds with exhaustion. But as she forces Jordan to break down complex genetic pathways, something incredible happens."));
        lines.add(new DialogueLine("Narrator", "[Jordan staring at the board, eyes finally clearing]"));
        lines.add(new DialogueLine("Jordan", "Wait... so if the repressor binds here, the pathway is completely shut down. It doesn't matter what the other allele is."));
        lines.add(new DialogueLine("Maya", "Exactly. It masks the expression. You've got it."));
        lines.add(new DialogueLine("Narrator", "As Maya explains the concepts out loud, drawing the diagrams over and over, her own fear begins to evaporate. She isn't just reciting facts; she *understands* the mechanics of life at a fundamental level. The knowledge is in her bones, earned through blood and mud and late nights. Hale's D- is just a ghost. The mastery she has now is real."));
        lines.add(new DialogueLine("Narrator", "By 1:00 AM, Jordan is exhausted but smiling."));
        lines.add(new DialogueLine("Jordan", "Thank you, Maya. Seriously. I owe you my life."));
        lines.add(new DialogueLine("Narrator", "Maya packs up her bag. She is physically drained, running on fumes, but as she walks back across the rainy campus, her mind is a fortress of absolute clarity. Let Hale ask his questions. She is ready."));
        lines.add(new DialogueLine("System", "[STAT_UPDATE: Mentorship MAXED, Energy DEPLETED, Confidence MAXED]"));
        lines.add(new DialogueLine("Narrator", "Tomorrow morning, the panel awaits. Maya will walk in not as a flawless machine, but as a survivor of the dirt and the grind."));
        
        // Scene 6 - Barn Version
        lines.add(new DialogueLine("Narrator", "[Scene 6: Friday Morning - The Waiting Area]"));
        lines.add(new DialogueLine("Narrator", "[BACKGROUND: The antechamber outside the department's main conference room. 8:50 AM. The walls are a sterile, institutional beige. The only artwork is a faded cross-section diagram of a bovine stomach. Outside the frosted glass window, the rain has stopped, leaving a gray, overcast sky.]"));
        lines.add(new DialogueLine("Narrator", "Maya sits rigidly in a hard-backed wooden chair. She is wearing a borrowed blazer that feels too tight across the shoulders. Her hands are clammy, resting on her knees."));
        lines.add(new DialogueLine("Narrator", "Her eyes burn with sleep deprivation. Four hours of sleep isn't enough, and the dull ache in her lower back from standing at the whiteboard is screaming at her. But beneath the physical exhaustion, there is a quiet, steel-reinforced calm."));
        lines.add(new DialogueLine("Narrator", "Her phone buzzes in her pocket.\n*Jordan: Maya. I passed the quiz. 85%. I actually understood the epistasis questions. Thank you. Knock them dead today.*"));
        lines.add(new DialogueLine("Narrator", "Maya reads the text, a small, genuine smile cracking through her anxiety. She rubs her tired eyes. The D- on her transcript doesn't matter. She knows this science in her blood."));
        lines.add(new DialogueLine("Narrator", "The heavy oak door to the conference room clicks open."));
        lines.add(new DialogueLine("Narrator", "[Dr. Vasquez wearing a sharp charcoal suit, offering a warm but professional smile]"));
        lines.add(new DialogueLine("Dr. Vasquez", "We're ready for you, Maya. Come on in."));
        
        lines.addAll(Arrays.asList(getScene7AndEnding()));
        
        return lines.toArray(new DialogueLine[0]);
    }

    private static DialogueLine[] getBranch5B() {
        List<DialogueLine> lines = new LinkedList<>();
        lines.add(new DialogueLine("Narrator", "Maya closes her eyes, takes a deep breath, and opens her laptop."));
        lines.add(new DialogueLine("Narrator", "She attaches her master study guide, annotated with all of Hale's specific testing quirks, and hits send to Jordan.\n*Maya: Jordan, everything you need is in this PDF. Focus on the red-highlighted sections. You can do this. I have to prep for the Horizon panel tonight.*"));
        lines.add(new DialogueLine("Narrator", "She turns off her phone. She blocks out the guilt. She blocks out the world."));
        lines.add(new DialogueLine("Narrator", "[BACKGROUND: Maya's dorm room. The desk lamp casts a harsh, clinical circle of light. Outside, the rain falls in sheets.]"));
        lines.add(new DialogueLine("Narrator", "For the next five hours, Maya runs a brutal, solitary boot camp. She pulls up her old, failed ANSC 101 exams, staring directly at the red ink and the brutal comments Hale left years ago. She forces herself to answer them now, out loud, to the empty room."));
        lines.add(new DialogueLine("Maya", "If the luteal phase is interrupted, progesterone levels drop, triggering a premature return to estrus..."));
        lines.add(new DialogueLine("Narrator", "She builds a flawless mental architecture. She anticipates every trap Hale might lay, every obscure footnote he might weaponize against her. She uses Jules's perfectionist energy, turning her anxiety into a sharp, lethal weapon of preparation."));
        lines.add(new DialogueLine("Narrator", "By midnight, her desk is covered in perfectly organized, color-coded diagrams. She hasn't spent an ounce of energy on anyone else. She is rested, razor-sharp, and coldly prepared."));
        lines.add(new DialogueLine("Narrator", "But as she looks out into the rainy dark, a quiet loneliness settles over her. She wonders if Jordan is currently staring at her PDF, still drowning. She wonders if she is becoming exactly like the people who once looked down on her."));
        lines.add(new DialogueLine("System", "[STAT_UPDATE: Energy MAXED, Confidence HIGH, Mentorship DEPLETED]"));
        lines.add(new DialogueLine("Narrator", "Tomorrow morning, the panel awaits. Maya will walk in with impenetrable armor, ready to prove to Dr. Hale that she is undeniable."));

        // Scene 6 - Solo Prep Version
        lines.add(new DialogueLine("Narrator", "[Scene 6: Friday Morning - The Waiting Area]"));
        lines.add(new DialogueLine("Narrator", "[BACKGROUND: The antechamber outside the department's main conference room. 8:50 AM. The walls are a sterile, institutional beige. The only artwork is a faded cross-section diagram of a bovine stomach. Outside the frosted glass window, the rain has stopped, leaving a gray, overcast sky.]"));
        lines.add(new DialogueLine("Narrator", "Maya sits rigidly in a hard-backed wooden chair. She is wearing a borrowed blazer that feels too tight across the shoulders. Her hands are clammy, resting on her knees."));
        lines.add(new DialogueLine("Narrator", "She is hyper-caffeinated, her pulse thrumming behind her eyes. Every textbook pathway, every hormone cycle, and every footnote from Hale's lectures is indexed perfectly in her mind. She is a fortress of information."));
        lines.add(new DialogueLine("Narrator", "Her phone buzzes in her pocket.\n*Jordan: Took the quiz. Did my best with the PDF. Thanks for sending it.*"));
        lines.add(new DialogueLine("Narrator", "It’s a flat message. Maya swallows hard, pushing away the lingering guilt. *Protect your own oxygen mask first,* Jules had said. She opens a text from Jules himself: *Don't blink today, Sterling. You're a machine.* She locks her screen, retreating behind her impenetrable armor of preparation."));
        lines.add(new DialogueLine("Narrator", "The heavy oak door to the conference room clicks open."));
        lines.add(new DialogueLine("Narrator", "[Dr. Vasquez wearing a sharp charcoal suit, offering a warm but professional smile]"));
        lines.add(new DialogueLine("Dr. Vasquez", "We're ready for you, Maya. Come on in."));

        lines.addAll(Arrays.asList(getScene7AndEnding()));

        return lines.toArray(new DialogueLine[0]);
    }

    private static DialogueLine[] getScene7AndEnding() {
        return new DialogueLine[]{
            new DialogueLine("Narrator", "[Scene 7: The Interview]"),
            new DialogueLine("Narrator", "[BACKGROUND: The conference room is vast and intimidating. A long mahogany table dominates the space. Three empty coffee cups sit in front of the panelists. The smell of floor wax and stale espresso is thick in the air.]"),
            new DialogueLine("Narrator", "Maya takes her seat at the end of the table.\nIn the center is Dr. Vasquez, projecting steady encouragement. To her left is Dr. Chen, a visiting geneticist from the Horizon Center, tapping a silver pen against a notepad. To the right is Dr. Hale. He looks exactly as he did two years ago when Maya sat in his office, begging for extra credit: detached, scrutinizing, and distinctly unimpressed."),
            new DialogueLine("Narrator", "[Dr. Chen leaning forward, pen poised]"),
            new DialogueLine("Dr. Chen", "Ms. Sterling. Your practical hours are extraordinary for an undergraduate. Dr. Vasquez notes you've managed synchronization protocols for the entire herd. Let's start there. Walk me through the hormonal cascade of a CIDR insert removal."),
            new DialogueLine("Narrator", "Maya breathes out. This is her territory."),
            new DialogueLine("Maya", "Upon removal of the controlled internal drug release device, the sudden drop in progesterone removes the negative feedback on the hypothalamus. This triggers a surge in GnRH, which in turn causes the anterior pituitary to release LH and FSH, stimulating follicular maturation and bringing the animal into estrus within 48 to 72 hours."),
            new DialogueLine("Narrator", "Dr. Chen nods, writing something down. Dr. Vasquez gives a fractional, approving blink."),
            new DialogueLine("Narrator", "For twenty minutes, the questions flow. Maya catches them, dissects them, and answers with precision. She is surviving. She is actually doing it."),
            new DialogueLine("Narrator", "Then, Dr. Hale folds his hands over his copy of Maya's transcript."),
            new DialogueLine("Narrator", "[Dr. Hale's voice smooth, quiet, and cutting through the room like a scalpel]"),
            new DialogueLine("Dr. Hale", "A competent recitation of the textbook, Ms. Sterling. But the Horizon Center deals in anomalies, not just routine procedures. Let’s test your... foundations."),
            new DialogueLine("Narrator", "Hale adjusts his wire-rimmed glasses, his eyes locking onto hers."),
            new DialogueLine("Dr. Hale", "You are managing a high-value donor cow on Day 6 of a superovulation protocol. You review her charts and notice her estradiol levels have plateaued unexpectedly, and she is showing mild, atypical lethargy. According to the standard reproductive manual—the one I teach in my 101 course—what is the immediate protocol?"),
            new DialogueLine("Narrator", "Maya's mind races. The textbook. Hale's textbook.\n*Chapter 14, page 312. \"Any deviation in estradiol progression during superovulation indicates follicular regression. Immediately cancel the flush and administer prostaglandin to reset the cycle.\"*"),
            new DialogueLine("Narrator", "But Maya's memory flashes to a freezing morning last November. A Holstein named Bertha. Bertha had plateaued. The textbook said to abort the cycle. But Vasquez had taught Maya to look at the animal, not just the chart. Bertha’s lethargy wasn't follicular regression; it was a mild metabolic imbalance from a feed change. They had adjusted her ration, pushed through, and flushed six viable embryos two days later."),
            new DialogueLine("Narrator", "Maya looks at Hale. He is waiting for the textbook answer. He is waiting for her to prove she finally memorized his rules."),
            new Choice("Narrator", "How does Maya answer Dr. Hale's trap?", List.of(
                new ChoiceOption("\"According to the manual, you abort the cycle and administer prostaglandin.\" (Give him the safe, institutional answer.)", branch(
                    new DialogueLine("Narrator", "Maya swallows the dry lump in her throat. She can't risk it. Not with the fellowship on the line."),
                    new DialogueLine("Maya", "According to standard protocol, an estradiol plateau indicates the follicles are regressing. You must immediately cancel the flush protocol and administer a dose of prostaglandin to regress the corpus luteum and reset her cycle. Proceeding would be a waste of resources."),
                    new DialogueLine("Narrator", "[Dr. Hale leaning back, a thin, satisfied smile on his lips]"),
                    new DialogueLine("Dr. Hale", "Correct. A strict adherence to protocol prevents costly errors. I'm glad to see my foundational lectures eventually took root, Ms. Sterling."),
                    new DialogueLine("Narrator", "Maya looks over at Dr. Vasquez. Vasquez isn't frowning, but a subtle, imperceptible light has gone out of her eyes. It is the look of a mentor who just watched her student choose safety over brilliance."),
                    new DialogueLine("System", "[STAT_UPDATE: Confidence -1, Standing +Institutional]"),
                    new DialogueLine("Narrator", "The rest of the interview concludes smoothly. Maya shakes their hands, steps out of the conference room, and leans against the beige wall.\nShe gave them the perfect answer. She knows, with a hollow certainty, that she will get the fellowship. She played their game and she won. But as she walks down the hallway, the victory feels like a borrowed coat. She didn't win as Maya the barn manager. She won by pretending to be the machine Jules told her to be."),
                    new DialogueLine("System", "*Ending A: The Paper Victory.*")
                )),
                new ChoiceOption("\"The manual says abort. But in the barn, I would check her feed ration first.\" (Risk his anger and stand by her practical experience.)", branch(
                    new DialogueLine("Narrator", "Maya's pulse pounds in her ears. She looks at the red ink of her past, sitting right there in Hale's folder. Then, she thinks of Nora’s exhausted voice in the lab: *Don't let a ghost evict you from a house you built.*"),
                    new DialogueLine("Narrator", "Maya sits up straighter, squaring her shoulders."),
                    new DialogueLine("Maya", "The manual dictates that you immediately cancel the flush and administer prostaglandin. That is the standard protocol."),
                    new DialogueLine("Narrator", "Dr. Hale nods, preparing to move on."),
                    new DialogueLine("Maya", "But I wouldn't do that."),
                    new DialogueLine("Narrator", "The room goes dead silent. Dr. Chen stops tapping his pen. Dr. Hale’s eyebrows slowly rise behind his glasses."),
                    new DialogueLine("Narrator", "[Dr. Hale's voice dropping to a dangerous whisper]"),
                    new DialogueLine("Dr. Hale", "You wouldn't? You would waste the Center's time and money pursuing a failed cycle?"),
                    new DialogueLine("Maya", "I would check her feed first. A plateau on Day 6 with mild lethargy isn't always follicular regression. It can be a metabolic response to a carbohydrate shift in her silage. If her rumen is off, her liver metabolism of steroids changes. If you just look at the paper, you abort a ten-thousand-dollar cycle. If you look at the cow, you adjust her ration, wait twelve hours, and salvage the flush."),
                    new DialogueLine("Narrator", "Hale stares at her. The silence stretches, thick and suffocating. Maya's heart is hammering against her ribs, but she doesn't break eye contact. She has shoveled too much manure, lost too much sleep, and rebuilt herself too thoroughly to apologize for how she learns."),
                    new DialogueLine("Narrator", "Suddenly, a sound breaks the tension.\nDr. Chen is laughing. It’s a bright, delighted sound."),
                    new DialogueLine("Narrator", "[Dr. Chen grinning, turning to Dr. Vasquez]"),
                    new DialogueLine("Dr. Chen", "Elena, you weren't kidding. She doesn't just read the data, she reads the environment. Do you know how many grad students we get who would just blindly terminate that cycle because a PDF told them to?"),
                    new DialogueLine("Narrator", "Dr. Vasquez is beaming, a fierce, protective pride radiating from her."),
                    new DialogueLine("Narrator", "Dr. Hale looks at Dr. Chen, then back to Maya. He slowly closes the folder containing her old, failed transcript."),
                    new DialogueLine("Narrator", "[Dr. Hale sighing, the harshness finally leaving his posture]"),
                    new DialogueLine("Dr. Hale", "It is a risky deviation from the literature, Ms. Sterling. But... an astutely observed one. It seems your education has extended significantly beyond my lecture hall."),
                    new DialogueLine("System", "[STAT_UPDATE: Confidence MAXED, Standing +Authentic]"),
                    new DialogueLine("Narrator", "When Maya walks out of the conference room twenty minutes later, the air in the hallway feels different. Lighter. She pushes open the heavy doors of the Agricultural Sciences building and steps out into the crisp, post-rain Friday morning."),
                    new DialogueLine("Narrator", "She doesn't know if she got the fellowship yet. But as she breathes in the smell of wet asphalt and distant earth, she realizes something far more important: she isn't afraid of the paper anymore. The horizon is finally hers to chase."),
                    new DialogueLine("System", "*Ending B: The Horizon Earned.*")
                ))
            ))
        };
    }
}