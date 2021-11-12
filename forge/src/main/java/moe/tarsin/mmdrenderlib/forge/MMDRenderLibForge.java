package moe.tarsin.mmdrenderlib.forge;

import dev.architectury.platform.forge.EventBuses;
import moe.tarsin.mmdrenderlib.MMDRenderLib;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MMDRenderLib.MOD_ID)
public class MMDRenderLibForge {
    public MMDRenderLibForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(MMDRenderLib.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        MMDRenderLib.init();
    }
}
