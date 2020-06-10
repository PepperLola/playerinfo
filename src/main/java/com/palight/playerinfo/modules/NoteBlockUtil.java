package com.palight.playerinfo.modules;

import com.palight.playerinfo.PlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.world.NoteBlockEvent;

import static net.minecraftforge.event.world.NoteBlockEvent.Note;

public class NoteBlockUtil {
    public static int noteId = -1;
    public static int maxLength = (Note.values().length * 2) + 1;

    public static void setNoteId(int noteId) {
        if (noteId != NoteBlockUtil.noteId) {
            NoteBlockUtil.noteId = noteId % maxLength;
        }
    }

    public static int getNoteId() {
        return NoteBlockUtil.noteId;
    }

    public static Note getNote(int id) {
        Note[] notes = Note.values();
        return notes[id % maxLength];
    }

    public static String getNoteString(Note note) {
        return note.name().replace("_",  "").replace("SHARP", "#");
    }

    public static float getPitch(Note note) {
        float pitch = (float)Math.pow(2.0D, (double)(note.ordinal() - 12) / 12.0D);
        return pitch;
    }

    public static Note getRandomNote() {
        Note[] notes = Note.values();
        return notes[PlayerInfo.random.nextInt(notes.length - 1)];
    }
}
