package com.palight.playerinfo.mixin.client.resources;

import net.minecraft.client.resources.Locale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.io.InputStream;

@Mixin(Locale.class)
public abstract class MixinLocale {
    @Shadow
    protected abstract void loadLocaleData(InputStream inputStream) throws IOException;

    @Redirect(
            method = "loadLocaleDataFiles",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/String;format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"
            )
    )
    private String injectI18nData(String format, Object[] args) {
        try {
            loadLocaleData(MixinLocale.class.getResourceAsStream("/assets/playerinfo/lang/en_us.lang"));
            loadLocaleData(MixinLocale.class.getResourceAsStream("/assets/playerinfo/lang/es_es.lang"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return String.format(format, args);
    }
}
