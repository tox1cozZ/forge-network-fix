package io.github.tox1cozz.forgenetworkfix.asm.mixin;

import com.google.common.collect.Sets;
import gnu.trove.set.TByteSet;
import gnu.trove.set.hash.TByteHashSet;
import io.github.tox1cozz.forgenetworkfix.TargetableMessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Set;

@Mixin(value = FMLIndexedMessageToMessageCodec.class, remap = false)
public class MixinFMLIndexedMessageToMessageCodec implements TargetableMessageCodec {

    private static final Set<String> FML_CHANNELS = Sets.newHashSet("FML", "FML|HS", "REGISTER", "UNREGISTER");

    private final TByteSet illegalSideMessages = new TByteHashSet();

    @Redirect(method = "decode", at = @At(value = "INVOKE", target = "Lio/netty/buffer/ByteBuf;readByte()B"))
    private byte decode(ByteBuf payload, ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) {
        byte discriminator = payload.readByte();
        String channel = msg.channel();
        if (illegalSideMessages.contains(discriminator) && !FML_CHANNELS.contains(channel)) {
            throw new IllegalStateException("Undefined message side for discriminator " + discriminator + " in channel " + channel);
        }
        return discriminator;
    }

    @Override
    public TByteSet getIllegalSideMessages() {
        return illegalSideMessages;
    }
}