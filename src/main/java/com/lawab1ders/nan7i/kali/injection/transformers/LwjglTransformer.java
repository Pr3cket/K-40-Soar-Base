package com.lawab1ders.nan7i.kali.injection.transformers;

/*
 * Copyright (c) 2025 EldoDebug, Nan7i.南起
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class LwjglTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!name.equals("org.lwjgl.nanovg.NanoVGGLConfig")) {
            return basicClass;
        }

        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, ClassReader.EXPAND_FRAMES);

        for (MethodNode method : node.methods) {
            if (!method.name.equals("configGL")) continue;

            InsnList list = new InsnList();

            list.add(new VarInsnNode(Opcodes.LLOAD, 0));
            list.add(new TypeInsnNode(Opcodes.NEW,
                                      "com/lawab1ders/nan7i/kali/injection/transformers/Lwjgl2FunctionProvider"
            ));
            list.add(new InsnNode(Opcodes.DUP));
            list.add(new MethodInsnNode(
                    Opcodes.INVOKESPECIAL,
                    "com/lawab1ders/nan7i/kali/injection/transformers/Lwjgl2FunctionProvider",
                    "<init>",
                    "()V",
                    false
            ));

            list.add(new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "org/lwjgl/nanovg/NanoVGGLConfig",
                    "config",
                    "(JLorg/lwjgl/system/FunctionProvider;)V",
                    false
            ));

            list.add(new InsnNode(Opcodes.RETURN));

            method.instructions.clear();
            method.instructions.insert(list);
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        node.accept(cw);
        return cw.toByteArray();
    }
}
