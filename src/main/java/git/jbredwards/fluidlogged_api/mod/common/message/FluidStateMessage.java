package git.jbredwards.fluidlogged_api.mod.common.message;

import git.jbredwards.fluidlogged_api.mod.common.capability.IFluidStateCapability;
import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * send a FluidState to the client
 * @author jbred
 *
 */
public final class FluidStateMessage implements IMessage
{
    public boolean isValid;
    public long pos;
    public int state;
    public boolean doRenderUpdate;

    public FluidStateMessage() {}
    public FluidStateMessage(@Nonnull BlockPos pos, @Nonnull FluidState state, boolean doRenderUpdate) {
        this.isValid = true;
        this.pos = pos.toLong();
        this.state = state.serialize();
        this.doRenderUpdate = doRenderUpdate;
    }

    @Override
    public void fromBytes(@Nonnull ByteBuf buf) {
        isValid = buf.readBoolean();
        if(isValid) {
            pos = buf.readLong();
            state = buf.readInt();
            doRenderUpdate = buf.readBoolean();
        }
    }

    @Override
    public void toBytes(@Nonnull ByteBuf buf) {
        buf.writeBoolean(isValid);
        if(isValid) {
            buf.writeLong(pos);
            buf.writeInt(state);
            buf.writeBoolean(doRenderUpdate);
        }
    }

    public enum Handler implements IMessageHandler<FluidStateMessage, IMessage>
    {
        INSTANCE;

        @Nullable
        @Override
        public IMessage onMessage(@Nonnull FluidStateMessage message, @Nonnull MessageContext ctx) {
            if(message.isValid && ctx.side.isClient()) addTask(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        void addTask(@Nonnull FluidStateMessage message) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                final World world = Minecraft.getMinecraft().world;
                final BlockPos pos = BlockPos.fromLong(message.pos);
                final @Nullable IFluidStateCapability cap = IFluidStateCapability.get(world.getChunk(pos));

                if(cap != null) {
                    final FluidState fluidState = FluidState.deserialize(message.state);

                    //send changes to client
                    cap.setFluidState(pos.toLong(), fluidState);

                    //re-render block
                    FluidloggedUtils.relightFluidBlock(world, pos, fluidState);
                    if(message.doRenderUpdate) world.markBlockRangeForRenderUpdate(pos, pos);
                }
            });
        }
    }
}
