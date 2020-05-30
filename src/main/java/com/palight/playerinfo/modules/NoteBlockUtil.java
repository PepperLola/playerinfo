package com.palight.playerinfo.modules;

import net.minecraftforge.event.world.NoteBlockEvent;

public class NoteBlockUtil {
    public static int noteId = -1;

    public static void setNoteId(int noteId) {
        if (noteId != NoteBlockUtil.noteId) {
            NoteBlockUtil.noteId = noteId % (NoteBlockEvent.Note.values().length * 2);
        }
    }

    public static int getNoteId() {
        return NoteBlockUtil.noteId;
    }

    public static NoteBlockEvent.Note getNote(int id) {
        NoteBlockEvent.Note[] notes = NoteBlockEvent.Note.values();
        return notes[id % (notes.length * 2)];
    }

    public static String getNoteString(NoteBlockEvent.Note note) {
        return note.name().replace("_",  "").replace("SHARP", "#");
    }
}
