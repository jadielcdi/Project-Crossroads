package com.jadielsantiago.crossroadsvn.model;

import static com.jadielsantiago.crossroadsvn.model.Choice.branch;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Noras_Story {

    public static Queue<DialogueLine> getScene() {
        Queue<DialogueLine> scene = new LinkedList<>();

        // Scene 1
        scene.add(new DialogueLine("Narrator", "[Scene 1: Monday Afternoon - The Airlock]"));
        scene.add(new DialogueLine("Narrator", "[BACKGROUND: The shared biotechnology and life sciences laboratory. Pristine white counters, the soft glow of UV hoods, and neatly racked Eppendorf tubes. The afternoon sun filters through the blinds, casting striped shadows across the sterile floor.]"));
        scene.add(new DialogueLine("Narrator", "[AUDIO: The rhythmic, high-pitched hum of a centrifuge in the corner, layered with the sharp, satisfying *click-clack* of a micropipette.]"));
        scene.add(new DialogueLine("Narrator", "The micropipette clicks in Nora's hand, a small satisfying sound, like a seatbelt buckling. Two microliters of guide RNA, drawn up clean, no bubbles. She loves this part. The part where the whole world narrows to a steady hand, a steady breath, and nothing else is allowed in."));
        scene.add(new DialogueLine("Narrator", "[Dr. Okafor pausing by the bench, adjusting her safety glasses]"));
        scene.add(new DialogueLine("Dr. Okafor", "You make that look like breathing, Nora."));
        scene.add(new DialogueLine("Narrator", "Nora doesn't look up, keeping her focus on the tiny clear droplet hanging from the disposable tip. Six months into the sepsis biosensor project, and her hands have stopped needing her brain's permission to do this. The CRISPR-based diagnostic she helped build could flag a bloodstream infection in under an hour instead of the two days a culture took. On Thursday, she’ll present it at the Undergraduate Research Symposium as a finalist for the Callahan Prize."));
        scene.add(new DialogueLine("Nora", "It kind of is, by now. Just muscle memory and a lot of caffeine."));
        scene.add(new DialogueLine("Narrator", "Dr. Okafor smiles, tapping the edge of the bench before walking away. Nora’s phone buzzes violently against the black resin countertop. The screen lights up: *Camille <3*."));
        scene.add(new DialogueLine("Narrator", "*Camille: ugh my roommate did the thing again. the dishes thing. I can't even talk about it I'm so mad. I swear she’s doing it just to mess with my thesis prep.*"));
        scene.add(new DialogueLine("Narrator", "Nora sets the pipette down. She looks at the message. She has a strict run-through of her presentation to do, and the RNA is time-sensitive."));

        scene.add(new Choice("Narrator", "How does Nora handle Camille's familiar spiral?", List.of(
            new ChoiceOption("\"That sounds so frustrating. Did you say something to her?\" (Engage and absorb the emotional labor)", branch(
                new DialogueLine("Narrator", "Nora carefully types back with her thumb, making sure not to touch anything with residual template DNA on it.\n*Nora: That sounds so frustrating. Did you say something to her?*"),
                new DialogueLine("Narrator", "Three bubbles immediately pop up. Camille launches into a massive, multi-paragraph rant. Nora sighs, a quiet, deflating sound, reading through the drama while her samples sit on ice. By the time Nora types, *Pretty good day here, actually—got my run-through slot for Thursday,* Camille has stopped replying. The message sits there, a single gray bubble, unanswered."),
                new DialogueLine("System", "[STAT_UPDATE: +Emotional Labor, -Energy]")
            )),
            new ChoiceOption("\"I'm sorry, Cam. I'm deep in CRISPR assays right now. Can we talk tonight?\" (Set a gentle boundary)", branch(
                new DialogueLine("Narrator", "Nora stares at the screen, feeling the familiar weight of Camille's crisis trying to pull her out of her airlock. She types quickly.\n*Nora: I'm sorry, Cam. I'm deep in CRISPR assays right now and have my symposium run-through. Can we talk tonight?*"),
                new DialogueLine("Narrator", "A minute passes.\n*Camille: wow okay. sorry to bother you with my life.*"),
                new DialogueLine("Narrator", "Nora swallows a lump of guilt, turning her phone face down. It stings, but she picks up the pipette again. The lab is her clean room. She won't let the mess in."),
                new DialogueLine("System", "[STAT_UPDATE: +Self-Respect, -Emotional Labor]")
            ))
        )));

        scene.add(new DialogueLine("Narrator", "At six, packing up, Nora rounds the corner from the animal facility core and nearly collides with a girl whose arms are full of centrifuge tubes on ice."));
        scene.add(new DialogueLine("Narrator", "\"Sorry—sorry, I wasn't looking,\" the girl gasps, catching two tubes against her chest before they hit the floor. She has dark hair pulled back in a failing knot, deeply tired eyes, and a lanyard that reads *MAYA - ANIMAL SCI*."));
        scene.add(new DialogueLine("Narrator", "[Nora reaching out to steady the blue plastic ice tray]"));
        scene.add(new DialogueLine("Nora", "No, that was me. You're doing cortisol assays?"));
        scene.add(new DialogueLine("Narrator", "[Maya blinking in surprise, out of breath]"));
        scene.add(new DialogueLine("Maya", "How did you know that?"));
        scene.add(new DialogueLine("Nora", "The blue caps. That's the kit UCB sells for it. My PI used to run the same panel before we moved fully into the CRISPR stuff. Do you need the plate reader on this floor? Ours has better calibration than the one in Life Sciences."));
        scene.add(new DialogueLine("Narrator", "Maya looks like she might actually cry from relief."));
        scene.add(new DialogueLine("Maya", "I would love that so much you have no idea. I've been fighting with the Life Sciences one for two hours. It hates me specifically."));
        scene.add(new DialogueLine("Narrator", "Nora smiles, badging them into the biotech suite. They talk the whole walk down. Maya explains her research on chronic stress hormones in rescued lab rodents, the mountains of ethics paperwork, and the lonely grind of the barn. Nora recognizes something in the way Maya talks—gentle, fiercely dedicated, and matter-of-fact."));
        scene.add(new DialogueLine("Maya", "Thank you. Seriously. You saved my whole week."));
        scene.add(new DialogueLine("Nora", "Anytime. I'm two benches over. Nora."));
        scene.add(new DialogueLine("Maya", "Maya. I owe you a coffee."));
        scene.add(new DialogueLine("Narrator", "Nora walks home in the dark, thinking that was probably the best conversation she'd had all day."));

        // Scene 2
        scene.add(new DialogueLine("Narrator", "[Scene 2: Tuesday - The Metronome & The Grind]"));
        scene.add(new DialogueLine("Narrator", "[BACKGROUND: The 24-hour campus café, 11:30 PM. The neon sign buzzes against the dark window. The tables are sticky, and the air smells like burnt espresso grounds and desperation.]"));
        scene.add(new DialogueLine("Narrator", "[AUDIO: Lo-fi hip-hop beats playing quietly over the speakers, mixed with the rapid-fire clicking of a mechanical keyboard.]"));
        scene.add(new DialogueLine("Narrator", "Maya made good on her promise. Earlier that morning, she had perched on Nora's lab stool, bringing coffee and observing Nora’s unflappable calm. *“You sigh like a metronome,”* Maya had joked. *“How are you like this?”* Nora had admitted the truth: she compartmentalized because her long-distance girlfriend gave her no other choice."));
        scene.add(new DialogueLine("Narrator", "Now, late Tuesday night, Nora is hunting for a working printer for her poster proofs. She ducks into the 24-hour café near the engineering quad and nearly misses Jules entirely."));
        scene.add(new DialogueLine("Narrator", "Julian \"Jules\" Rivera is folded into a corner booth behind a fortress of color-coded flashcards and three portable monitors."));
        scene.add(new DialogueLine("Narrator", "[Jules not looking up, her voice tight and caffeinated]"));
        scene.add(new DialogueLine("Jules", "Vance. Let me guess. Symposium poster."));
        scene.add(new DialogueLine("Nora", "Guilty."));
        scene.add(new DialogueLine("Narrator", "Nora drops into the seat across from her. Jules looks like she hasn't slept since spring break. She has a highlighter behind each ear and a terrifyingly intense look in her bloodshot eyes."));
        scene.add(new DialogueLine("Jules", "How is your data even done already? I've redone my discussion section six times. Six. My advisor hasn't even seen a full draft. If this compiling fails, my grade drops. If my grade drops, the Lockheed interview is gone."));

        scene.add(new Choice("Narrator", "How does Nora respond to Jules's suffocating academic terror?", List.of(
            new ChoiceOption("\"It's just one project, Jules. Try doing it because you like it, not out of fear.\" (Challenge the perfectionism)", branch(
                new DialogueLine("Jules", "Do you not feel behind, ever? Like something's about to fall apart?"),
                new DialogueLine("Narrator", "Jules asks, staring at her."),
                new DialogueLine("Nora", "Sometimes. But usually about the stuff outside the lab. The work itself doesn't feel like something to survive. I actually like doing it. Maybe try one class you don't need to be perfect in. See what's left when the fear's not doing the driving."),
                new DialogueLine("Narrator", "Jules snorts, a harsh, defensive sound. But slowly, her shoulders drop a fraction of an inch. She picks up a pen and writes something down on the corner of a flashcard. Nora suspects it isn't an assignment."),
                new DialogueLine("System", "[STAT_UPDATE: +Chosen Family, +Self-Respect]")
            )),
            new ChoiceOption("\"I get it. The pressure is insane. You just have to survive it.\" (Validate the stress)", branch(
                new DialogueLine("Jules", "Do you not feel behind, ever? Like something's about to fall apart?"),
                new DialogueLine("Narrator", "Jules asks, staring at her."),
                new DialogueLine("Nora", "Of course I do. The pressure is insane. You just have to build a wall, put your head down, and survive the week. That's what I'm doing."),
                new DialogueLine("Narrator", "[Jules nodding grimly, gripping her coffee cup]"),
                new DialogueLine("Jules", "Exactly. Sympathy is a luxury we can't afford right now. It's a shark tank. Just keep swimming or drown."),
                new DialogueLine("Narrator", "They sit in a heavy, anxious silence, two people trapped in their own separate survival modes."),
                new DialogueLine("System", "[STAT_UPDATE: -Energy]")
            ))
        )));

        // Scene 3
        scene.add(new DialogueLine("Narrator", "[Scene 3: Wednesday - The Phone Calls]"));
        scene.add(new DialogueLine("Narrator", "[BACKGROUND: Nora's sparse off-campus apartment. A printed, full-size mock-up of her poster is taped to the living room wall. Outside, the streetlights flicker in the early evening gloom.]"));
        scene.add(new DialogueLine("Narrator", "[AUDIO: The quiet, isolating hum of a refrigerator.]"));
        scene.add(new DialogueLine("Narrator", "Her mother calls first. Nora almost lets it go to voicemail. She doesn't, because she never does."));
        scene.add(new DialogueLine("Narrator", "[Mother's voice bright, rapid, already mid-thought over the phone line]"));
        scene.add(new DialogueLine("Mother", "Hi, sweetheart, quick thing. Did you hear about your brother? Callahan Sterling wants him for a second interview. *Second* interview, Nora. Do you know what that means for a firm like that?"));
        scene.add(new DialogueLine("Nora", "That's great, Mom."));
        scene.add(new DialogueLine("Narrator", "And it is. Nora means it, mostly, the way you can mean two things at once."));
        scene.add(new DialogueLine("Mother", "I told your father, this is the one. I just have this feeling. Oh, how's your—is it a poster thing you're doing this week?"));

        scene.add(new Choice("Narrator", "Does Nora try to claim space in her mother's attention?", List.of(
            new ChoiceOption("\"It's the symposium. Thursday afternoon. I'm a finalist for a prize.\" (Seek validation)", branch(
                new DialogueLine("Nora", "The symposium. Thursday afternoon at two-thirty. I'm a finalist for the Callahan Prize, actually—different Callahan, funny coincidence."),
                new DialogueLine("Narrator", "[Mother sighing slightly, a dismissive tone slipping in]"),
                new DialogueLine("Mother", "That's nice, honey. Listen, I might not be able to make Thursday work. Daniel's interview got moved, and your father wants to take him to dinner after. Symbolic, you know? I don't want to miss that. You understand."),
                new DialogueLine("Narrator", "Nora’s chest tightens. She understands. She has understood for twenty-one years."),
                new DialogueLine("Nora", "I understand."),
                new DialogueLine("System", "[STAT_UPDATE: -Self-Respect, -Energy]")
            )),
            new ChoiceOption("\"Yeah, a poster thing. It's fine. Congrats to Daniel.\" (Compartmentalize)", branch(
                new DialogueLine("Nora", "Yeah, a poster thing. Just a requirement. Tell Daniel congratulations for me."),
                new DialogueLine("Mother", "I will! We're taking him to dinner Thursday night to celebrate. Symbolic, you know? I don't want to miss that. We'll catch up later, honey!"),
                new DialogueLine("Narrator", "The line clicks dead. Nora stares at the phone. She didn't even give her mother the ammunition to reject her, but the hollow space in her chest still aches."),
                new DialogueLine("System", "[STAT_UPDATE: +Self-Respect]")
            ))
        )));

        scene.add(new DialogueLine("Narrator", "Camille's call comes at eleven."));
        scene.add(new DialogueLine("Narrator", "For forty minutes, Camille talks about a fight with her thesis advisor. Her voice is tight and fast. Nora listens. She murmurs the right things. She offers the right reframes. She feels the familiar ache settle into her jaw from holding her expression so carefully neutral."));
        scene.add(new DialogueLine("Narrator", "At minute thirty-eight, Nora manages to say, \"I have my symposium tomorrow, actually. Are you still thinking you'll call in?\""));
        scene.add(new DialogueLine("Camille", "Oh—yeah, totally, I'll try. What time is it again?"));
        scene.add(new DialogueLine("Narrator", "Nora has told her the time four separate times across two weeks."));
        scene.add(new DialogueLine("Nora", "Two-thirty. My slot's second."));
        scene.add(new DialogueLine("Camille", "Got it. I'll set an alarm."));
        scene.add(new DialogueLine("Narrator", "She doesn't ask what the project is about. She never has."));

        // Scene 4
        scene.add(new DialogueLine("Narrator", "[Scene 4: Thursday Afternoon - The Crucible]"));
        scene.add(new DialogueLine("Narrator", "[BACKGROUND: The grand atrium of the campus convention center. Sunlight streams through the glass ceiling, illuminating dozens of glossy research posters. The room is packed with faculty, wandering underclassmen, and parents wearing guest lanyards.]"));
        scene.add(new DialogueLine("Narrator", "[AUDIO: A loud, echoing din of a hundred simultaneous conversations, punctuated by polite applause.]"));
        scene.add(new DialogueLine("Narrator", "Nora's poster is up by noon. The biosensor diagram is crisp under the halogen lights. When her two-thirty slot arrives, her hands aren't shaking at all."));
        scene.add(new DialogueLine("Narrator", "She talks about detection windows and false-positive rates. She explains the six-month-old idea that has become an actual, working diagnostic tool. Dr. Okafor stands at the back with her arms crossed, radiating a fierce, quiet pride."));
        scene.add(new DialogueLine("Narrator", "Nora looks for Camille's face on her phone screen between questions.\n*No missed call. No text.*"));
        scene.add(new DialogueLine("Narrator", "She looks for her mother in the crowd, out of habit more than hope, and doesn't find her either."));
        scene.add(new DialogueLine("Narrator", "But what she hasn't braced for is Maya."));
        scene.add(new DialogueLine("Narrator", "[Maya standing three rows back, grinning wildly]"));
        scene.add(new DialogueLine("Narrator", "Maya is holding a bouquet of cheap, plastic-wrapped gas-station carnations like they are something precious. Next to her is Jules, holding her phone up, filming the entire presentation without asking."));
        scene.add(new DialogueLine("Narrator", "The judges announce the finalists' scores at four o'clock.\nNora places second for the Callahan Prize. Second out of forty projects."));
        scene.add(new DialogueLine("Narrator", "As she steps away from the podium, the ribbon warm in her hand, her phone buzzes twice in rapid succession."));
        scene.add(new DialogueLine("Narrator", "*Camille: omg I'm so sorry I completely spaced, today has been such a disaster with my advisor, I'll call you tonight?? how'd it go*"));
        scene.add(new DialogueLine("Narrator", "*Mom: Wonderful news!! Daniel got the offer!! Callahan Sterling, associate position. Taking him to dinner tonight. So proud of that boy. How was your poster thing?*"));
        scene.add(new DialogueLine("Narrator", "Nora stands by her poster. She reads the texts. Sixty-one words about Daniel. Six about her. No apology from Camille that isn't really just about herself."));
        scene.add(new DialogueLine("Narrator", "Maya steps up beside her, shoulder to shoulder, the way you stand next to someone at a window when the weather's bad."));
        scene.add(new DialogueLine("Maya", "You did something really good today. Yours was the only poster I actually understood."));
        scene.add(new DialogueLine("Narrator", "Jules walks over, lowering her phone. Jules catches the devastated, hollow look on Nora's face."));
        scene.add(new DialogueLine("Narrator", "[Jules's voice surprisingly soft, completely stripped of her usual frantic edge]"));
        scene.add(new DialogueLine("Jules", "Do you want to get out of here?"));

        scene.add(new Choice("Narrator", "It's the breaking point. What does Nora choose?", List.of(
            new ChoiceOption("\"Yeah. Let's get out of here.\" (Stop making excuses. Choose the people who showed up.)", branch(
                getBranch4A()
            )),
            new ChoiceOption("\"I should stay. I need to call Camille back, she's having a crisis.\" (Maintain the status quo.)", branch(
                getBranch4B()
            ))
        )));

        return scene;
    }

    private static DialogueLine[] getBranch4A() {
        List<DialogueLine> lines = new LinkedList<>();
        lines.add(new DialogueLine("Narrator", "Nora looks at her phone screen one last time. The long-held stitch in her chest finally gives way. The fog burns off."));
        lines.add(new DialogueLine("Nora", "Yeah. Let's go."));
        lines.add(new DialogueLine("Narrator", "They end up at the 24-hour café. Three coffees and a shared plate of fries nobody ordered but somebody needed. Nora finally says it out loud, without editing it to make it palatable."));
        lines.add(new DialogueLine("Nora", "My family hasn't watched me finish a sentence in ten years. And my girlfriend hasn't asked me a real question since February."));
        lines.add(new DialogueLine("Narrator", "Jules slides her phone across the table. The video of Nora's presentation is queued up."));
        lines.add(new DialogueLine("Jules", "For what it's worth, you have proof of what you did today. No matter what anyone else was busy doing instead."));
        lines.add(new DialogueLine("Narrator", "Nora laughs, a wet, surprised sound. It is the beginning of a new foundation."));
        lines.add(new DialogueLine("System", "[STAT_UPDATE: Chosen Family MAXED, Self-Respect MAXED]"));
        
        // Scene 5 (Branch A)
        lines.add(new DialogueLine("Narrator", "[Scene 5: Friday - The Aftermath]"));
        lines.add(new DialogueLine("Narrator", "[BACKGROUND: The campus quad, crisp Friday morning. The air is cool and smells like damp earth and possibility.]"));
        lines.add(new DialogueLine("Narrator", "[AUDIO: A light, uplifting acoustic guitar melody, mixed with the distant chatter of students.]"));
        lines.add(new DialogueLine("Narrator", "Nora sits on a bench, the morning sun on her face. She calls Camille first, before her nerve can cool. Not to fight. Just to stop threading a needle with no thread in it."));
        lines.add(new DialogueLine("Narrator", "Camille answers, immediately defensive. *\"Look, I said I was sorry about yesterday, I've just had so much going on—\"*"));
        lines.add(new DialogueLine("Narrator", "[Nora's voice steady, surprising herself]"));
        lines.add(new DialogueLine("Nora", "I know you have. But I don't think this is working for me anymore. Not the distance. The balance. I can't keep being the only one holding this up."));
        lines.add(new DialogueLine("Narrator", "When the call ends, it hurts. But underneath the hurt is something steadier, like putting a heavy bag down after carrying it three miles too far."));
        lines.add(new DialogueLine("Narrator", "She doesn't call her mother. She sends a single text: *Congrats to Daniel. I got second place at the symposium. Talk soon.* No exclamation points. No performance."));
        lines.add(new DialogueLine("Narrator", "Her phone buzzes. A location pin from Maya.\n*Maya: Cafe. 10 AM. Jules made a color-coded itinerary for us to actually leave campus this weekend. Please come save me from her spreadsheets.*"));
        lines.add(new DialogueLine("Narrator", "Nora smiles. She thinks about the biosensor—about guide RNA finding precisely the sequence it was built for, ignoring all the noise around it. That is the trick. Not aiming for less love, but aiming it precisely at the people who can actually receive it."));
        lines.add(new DialogueLine("Narrator", "She stands up and walks toward the café. She has enough thread to build something real. She'll take it from here."));
        lines.add(new DialogueLine("System", "*Ending A: The New Horizon*"));
        return lines.toArray(new DialogueLine[0]);
    }

    private static DialogueLine[] getBranch4B() {
        List<DialogueLine> lines = new LinkedList<>();
        lines.add(new DialogueLine("Narrator", "Nora swallows hard, the familiar, suffocating blanket of accommodation wrapping around her."));
        lines.add(new DialogueLine("Nora", "I should stay. Camille is having a breakdown over her thesis, I need to call her. And I should call my mom to congratulate Daniel."));
        lines.add(new DialogueLine("Narrator", "Maya’s smile falters, the carnations lowering slightly. Jules frowns, pocketing her phone."));
        lines.add(new DialogueLine("Jules", "Nora... you just won second place."));
        lines.add(new DialogueLine("Nora", "I know. It's fine. Really. Thank you guys for coming, I just... I have to handle this."));
        lines.add(new DialogueLine("Narrator", "Maya hands her the carnations, her eyes full of a sad, quiet understanding. They leave her standing alone in the clearing atrium. Nora dials Camille's number, bracing herself to spend the rest of her triumph comforting someone else."));
        lines.add(new DialogueLine("System", "[STAT_UPDATE: Emotional Labor MAXED, Self-Respect DEPLETED]"));

        // Scene 5 (Branch B)
        lines.add(new DialogueLine("Narrator", "[Scene 5: Friday - The Aftermath]"));
        lines.add(new DialogueLine("Narrator", "[BACKGROUND: Nora's sparse apartment, Friday morning. The mock-up poster is still taped to the wall, but it looks less like a triumph and more like a piece of wallpaper.]"));
        lines.add(new DialogueLine("Narrator", "[AUDIO: The oppressive ticking of a wall clock, accompanied by a low, melancholy drone.]"));
        lines.add(new DialogueLine("Narrator", "Nora sits on her bed, her phone pressed to her ear. She has been on the phone with Camille for over an hour."));
        lines.add(new DialogueLine("Narrator", "[Camille's voice echoing through the speaker, breathless and dramatic]"));
        lines.add(new DialogueLine("Camille", "...and then she had the nerve to ask me for an extension on the data entry! Can you believe her? Oh, wait, Nora, I have to go, my roommate just walked in. We'll celebrate your poster thing this weekend, okay? Love you!"));
        lines.add(new DialogueLine("Narrator", "The line clicks dead."));
        lines.add(new DialogueLine("Narrator", "Nora lowers the phone. She looks at the Callahan Prize ribbon sitting on her desk, half-buried under a stack of printed literature."));
        lines.add(new DialogueLine("Narrator", "She had called her mother last night, spending twenty minutes praising Daniel’s new job, waiting for a question about her symposium that never came. She had chosen to keep the peace, to keep the relationships alive. But as she sits in the quiet room, she realizes she is the only one keeping them on life support."));
        lines.add(new DialogueLine("Narrator", "Her phone buzzes. A text from Maya.\n*Maya: Hey. Just checking in on you. The metronome still ticking?*"));
        lines.add(new DialogueLine("Narrator", "Nora stares at the message. She types a reply, deletes it, and puts the phone face down on the mattress. She picks up a printed journal article and a highlighter, retreating back into the only place that makes sense. The airlock closes. She is safe, and she is entirely alone."));
        lines.add(new DialogueLine("System", "*Ending B: The Echo Chamber*"));
        return lines.toArray(new DialogueLine[0]);
    }
}