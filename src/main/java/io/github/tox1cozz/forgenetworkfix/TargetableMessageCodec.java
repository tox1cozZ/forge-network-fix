package io.github.tox1cozz.forgenetworkfix;

import gnu.trove.set.TByteSet;

public interface TargetableMessageCodec {

    TByteSet getIllegalSideMessages();
}