package git.jbredwards.fluidlogged_api.mod.asm.plugins.modded;

import git.jbredwards.fluidlogged_api.mod.asm.plugins.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * the betweenlands mod overrides most forge fluid methods to implement its own sudo fluidlogging,
 * this mod fixes everything betweenlands did already, so this undoes all of its stuff
 * @author jbred
 *
 */
public final class PluginBetweenlands implements IASMPlugin
{
    @Override
    public int getMethodIndex(@Nonnull MethodNode method, boolean obfuscated) {
        if(method.name.equals(obfuscated ? "func_176200_f" : "isReplaceable")) return 1;
        else return method.name.equals("place") ? 2 : 0;
    }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        if(index == 1 && checkMethod(insn, obfuscated ? "func_180495_p" : "getBlockState")) {
            //add this.getDefaultState()
            instructions.insert(insn, new MethodInsnNode(INVOKESPECIAL, "net/minecraft/block/Block", obfuscated ? "func_176223_P" : "getDefaultState", "()Lnet/minecraft/block/state/IBlockState;", false));
            instructions.insert(insn, new VarInsnNode(ALOAD, 0));
            //remove world.getBlockState(pos)
            instructions.remove(getPrevious(insn, 2));
            instructions.remove(getPrevious(insn, 1));
            instructions.remove(insn);
            return true;
        }
        else if(index == 2 && checkMethod(insn, obfuscated ? "func_180501_a" : "setBlockState")) {
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 0));
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 5));
            instructions.insertBefore(insn, genMethodNode("fixBetweenlandsPlace", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;ILnet/minecraftforge/fluids/IFluidBlock;Lnet/minecraft/block/state/IBlockState;)Z"));
            instructions.remove(insn);
            return true;
        }

        return false;
    }

    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        classNode.methods.removeIf(method
                -> method.name.equals("canDisplace")
                || method.name.equals("displaceIfPossible")
                || method.name.equals("getFluidHeightForRender")
                || method.name.equals("getFlowVector")
                || method.name.equals("causesDownwardCurrent")
                || method.name.equals("getQuantaValue")
                || method.name.equals("canFlowInto")
                || method.name.equals(obfuscated ? "func_180650_b" : "updateTick")
                || method.name.equals(obfuscated ? "func_176225_a" : "shouldSideBeRendered"));

        return true;
    }
}
