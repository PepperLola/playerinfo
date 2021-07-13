package com.palight.playerinfo.modules.impl.util;

import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.util.MCUtil;
import com.palight.playerinfo.util.NoteBlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoteBlockMod extends Module {
    public NoteBlockMod() {
        super("noteBlockHelper", ModuleType.UTIL, null, null);
    }

    @SubscribeEvent
    public void onNoteBlockChange(NoteBlockEvent.Change event) {
        if (isEnabled()) {
            if (event.getVanillaNoteId() != NoteBlockUtil.getNoteId()) {
                NoteBlockUtil.setNoteId(event.getVanillaNoteId());
                MCUtil.sendPlayerMessage(
                        Minecraft.getMinecraft().thePlayer,
                        EnumChatFormatting.AQUA +
                                "Note Block now set to " +
                                EnumChatFormatting.RESET + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD +
                                NoteBlockUtil.getNoteString(event.getNote()));
            }
        }
    }
}
