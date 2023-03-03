package io.github.tox1cozz.forgenetworkfix;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public interface TargetableMessageCodec<A> {

    void setTargetSide(Class<? extends IMessage> type, Side side);

    boolean canDecode(Class<? extends A> type, Side side);
}