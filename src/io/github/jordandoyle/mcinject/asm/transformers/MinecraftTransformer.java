package io.github.jordandoyle.mcinject.asm.transformers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import io.github.jordandoyle.mcinject.asm.ClassTransformer;
import io.github.jordandoyle.mcinject.helpers.ObfuscatedMapping;

public class MinecraftTransformer extends ClassTransformer {

	@Override
	public void transform() {
		ObfuscatedMapping cl = new ObfuscatedMapping(
				"net/minecraft/src/Minecraft");
		ObfuscatedMapping runTick = new ObfuscatedMapping(
				"net/minecraft/src/Minecraft", "runTick", "()V");
		ObfuscatedMapping updateController = new ObfuscatedMapping(
				"net/minecraft/src/PlayerControllerMP", "updateController",
				"()V");

		InputStream in = getClass().getResourceAsStream(
				"/" + cl.toString().replace(".", "/") + ".class");

		try {
			ClassReader cr = new ClassReader(in);
			ClassNode cn = new ClassNode();
			cr.accept(cn, 0);

			boolean keyHooked = false;

			for (MethodNode mn : cn.methods) {
				if (!runTick.equals(mn))
					continue;

				Iterator<AbstractInsnNode> insnNodes = mn.instructions
						.iterator();

				while (insnNodes.hasNext()) {
					AbstractInsnNode insn = insnNodes.next();

					if (insn.getType() == insn.METHOD_INSN) {
						MethodInsnNode node = (MethodInsnNode) insn;

						if (updateController.equals(node)) {
							InsnList list = new InsnList();

							list.add(new MethodInsnNode(
									Opcodes.INVOKESTATIC,
                                    "io/github/jordandoyle/mcinject/event/EventFactory",
									"runTick", "()V"));

							mn.instructions.insertBefore(insn, list);
						}

						if (node.owner.equals("org/lwjgl/input/Keyboard")
								&& node.name.equals("getEventKeyState")
								&& node.desc.equals("()Z")
								&& !keyHooked) {
							keyHooked = true;

							InsnList list = new InsnList();

							list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
									"org/lwjgl/input/Keyboard", "getEventKey",
									"()I"));
							list.add(new VarInsnNode(Opcodes.ISTORE, 1));
							list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
									"org/lwjgl/input/Keyboard",
									"getEventKeyState", "()Z"));
							list.add(new VarInsnNode(Opcodes.ISTORE, 2));
							list.add(new VarInsnNode(Opcodes.ILOAD, 1));
							list.add(new VarInsnNode(Opcodes.ILOAD, 2));
							list.add(new MethodInsnNode(
									Opcodes.INVOKESTATIC,
                                    "io/github/jordandoyle/mcinject/event/EventFactory",
									"onKeyPressed", "(IZ)V"));

							mn.instructions.insertBefore(insn, list);
						}
					}
				}
			}

			ClassWriter cw = new ClassWriter(0);
			cn.accept(cw);

			loadClass(cw.toByteArray(), new ObfuscatedMapping(
					"net/minecraft/src/Minecraft").getMapping());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
