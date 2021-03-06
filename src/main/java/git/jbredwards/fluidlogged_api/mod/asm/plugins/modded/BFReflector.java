package git.jbredwards.fluidlogged_api.mod.asm.plugins.modded;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * stores better foliage methods (gets called even when not installed)
 * @author jbred
 *
 */
public final class BFReflector
{
    @Nullable
    public static Method canRenderBlockInLayer;

    static {
        try {
            final Class<?> Hooks = Class.forName("mods.betterfoliage.client.Hooks");

            canRenderBlockInLayer = ObfuscationReflectionHelper.findMethod(Hooks, "canRenderBlockInLayer", boolean.class, Block.class, IBlockState.class, BlockRenderLayer.class);
            canRenderBlockInLayer.setAccessible(true);
        }
        catch(ClassNotFoundException ignored) {}
    }
}
