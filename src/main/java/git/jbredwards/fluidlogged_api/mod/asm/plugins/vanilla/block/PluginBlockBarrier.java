package git.jbredwards.fluidlogged_api.mod.asm.plugins.vanilla.block;

import git.jbredwards.fluidlogged_api.mod.asm.plugins.IASMPlugin;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * makes barriers fluidloggable by default
 * @author jbred
 *
 */
public final class PluginBlockBarrier implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        classNode.interfaces.add("git/jbredwards/fluidlogged_api/api/block/IFluidloggable");
        //allow fluids to flow from any side
        addMethod(classNode, "canFluidFlow", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/EnumFacing;)Z",
            null, null, generator -> {
                generator.visitInsn(ICONST_1);
                generator.visitMaxs(1, 0);
            });
        //reimplement barrier particles
        addMethod(classNode, "randomDisplayTick", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)V",
            "fixBarrierParticles", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V",
                generator -> {
                    //adds the client-side only annotation
                    generator.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true)
                            .visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
                    generator.visitVarInsn(ALOAD, 1);
                    generator.visitVarInsn(ALOAD, 2);
                    generator.visitVarInsn(ALOAD, 3);
                    generator.visitMaxs(3, 0);
                });
        return false;
    }
}