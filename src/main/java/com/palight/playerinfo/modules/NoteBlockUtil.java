package com.palight.playerinfo.modules;

import net.minecraftforge.event.world.NoteBlockEvent;

public class NoteBlockUtil {
    public static int noteId = -1;
    public static int maxLength = (NoteBlockEvent.Note.values().length * 2) + 1;

    public static void setNoteId(int noteId) {
        if (noteId != NoteBlockUtil.noteId) {
            NoteBlockUtil.noteId = noteId % maxLength;
        }
    }

    public static int getNoteId() {
        return NoteBlockUtil.noteId;
    }

    public static NoteBlockEvent.Note getNote(int id) {
        NoteBlockEvent.Note[] notes = NoteBlockEvent.Note.values();
        return notes[id % maxLength];
    }

    public static String getNoteString(NoteBlockEvent.Note note) {
        return note.name().replace("_",  "").replace("SHARP", "#");
    }
}
