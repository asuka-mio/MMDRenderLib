package moe.tarsin.mmdrenderlib.fabric;

import moe.tarsin.mmdrenderlib.MMDRenderLib;
import net.fabricmc.api.ModInitializer;

public class MMDRenderLibFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MMDRenderLib.init();
    }
}
