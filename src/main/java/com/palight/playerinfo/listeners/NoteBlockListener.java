package com.palight.playerinfo.listeners;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.palight.playerinfo.util.MCUtil;
import com.palight.playerinfo.util.NoteBlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraftforge.event.world.NoteBlockEvent.Note;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class NoteBlockListener {
    @SubscribeEvent
    public static void onNoteChange(NoteBlockEvent.Change event) {
        Note note = event.getNote();
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;
        MCUtil.sendPlayerMessage(player,
                ChatFormatting.AQUA +
                        "Note Block now set to " +
                        ChatFormatting.RESET + ChatFormatting.GREEN + ChatFormatting.BOLD +
                        NoteBlockUtil.getNoteString(note));
    }
}
