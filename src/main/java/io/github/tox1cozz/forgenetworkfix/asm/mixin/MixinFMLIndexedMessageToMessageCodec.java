package io.github.tox1cozz.forgenetworkfix.asm.mixin;

import gnu.trove.map.TObjectByteMap;
import gnu.trove.map.hash.TObjectByteHashMap;
import io.github.tox1cozz.forgenetworkfix.TargetableMessageCodec;
import io.netty.channel.ChannelHandlerContext;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = FMLIndexedMessageToMessageCodec.class, remap = false)
public class MixinFMLIndexedMessageToMessageCodec<A> implements TargetableMessageCodec<A> {

    protected final TObjectByteMap<Class<? extends IMessage>> targetSides = new TObjectByteHashMap<>();

    @Redirect(method = "decode",
              at = @At(value = "INVOKE", target = "Ljava/lang/Class;newInstance()Ljava/lang/Object;"))
    private A decode(Class<? extends A> messageType, ChannelHandlerContext ctx, FMLProxyPacket packet, List<Object> out) throws Exception {
        if (!canDecode(messageType, packet.getTarget())) {
            throw new IllegalStateException("Undefined message side for type '" + messageType.getName() + "' in channel " + packet.channel());
        }
        return messageType.newInstance();
    }

    @Override
    public void setTargetSide(Class<? extends IMessage> type, Side side) {
        byte mask = toMask(side);
        if (targetSides.containsKey(type)) {
            targetSides.put(type, (byte)(targetSides.get(type) | mask));
        } else {
            targetSides.put(type, mask);
        }
    }

    @Override
    public boolean canDecode(Class<? extends A> type, Side side) {
        return true;
    }

    protected final byte toMask(Side side) {
        return (byte)(1 << side.ordinal());
    }
}