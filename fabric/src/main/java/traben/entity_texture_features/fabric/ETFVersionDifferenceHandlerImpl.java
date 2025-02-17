package traben.entity_texture_features.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@SuppressWarnings("SameReturnValue")
public class ETFVersionDifferenceHandlerImpl {


    public static boolean isThisModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static File getConfigDir() {
        return FabricLoader.getInstance().getConfigDir().toFile();
    }

    public static boolean isForge() {
        return false;
    }

    public static boolean isFabric() {
        return true;
    }

    public static boolean areShadersInUse() {
        return IrisCompat.isShaderPackInUse();
    }

    public static Logger getLogger() {
        //1.19 & 1.18.2 variation
        return LoggerFactory.getLogger("Entity Texture Features");
    }

    public static Text getTextFromTranslation(String translationKey) {
        //1.19.84 version
        return Text.translatable(translationKey);
    }

    @NotNull
    public static String getBiomeString(World world, BlockPos pos) {
        //1.19 & 1.18.2 variation
        return world.getBiome(pos).getKey().toString().split("\s/\s")[1].replaceAll("[^\\da-zA-Z_:-]", "");
    }


}
