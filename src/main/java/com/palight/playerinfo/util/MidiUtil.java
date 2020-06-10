package com.palight.playerinfo.util;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MidiUtil {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};


    public static Sequence getSequence(String fileName) throws InvalidMidiDataException, IOException {
        return MidiSystem.getSequence(new File(fileName));
    }

    public static List<ShortMessage> readMidi(String fileName) throws InvalidMidiDataException, IOException {
        return readMidi(getSequence(fileName));
    }

    public static List<ShortMessage> readMidi(Sequence sequence) throws InvalidMidiDataException, IOException {
        List<ShortMessage> smList = new ArrayList<ShortMessage>();

        for (Track track :  sequence.getTracks()) {
            for (int i=0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                System.out.println(message.getClass().getSimpleName());
                System.out.print("@" + event.getTick() + " ");
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    System.out.print("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == NOTE_ON) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        smList.add(sm);
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else if (sm.getCommand() == NOTE_OFF) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else {
                        System.out.println("Command:" + sm.getCommand());
                    }
                } else if (message instanceof MetaMessage) {
                    System.out.println(Arrays.toString(((MetaMessage) message).getData()));
                } else {
                    System.out.println("Other message: " + message.getClass());
                }
            }
        }

        return smList;
    }

    public static void playMidiSequence(Sequence sequence) {
        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            if (sequencer == null) {
                return;
            }
            sequencer.open();
            sequencer.setSequence(sequence);
            sequencer.start();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    public static int getKey(ShortMessage sm) {
        return sm.getData1();
    }

    public static int getOctave(ShortMessage sm) {
        return (getKey(sm) / 12) - 1;
    }

    public static int getNote(ShortMessage sm) {
        return getKey(sm) % 12;
    }

    public static int getVelocity(ShortMessage sm) {
        return sm.getData2();
    }

    public static String getNoteName(ShortMessage sm) {
        return NOTE_NAMES[getNote(sm)];
    }

    public static int getLength(ShortMessage sm) {
        return sm.getLength();
    }

    public static int toMCNote(ShortMessage sm) {
        int note = getNote(sm);
        return (note + 2) % 11;
    }
}
