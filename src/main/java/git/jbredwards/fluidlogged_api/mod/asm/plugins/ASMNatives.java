package git.jbredwards.fluidlogged_api.mod.asm.plugins;

import git.jbredwards.fluidlogged_api.api.util.FluidState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * methods for this class are built during runtime via PluginASMNatives
 * @author jbred
 *
 */
public final class ASMNatives
{
    /**
     * {@link Block}
     */
    @Nullable
    public static native Boolean getCanFluidFlow(@Nonnull Block block);
    public static native void setCanFluidFlow(@Nonnull Block block, @Nullable Boolean canFluidFlow);

    /**
     * {@link BlockBush}
     */
    public static native boolean canSustainBush(@Nonnull BlockBush bush, @Nonnull IBlockState state);

    /**
     * {@link BlockFluidBase}
     */
    public static native int getFlowDecay(@Nonnull BlockFluidBase block, @Nonnull IBlockAccess world, @Nonnull BlockPos pos);
    public static native boolean hasVerticalFlow(@Nonnull BlockFluidBase block, @Nonnull IBlockAccess world, @Nonnull BlockPos pos);

    /**
     * {@link BlockFluidClassic}
     */
    @Nonnull
    public static native boolean[] getOptimalFlowDirections(@Nonnull BlockFluidClassic block, @Nonnull World world, @Nonnull BlockPos pos);
    public static native int getLargerQuanta(@Nonnull BlockFluidClassic block, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, int compare);
    public static native boolean canFlowInto(@Nonnull BlockFluidClassic block, @Nonnull IBlockAccess world, @Nonnull BlockPos pos);
    public static native void flowIntoBlock(@Nonnull BlockFluidClassic block, @Nonnull World world, @Nonnull BlockPos pos, int meta);

    /**
     * {@link Fluid}
     */
    @Nonnull public static native FluidState getDefaultFluidState(@Nonnull Fluid fluid);
    @Nonnull public static native FluidState setDefaultFluidState(@Nonnull Fluid fluid, @Nonnull FluidState fluidState);

    /**
     * {@link Template}
     */
    public static native void setKeepOldFluidStates(@Nonnull Template template, boolean keepOldFluidStates);
}
