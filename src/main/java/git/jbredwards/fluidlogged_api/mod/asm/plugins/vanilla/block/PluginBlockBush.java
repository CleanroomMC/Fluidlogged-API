package git.jbredwards.fluidlogged_api.mod.asm.plugins.vanilla.block;

import git.jbredwards.fluidlogged_api.mod.asm.plugins.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * breaking this block type no longer voids the possible FluidState here
 * @author jbred
 *
 */
public final class PluginBlockBush implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) {
        return method.name.equals(obfuscated ? "func_176475_e" : "checkAndDropBlock");
    }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        //dropBlock, line 153
        if(checkField(insn, obfuscated ? "field_150350_a" : "AIR", "Lnet/minecraft/block/Block;")) {
            final InsnList list = new InsnList();
            //parameters
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new VarInsnNode(ALOAD, 2));
            //adds new code
            list.add(genMethodNode("getFluidOrAir", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"));
            instructions.insertBefore(insn, list);
            instructions.remove(insn.getNext());
            instructions.remove(insn);
            return true;
        }

        return false;
    }

    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        //add public getter for protected method
        addMethod(classNode, "canSustainBush_Public", "(Lnet/minecraft/block/state/IBlockState;)Z", null, null, generator -> {
            generator.visitVarInsn(ALOAD, 0);
            generator.visitVarInsn(ALOAD, 1);
            generator.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/BlockBush", obfuscated ? "func_185514_i" : "canSustainBush", "(Lnet/minecraft/block/state/IBlockState;)Z", false);
        });

        return true;
    }
}
