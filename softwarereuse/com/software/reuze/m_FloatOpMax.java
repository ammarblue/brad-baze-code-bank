package com.software.reuze;

public class m_FloatOpMax implements m_i_FloatOpBinary {

    public final float apply(float orig, float brush) {
        return m_MathUtils.max(orig, brush);
    }
}
