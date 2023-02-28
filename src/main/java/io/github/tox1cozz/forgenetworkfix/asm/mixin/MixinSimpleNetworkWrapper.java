package io.github.tox1cozz.forgenetworkfix.asm.mixin;

import gnu.trove.set.TByteSet;
import io.github.tox1cozz.forgenetworkfix.TargetableMessageCodec;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleIndexedCodec;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(value = SimpleNetworkWrapper.class, remap = false)
public class MixinSimpleNetworkWrapper {

    @Shadow
    @Final
    private SimpleIndexedCodec packetCodec;

    // Если один и тот же пакет отрабатывает и на клиенте и на сервере
    private final Set<Class<? extends IMessage>> duplexMessages = new HashSet<>();

    @Inject(method = "registerMessage(Lnet/minecraftforge/fml/common/network/simpleimpl/IMessageHandler;Ljava/lang/Class;ILnet/minecraftforge/fml/relauncher/Side;)V",
            at = @At(value = "HEAD"))
    private <REQ extends IMessage, REPLY extends IMessage> void registerMessage(IMessageHandler<? super REQ, ? extends REPLY> messageHandler, Class<REQ> requestMessageType, int discriminator, Side side, CallbackInfo callback) {
        TByteSet illegalSideMessages = ((TargetableMessageCodec)packetCodec).getIllegalSideMessages();
        if (side == Side.CLIENT && duplexMessages.add(requestMessageType)) {
            illegalSideMessages.add((byte)discriminator);
        } else if (duplexMessages.contains(requestMessageType)) {
            illegalSideMessages.remove((byte)discriminator);
        }
    }
}