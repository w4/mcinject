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

import io.github.jordandoyle.mcinject.asm.ClassTransformer;
import io.github.jordandoyle.mcinject.helpers.ObfuscatedMapping;

public class EntityRendererTransformer extends ClassTransformer {

	@Override
	public void transform() {
		ObfuscatedMapping cl = new ObfuscatedMapping("net/minecraft/src/EntityRenderer");
		ObfuscatedMapping renderWorld = new ObfuscatedMapping("net/minecraft/src/EntityRenderer", "renderWorld", "(FJ)V");
		ObfuscatedMapping renderHand = new ObfuscatedMapping("net/minecraft/src/EntityRenderer", "renderHand", "(FI)V");
		
		InputStream in = getClass().getResourceAsStream("/" + cl.toString().replace(".", "/") + ".class");
		
		// mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/src/EntityRenderer", "renderHand", "(FI)V");
		
		try {
			ClassReader cr = new ClassReader(in);
			ClassNode cn = new ClassNode();
			cr.accept(cn, 0);
			
			for (MethodNode mn : cn.methods) {
				if (!renderWorld.equals(mn))
					continue;
				
				Iterator<AbstractInsnNode> insnNodes = mn.instructions
						.iterator();

				while (insnNodes.hasNext()) {
					AbstractInsnNode insn = insnNodes.next();

					if (insn.getType() == insn.METHOD_INSN) {
						MethodInsnNode node = (MethodInsnNode) insn;
						
						if (renderHand.equals(node)) {
							InsnList list = new InsnList();
							
							list.add(new MethodInsnNode(
									Opcodes.INVOKESTATIC,
                                    "io/github/jordandoyle/mcinject/event/EventFactory",
									"renderHand", "()V"));
							
							mn.instructions.insertBefore(insn, list);
						}
					}
				}
			}

			ClassWriter cw = new ClassWriter(0);
			cn.accept(cw);
			
			loadClass(cw.toByteArray(), new ObfuscatedMapping("net/minecraft/src/EntityRenderer").getMapping());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
