package net.themorningcompany.playerinfo.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.themorningcompany.playerinfo.util.MCUtil;
import net.themorningcompany.playerinfo.util.NoteBlockUtil;

import static net.minecraftforge.event.world.NoteBlockEvent.Note;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class NoteBlockListener {
    @SubscribeEvent
    public static void onNoteChange(NoteBlockEvent.Change event) {
        Note note = event.getNote();
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;
        MCUtil.sendPlayerMessage(player,
                TextFormatting.AQUA +
                        "Note Block now set to " +
                        TextFormatting.RESET + TextFormatting.GREEN + TextFormatting.BOLD +
                        NoteBlockUtil.getNoteString(note));
    }
}
