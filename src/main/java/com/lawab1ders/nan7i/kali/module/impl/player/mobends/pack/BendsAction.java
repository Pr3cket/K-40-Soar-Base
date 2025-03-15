package com.lawab1ders.nan7i.kali.module.impl.player.mobends.pack;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.util.EnumAxis;
import net.minecraft.util.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class BendsAction {
    public final List<BendsAction.Calculation> calculations = new ArrayList<>();
    public String anim;
    public String model;
    public BendsAction.EnumBoxProperty prop;
    public EnumAxis axis;
    public float smooth;
    public BendsAction.EnumModifier mod;

    public float getNumber(float in) {
        return BendsAction.Calculation.calculateAll(this.mod, in, this.calculations);
    }

    public enum EnumModifier {
        COS,
        SIN
    }

    public enum EnumBoxProperty {
        ROT,
        SCALE,
        PREROT
    }

    public enum EnumOperator {
        SET,
        ADD,
        MULTIPLY,
        DIVIDE,
        SUBSTRACT
    }

    public static class Calculation {
        public final BendsAction.EnumOperator operator;
        public final float number;
        public String globalVar = null;

        public Calculation(BendsAction.EnumOperator argOperator, float argNumber) {
            this.operator = argOperator;
            this.number = argNumber;
        }

        public static float calculateAll(BendsAction.EnumModifier mod, float in,
                                         List<BendsAction.Calculation> argCalc) {
            float out = in;

            for (Calculation calculation : argCalc) {
                out = calculation.calculate(out);
            }

            if (mod == BendsAction.EnumModifier.COS) {
                out = MathHelper.cos(out);
            }

            if (mod == BendsAction.EnumModifier.SIN) {
                out = MathHelper.sin(out);
            }

            return out;
        }

        public float calculate(float in) {
            float num = this.globalVar != null ? BendsVar.getGlobalVar(this.globalVar) : this.number;
            float out = 0.0F;
            if (this.operator == BendsAction.EnumOperator.ADD) {
                out = in + num;
            }

            if (this.operator == BendsAction.EnumOperator.SET) {
                out = num;
            }

            if (this.operator == BendsAction.EnumOperator.SUBSTRACT) {
                out = in - num;
            }

            if (this.operator == BendsAction.EnumOperator.MULTIPLY) {
                out = in * num;
            }

            if (this.operator == BendsAction.EnumOperator.DIVIDE) {
                out = in / num;
            }

            return out;
        }
    }
}
