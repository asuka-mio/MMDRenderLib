package moe.tarsin.mmdrenderlib;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class MMDRenderLib {
    public static final Logger logger = LogManager.getLogger();
    public static final String MOD_ID = "mmdrenderlib";
    public static void init() {
        NativeLibraryProvider.Init();
    }
}
